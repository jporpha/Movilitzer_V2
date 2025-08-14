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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotifyServiceImpl implements SpotifyService {

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

    @Override
    public String buildAuthorizationUrl(Long venueId) {
        Venue v = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        String state = "venue_" + venueId;
        String url = "https://accounts.spotify.com/authorize"
                + "?client_id=" + url(clientId)
                + "&response_type=code"
                + "&redirect_uri=" + url(redirectUri)
                + "&scope=" + url(scopes)
                + "&state=" + url(state);
        return url;
    }

    @Override
    public void handleCallback(Long venueId, String code) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        String tokenEndpoint = "https://accounts.spotify.com/api/token";
        String basic = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

        TokenResponse token = WebClient.builder().build()
                .post().uri(tokenEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=authorization_code&code=" + url(code) + "&redirect_uri=" + url(redirectUri))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        if(token == null || token.access_token == null){
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

    @Override
    public List<TrackDto> syncPlaylistTracks(Long venueId) {
        Venue v = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        List<PlaylistSource> sources = playlistSourceRepository.findByVenueAndActiveTrue(v);
        if(sources.isEmpty()) throw new BadRequestException("Venue sin playlists activas");

        SpotifyLink link = spotifyLinkRepository.findByVenue(v).orElseThrow(() -> new BadRequestException("Venue sin conexión Spotify"));
        String token = link.getAccessToken();

        // Simple sync: borra y vuelve a cargar
        for(PlaylistSource ps : sources){
            PlaylistItemsResponse res = spotifyWebClient.get()
                    .uri("/playlists/{id}/tracks?limit=50", ps.getPlaylistId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(PlaylistItemsResponse.class)
                    .block();

            // eliminar previos de esa playlist
            trackSnapshotRepository.findAll().stream()
                    .filter(t -> t.getPlaylistSource().getId().equals(ps.getId()))
                    .forEach(trackSnapshotRepository::delete);

            if(res != null && res.items != null){
                res.items.forEach(item -> {
                    if(item.track != null){
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

    @Override
    public void addToQueue(Long venueId, String spotifyUri) {
        Venue v = venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException("Venue no encontrado"));
        SpotifyLink link = spotifyLinkRepository.findByVenue(v).orElseThrow(() -> new BadRequestException("Venue sin conexión Spotify"));
        String token = link.getAccessToken();

        spotifyWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/me/player/queue")
                        .queryParam("uri", spotifyUri).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public List<TrackDto> listPublicTracks(String accessCode) {
        Venue v = venueRepository.findByAccessCode(accessCode).orElseThrow(() -> new NotFoundException("Local no encontrado"));
        var sources = playlistSourceRepository.findByVenueAndActiveTrue(v);
        var snapshots = trackSnapshotRepository.findByPlaylistSourceInAndAvailableTrueOrderByNameAsc(sources);
        return trackMapper.toDtos(snapshots);
    }

    private String url(String v){ return URLEncoder.encode(v, StandardCharsets.UTF_8); }

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
