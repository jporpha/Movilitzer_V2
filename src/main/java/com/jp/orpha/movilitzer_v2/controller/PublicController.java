package com.jp.orpha.movilitzer_v2.controller;

import com.jp.orpha.movilitzer_v2.dto.QueueRequestDto;
import com.jp.orpha.movilitzer_v2.dto.TrackDto;
import com.jp.orpha.movilitzer_v2.service.SpotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Public", description = "Endpoints públicos por código de local")
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final SpotifyService spotifyService;

    @Operation(summary = "Listado de canciones visibles del local")
    @GetMapping("/venues/{code}/tracks")
    public ResponseEntity<List<TrackDto>> listTracks(@PathVariable("code") String code){
        return ResponseEntity.ok(spotifyService.listPublicTracks(code));
    }

    @Operation(summary = "Encolar canción (Plan A: Spotify Add to Queue)")
    @PostMapping("/venues/{venueId}/queue")
    public ResponseEntity<Void> enqueue(@PathVariable("venueId") Long venueId,
                                        @Valid @RequestBody QueueRequestDto dto){
        spotifyService.addToQueue(venueId, dto.getSpotifyUri());
        return ResponseEntity.accepted().build();
    }
}
