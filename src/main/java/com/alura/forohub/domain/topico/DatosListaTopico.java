package com.alura.forohub.domain.topico;

import java.time.LocalDateTime;

public record DatosListaTopico(
        Long id,
        String titulo,
        String autor,
        String curso,
        LocalDateTime fechaCreacion,
        TopicoStatus status) {
    public DatosListaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getAutor(),
                topico.getCurso(),
                topico.getFechaCreacion(),
                topico.getStatus());
    }
}
