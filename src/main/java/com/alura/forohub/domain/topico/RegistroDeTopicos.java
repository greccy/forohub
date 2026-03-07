package com.alura.forohub.domain.topico;

import com.alura.forohub.domain.topico.validaciones.ValidadorDeTopicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroDeTopicos {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private List<ValidadorDeTopicos> validadores;

    public Topico registrar(DatosRegistroTopico datos) {
        validadores.forEach(v -> v.validar(datos));

        var topico = new Topico(datos);
        repository.save(topico);
        return topico;
    }
}
