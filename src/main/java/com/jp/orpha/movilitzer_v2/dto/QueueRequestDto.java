package com.jp.orpha.movilitzer_v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para encolar un track")
public class QueueRequestDto {

    @NotBlank(message = "La URI de Spotify es obligatoria (ej: spotify:track:xxxx)")
    private String spotifyUri;

    @Email(message = "El email no es válido")
    private String publicUserEmail; // login ligero (opcional)
}