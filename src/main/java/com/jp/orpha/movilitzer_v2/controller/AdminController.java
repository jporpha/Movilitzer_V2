package com.jp.orpha.movilitzer_v2.controller;

import com.jp.orpha.movilitzer_v2.dto.CreateVenueDto;
import com.jp.orpha.movilitzer_v2.dto.CreatePlaylistDto;
import com.jp.orpha.movilitzer_v2.entity.PlaylistSource;
import com.jp.orpha.movilitzer_v2.entity.Venue;
import com.jp.orpha.movilitzer_v2.repository.PlaylistSourceRepository;
import com.jp.orpha.movilitzer_v2.repository.VenueRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "Endpoints administrativos")
@Validated
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VenueRepository venueRepository;
    private final PlaylistSourceRepository playlistSourceRepository;

    @Operation(summary = "Crear Venue")
    @PostMapping("/venues")
    public ResponseEntity<Venue> createVenue(@Valid @RequestBody CreateVenueDto dto){
        Venue v = Venue.builder()
                .name(dto.getName())
                .timezone(dto.getTimezone())
                .status("ACTIVE")
                .accessCode(java.util.UUID.randomUUID().toString().substring(0,6).toUpperCase())
                .build();
        return ResponseEntity.ok(venueRepository.save(v));
    }

    @Operation(summary = "Agregar PlaylistSource a un Venue")
    @PostMapping("/venues/{venueId}/playlist-sources")
    public ResponseEntity<PlaylistSource> addPlaylist(@PathVariable("venueId") @Positive(message = "venueId debe ser positivo") Long venueId,
                                                      @Valid @RequestBody CreatePlaylistDto dto) {
        Venue v = venueRepository.findById(venueId).orElseThrow();
        PlaylistSource ps = PlaylistSource.builder()
                .venue(v)
                .provider("SPOTIFY")
                .playlistId(dto.getPlaylistId())
                .displayName(dto.getDisplayName())
                .active(true)
                .build();
        return ResponseEntity.ok(playlistSourceRepository.save(ps));
    }
}