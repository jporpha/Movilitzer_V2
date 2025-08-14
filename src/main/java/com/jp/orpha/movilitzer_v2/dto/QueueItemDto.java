package com.jp.orpha.movilitzer_v2.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueItemDto {
    private Long id;
    private String spotifyUri;
    private String state;
    private Integer position;
}