package com.alura.forohub.domain.topico;

import jakarta.validation.constraints.NotNull;

public record DatosActualizacionTopico(
        @NotNull(message = "El ID es obligatorio") Long id,
        String titulo,
        String mensaje,
        TopicoStatus status,
        String curso) {
}
