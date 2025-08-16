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
@Schema(description = "Crear un local (Venue)")
public class CreateVenueDto {

    @NotBlank(message = "El nombre del local es obligatorio")
    @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
    private String name;

    @NotBlank(message = "La zona horaria es obligatoria (ej: America/Santiago)")
    private String timezone;
}
