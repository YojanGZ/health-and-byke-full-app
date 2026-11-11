package com.healthandbyke.controllers;

import com.healthandbyke.models.EstadoMoto;
import com.healthandbyke.services.EstadoMotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/estadomotos", "/api/estadomoto"})
@CrossOrigin(origins = "*")
public class EstadoMotoController {

    @Autowired
    private EstadoMotoService service;

    @PostMapping
    public ResponseEntity<EstadoMoto> create(@RequestBody EstadoMoto entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<EstadoMoto>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoMoto> selectById(@PathVariable int id) {
        EstadoMoto entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoMoto> update(@PathVariable int id, @RequestBody EstadoMoto entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
