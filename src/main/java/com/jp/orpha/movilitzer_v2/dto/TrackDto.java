package com.movilitzer.v2.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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