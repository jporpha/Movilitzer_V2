package com.jp.orpha.movilitzer_v2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class QueueRequestDto {
    @NotBlank
    private String spotifyUri;
    private String publicUserEmail; // login ligero
}