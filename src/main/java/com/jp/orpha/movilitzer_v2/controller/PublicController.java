package com.movilitzer.v2.controller;

import com.movilitzer.v2.dto.QueueRequestDto;
import com.movilitzer.v2.dto.TrackDto;
import com.movilitzer.v2.service.SpotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Public", description = "Endpoints públicos por código de local")
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final SpotifyService spotifyService;

    @Operation(summary = "Listado de canciones visibles del local")
    @GetMapping("/venues/{code}/tracks")
    public ResponseEntity<List<TrackDto>> listTracks(@PathVariable String code){
        return ResponseEntity.ok(spotifyService.listPublicTracks(code));
    }

    @Operation(summary = "Encolar canción (Plan A: Spotify Add to Queue)")
    @PostMapping("/venues/{venueId}/queue")
    public ResponseEntity<Void> enqueue(@PathVariable Long venueId, @Valid @RequestBody QueueRequestDto dto){
        spotifyService.addToQueue(venueId, dto.getSpotifyUri());
        return ResponseEntity.accepted().build();
    }
}
