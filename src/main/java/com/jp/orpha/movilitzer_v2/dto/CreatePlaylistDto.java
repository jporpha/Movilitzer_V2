package com.jp.orpha.movilitzer_v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Registrar playlist fuente para un Venue")
public class CreatePlaylistDto {

    @NotBlank(message = "El playlistId de Spotify es obligatorio")
    @Size(max = 120, message = "El playlistId no puede exceder 120 caracteres")
    private String playlistId;

    @NotBlank(message = "El nombre para mostrar es obligatorio")
    @Size(max = 120, message = "El nombre para mostrar no puede exceder 120 caracteres")
    private String displayName;
}
