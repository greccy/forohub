package com.alura.forohub.controller;

import com.alura.forohub.domain.ValidacionException;
import com.alura.forohub.domain.topico.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private RegistroDeTopicos registroDeTopicos;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleTopico> registrar(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder) {

        var topico = registroDeTopicos.registrar(datos);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(
            @PageableDefault(size = 10, sort = {
                    "fechaCreacion" }, direction = Sort.Direction.ASC) Pageable paginacion) {

        var page = repository.findAll(paginacion).map(DatosListaTopico::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> detallar(@PathVariable Long id) {
        var topico = repository.findById(id)
                .orElseThrow(() -> new ValidacionException("No existe un tópico con el ID informado"));
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosDetalleTopico> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizacionTopico datos) {

        var optionalTopico = repository.findById(id);
        if (!optionalTopico.isPresent()) {
            throw new ValidacionException("No existe un tópico con el ID informado");
        }

        if (datos.titulo() != null || datos.mensaje() != null) {
            var titulo = datos.titulo() != null ? datos.titulo() : optionalTopico.get().getTitulo();
            var mensaje = datos.mensaje() != null ? datos.mensaje() : optionalTopico.get().getMensaje();
            if (repository.existsByTituloOrMensaje(titulo, mensaje)) {
                throw new ValidacionException("Ya existe un tópico con ese título o mensaje");
            }
        }

        var topico = optionalTopico.get();
        topico.actualizar(datos);
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
