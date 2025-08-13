package com.movilitzer.v2.controller;

import com.movilitzer.v2.dto.TrackDto;
import com.movilitzer.v2.service.SpotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Spotify", description = "Integración con Spotify (Plan A)")
@RestController
@RequestMapping("/api/v1/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyService spotifyService;

    @Operation(summary = "Iniciar autorización OAuth para un venue")
    @PostMapping("/authorize/{venueId}")
    public ResponseEntity<Void> authorize(@PathVariable Long venueId){
        String url = spotifyService.buildAuthorizationUrl(venueId);
        return ResponseEntity.status(302).location(URI.create(url)).build();
    }

    @Operation(summary = "Callback de OAuth (configurar redirect-uri en Spotify)")
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam Long venueId, @RequestParam String code){
        spotifyService.handleCallback(venueId, code);
        return ResponseEntity.ok("Conexión Spotify OK para venue " + venueId);
    }

    @Operation(summary = "Sincronizar tracks de la(s) playlist(s) activa(s) del venue")
    @PostMapping("/sync/{venueId}")
    public ResponseEntity<List<TrackDto>> sync(@PathVariable Long venueId){
        return ResponseEntity.ok(spotifyService.syncPlaylistTracks(venueId));
    }
}
