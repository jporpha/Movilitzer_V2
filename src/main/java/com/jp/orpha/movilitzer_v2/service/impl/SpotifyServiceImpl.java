package com.jp.orpha.movilitzer_v2.service.impl;

import com.jp.orpha.movilitzer_v2.dto.TrackDto;
import com.jp.orpha.movilitzer_v2.entity.Venue;
import com.jp.orpha.movilitzer_v2.entity.SpotifyLink;
import com.jp.orpha.movilitzer_v2.entity.PlaylistSource;
import com.jp.orpha.movilitzer_v2.entity.TrackSnapshot;
import com.jp.orpha.movilitzer_v2.exception.BadRequestException;
import com.jp.orpha.movilitzer_v2.exception.NotFoundException;
import com.jp.orpha.movilitzer_v2.mapper.TrackMapper;
import com.jp.orpha.movilitzer_v2.repository.VenueRepository;
import com.jp.orpha.movilitzer_v2.repository.SpotifyLinkRepository;
import com.jp.orpha.movilitzer_v2.repository.PlaylistSourceRepository;
import com.jp.orpha.movilitzer_v2.repository.TrackSnapshotRepository;
import com.jp.orpha.movilitzer_v2.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SpotifyServiceImpl implements SpotifyService {

    private static final long SKEW_SECONDS = 60; // refrescar si faltan <= 60s

    private final WebClient spotifyWebClient;
    private final VenueRepository venueRepository;
    private final SpotifyLinkRepository spotifyLinkRepository;
    private final PlaylistSourceRepository playlistSourceRepository;
    private final TrackSnapshotRepository trackSnapshotRepository;
    private final TrackMapper trackMapper;

    @Value("${movilitzer.spotify.client-id}")
    private String clientId;
    @Value("${movilitzer.spotify.client-secret}")
    private String clientSecret;
    @Value("${movilitzer.spotify.redirect-uri}")
    private String redirectUri;
    @Value("${movilitzer.spotify.scopes}")
    private String scopes;

    // ========= OAUTH =========

    @Override
    public String buildAuthorizationUrl(Long venueId) {
        Venue v = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        String state = "venue_" + venueId;
        return "https://accounts.spotify.com/authorize"
                + "?client_id=" + url(clientId)
                + "&response_type=code"
                + "&redirect_uri=" + url(redirectUri)
                + "&scope=" + url(scopes)
                + "&state=" + url(state);
    }

    @Override
    public void handleCallback(Long venueId, String code) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        TokenResponse token = exchangeCodeForToken(code);

        if (token == null || token.access_token == null) {
            throw new BadRequestException("No se pudo intercambiar el código por tokens");
        }

        SpotifyLink link = spotifyLinkRepository.findByVenue(venue).orElse(SpotifyLink.builder().venue(venue).build());
        link.setAccessToken(token.access_token);
        link.setRefreshToken(token.refresh_token);
        link.setExpiresAt(LocalDateTime.now().plusSeconds(token.expires_in));
        link.setOwnerSpotifyUserId("me");
        link.setActive(true);
        spotifyLinkRepository.save(link);
    }

    private TokenResponse exchangeCodeForToken(String code) {
        String tokenEndpoint = "https://accounts.spotify.com/api/token";
        String basic = basicAuth(clientId, clientSecret);

        return WebClient.builder().build()
                .post().uri(tokenEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=authorization_code&code=" + url(code) + "&redirect_uri=" + url(redirectUri))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    private TokenResponse refreshToken(SpotifyLink link) {
        if (link.getRefreshToken() == null) return null;

        String tokenEndpoint = "https://accounts.spotify.com/api/token";
        String basic = basicAuth(clientId, clientSecret);

        TokenResponse token = WebClient.builder().build()
                .post().uri(tokenEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=refresh_token&refresh_token=" + url(link.getRefreshToken()))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        if (token != null && token.access_token != null) {
            link.setAccessToken(token.access_token);
            // Spotify puede no devolver refresh_token en el refresh; conservar el actual si viene null
            if (token.refresh_token != null && !token.refresh_token.isBlank()) {
                link.setRefreshToken(token.refresh_token);
            }
            // si no viene expires_in, asumir 3600
            int ttl = token.expires_in != null ? token.expires_in : 3600;
            link.setExpiresAt(LocalDateTime.now().plusSeconds(ttl));
            spotifyLinkRepository.save(link);
        }
        return token;
    }

    private String getValidAccessToken(Venue venue) {
        SpotifyLink link = spotifyLinkRepository.findByVenue(venue)
                .orElseThrow(() -> new BadRequestException("Venue sin conexión Spotify"));

        // refresh proactivo si está por expirar
        if (link.getExpiresAt() == null || link.getExpiresAt().isBefore(LocalDateTime.now().plusSeconds(SKEW_SECONDS))) {
            TokenResponse refreshed = refreshToken(link);
            if (refreshed == null || refreshed.access_token == null) {
                throw new BadRequestException("No fue posible refrescar el token de Spotify");
            }
        }
        return link.getAccessToken();
    }

    // ========= PLAYLISTS / SNAPSHOT =========

    @Override
    public List<TrackDto> syncPlaylistTracks(Long venueId) {
        Venue v = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        List<PlaylistSource> sources = playlistSourceRepository.findByVenueAndActiveTrue(v);
        if (sources.isEmpty()) throw new BadRequestException("Venue sin playlists activas");

        // Llamar a Spotify con refresh automático + retry 401
        String token = getValidAccessToken(v);

        for (PlaylistSource ps : sources) {
            PlaylistItemsResponse res = callWithRefreshRetry(
                    () -> spotifyWebClient.get()
                            .uri("/playlists/{id}/tracks?limit=50", ps.getPlaylistId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, this::asError)
                            .bodyToMono(PlaylistItemsResponse.class)
                            .block(),
                    v
            );

            // eliminar previos de esa playlist (simple; luego lo optimizamos a delta)
            trackSnapshotRepository.findAll().stream()
                    .filter(t -> t.getPlaylistSource().getId().equals(ps.getId()))
                    .forEach(trackSnapshotRepository::delete);

            if (res != null && res.items != null) {
                res.items.forEach(item -> {
                    if (item.track != null) {
                        TrackSnapshot ts = TrackSnapshot.builder()
                                .playlistSource(ps)
                                .trackId(item.track.id)
                                .name(item.track.name)
                                .artistNames(item.track.getArtistNames())
                                .albumName(item.track.album != null ? item.track.album.name : null)
                                .durationMs(item.track.duration_ms)
                                .spotifyUrl(item.track.external_urls != null ? item.track.external_urls.spotify : null)
                                .spotifyUri(item.track.uri)
                                .imageUrl(item.track.getImageUrl())
                                .available(true)
                                .build();
                        trackSnapshotRepository.save(ts);
                    }
                });
            }
        }

        var snapshots = trackSnapshotRepository.findByPlaylistSourceInAndAvailableTrueOrderByNameAsc(sources);
        return trackMapper.toDtos(snapshots);
    }

    // ========= QUEUE =========

    @Override
    public void addToQueue(Long venueId, String spotifyUri) {
        Venue v = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        String token = getValidAccessToken(v);

        callWithRefreshRetry(
                () -> spotifyWebClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/me/player/queue").queryParam("uri", spotifyUri).build())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, this::asError)
                        .toBodilessEntity()
                        .block(),
                v
        );
    }

    // ========= PUBLIC LIST =========

    @Override
    public List<TrackDto> listPublicTracks(String accessCode) {
        Venue v = venueRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new NotFoundException("Local no encontrado"));
        var sources = playlistSourceRepository.findByVenueAndActiveTrue(v);
        var snapshots = trackSnapshotRepository.findByPlaylistSourceInAndAvailableTrueOrderByNameAsc(sources);
        return trackMapper.toDtos(snapshots);
    }

    // ========= HELPERS =========

    private <T> T callWithRefreshRetry(Supplier<T> call, Venue venue) {
        try {
            return call.get();
        } catch (BadRequestException ex) {
            throw ex; // errores de dominio propios
        } catch (RuntimeException ex) {
            // Si el error vino de Spotify con 401, intentamos refrescar y reintentar 1 vez
            if (isUnauthorized(ex)) {
                SpotifyLink link = spotifyLinkRepository.findByVenue(venue)
                        .orElseThrow(() -> new BadRequestException("Venue sin conexión Spotify"));
                TokenResponse refreshed = refreshToken(link);
                if (refreshed == null || refreshed.access_token == null) {
                    throw new BadRequestException("No fue posible refrescar el token de Spotify");
                }
                // Reintento una vez con el nuevo access token
                return call.get();
            }
            throw ex;
        }
    }

    private boolean isUnauthorized(Throwable ex) {
        // Simplificado: WebClient lanza WebClientResponseException con status 401. No lo tipamos para no importar más clases.
        String msg = ex.getMessage();
        return msg != null && (msg.contains("401") || msg.toLowerCase().contains("unauthorized"));
    }

    private String basicAuth(String id, String secret) {
        return Base64.getEncoder().encodeToString((id + ":" + secret).getBytes(StandardCharsets.UTF_8));
    }

    private String url(String v) { return URLEncoder.encode(v, StandardCharsets.UTF_8); }

    private Mono<? extends Throwable> asError(ClientResponse r) {
        return r.bodyToMono(String.class)
                .defaultIfEmpty("") // por si no viene body
                .map(body -> new RuntimeException(
                        "Spotify API error " + r.statusCode().value() + ": " + body
                ));
    }

    // ---- Spotify DTOs ----
    @lombok.Data
    static class TokenResponse {
        String access_token;
        String token_type;
        Integer expires_in;
        String refresh_token;
        String scope;
    }

    @lombok.Data
    static class PlaylistItemsResponse {
        java.util.List<Item> items;
    }
    @lombok.Data
    static class Item { Track track; }
    @lombok.Data
    static class Track {
        String id; String name; String uri; Long duration_ms;
        Album album; java.util.List<Artist> artists;
        ExternalUrls external_urls;
        String getArtistNames(){
            if(artists == null) return "";
            return String.join(", ", artists.stream().map(a -> a.name).toList());
        }
        String getImageUrl(){
            if(album == null || album.images == null || album.images.isEmpty()) return null;
            return album.images.get(0).url;
        }
    }
    @lombok.Data static class Album { String name; java.util.List<Image> images; }
    @lombok.Data static class Image { String url; Integer width; Integer height; }
    @lombok.Data static class Artist { String name; }
    @lombok.Data static class ExternalUrls { String spotify; }
}
