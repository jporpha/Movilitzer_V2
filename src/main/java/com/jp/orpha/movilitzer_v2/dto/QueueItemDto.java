package com.movilitzer.v2.dto;
import lombok.*;
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class QueueItemDto {
    private Long id;
    private String spotifyUri;
    private String state;
    private Integer position;
}