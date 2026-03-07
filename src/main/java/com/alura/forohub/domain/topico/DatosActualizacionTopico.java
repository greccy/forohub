package com.alura.forohub.domain.topico;

public record DatosActualizacionTopico(
                String titulo,
                String mensaje,
                TopicoStatus status,
                String curso) {
}
