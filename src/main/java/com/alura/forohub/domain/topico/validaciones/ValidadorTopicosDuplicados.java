package com.alura.forohub.domain.topico.validaciones;

import com.alura.forohub.domain.ValidacionException;
import com.alura.forohub.domain.topico.DatosRegistroTopico;
import com.alura.forohub.domain.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorTopicosDuplicados implements ValidadorDeTopicos {

    @Autowired
    private TopicoRepository repository;

    @Override
    public void validar(DatosRegistroTopico datos) {
        var duplicado = repository.existsByTituloOrMensaje(datos.titulo(), datos.mensaje());
        if (duplicado) {
            throw new ValidacionException("Ya existe un tópico con ese título o mensaje");
        }
    }
}
