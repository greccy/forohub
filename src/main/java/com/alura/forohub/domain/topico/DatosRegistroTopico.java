package com.alura.forohub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank(message = "El título es obligatorio") String titulo,

        @NotBlank(message = "El mensaje es obligatorio") String mensaje,

        @NotBlank(message = "El autor es obligatorio") String autor,

        @NotBlank(message = "El curso es obligatorio") String curso) {
}
