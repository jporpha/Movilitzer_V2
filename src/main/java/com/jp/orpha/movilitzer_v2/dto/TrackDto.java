package com.jp.orpha.movilitzer_v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Schema(description = "Track visible para usuarios públicos")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class TrackDto {
    private String trackId;
    private String name;
    private String artistNames;
    private String albumName;
    private Long durationMs;
    private String spotifyUrl;
    private String spotifyUri;
    private String imageUrl;
}