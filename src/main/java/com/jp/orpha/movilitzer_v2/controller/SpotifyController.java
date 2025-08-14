package com.jp.orpha.movilitzer_v2.controller;

import com.jp.orpha.movilitzer_v2.dto.TrackDto;
import com.jp.orpha.movilitzer_v2.service.SpotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Tag(name = "Spotify", description = "Integración con Spotify (Plan A)")
@RestController
@RequestMapping("/api/v1/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyService spotifyService;

    @Operation(summary = "Obtener URL de autorización OAuth para un venue (copiar y abrir en el navegador)")
    @GetMapping("/{venueId}/authorize")
    public ResponseEntity<Map<String, String>> authorize(@PathVariable("venueId") Long venueId) {
        String url = spotifyService.buildAuthorizationUrl(venueId);
        return ResponseEntity.ok(Map.of("authorizationUrl", url));
    }

    @Operation(summary = "Iniciar autorización OAuth (redirige a Spotify)")
    @GetMapping("/{venueId}/authorize/redirect")
    public ResponseEntity<Void> authorizeRedirect(@PathVariable("venueId") Long venueId) {
        String url = spotifyService.buildAuthorizationUrl(venueId);
        return ResponseEntity.status(302).location(URI.create(url)).build();
    }

    @Operation(summary = "Callback de OAuth (configurar redirect-uri en Spotify)")
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("venueId") Long venueId,
                                           @RequestParam("code") String code){
        spotifyService.handleCallback(venueId, code);
        return ResponseEntity.ok("Conexión Spotify OK para venue " + venueId);
    }

    @Operation(summary = "Sincronizar tracks de la(s) playlist(s) activa(s) del venue")
    @PostMapping("/{venueId}/sync")
    public ResponseEntity<List<TrackDto>> sync(@PathVariable("venueId") Long venueId){
        return ResponseEntity.ok(spotifyService.syncPlaylistTracks(venueId));
    }
}
