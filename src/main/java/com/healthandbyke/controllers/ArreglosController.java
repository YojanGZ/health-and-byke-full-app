package com.healthandbyke.controllers;

import com.healthandbyke.models.Arreglos;
import com.healthandbyke.services.ArreglosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/arreglos", "/api/arregloss"})
@CrossOrigin(origins = "*")
public class ArreglosController {

    @Autowired
    private ArreglosService service;

    @PostMapping
    public ResponseEntity<Arreglos> create(@RequestBody Arreglos entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<Arreglos>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arreglos> selectById(@PathVariable int id) {
        Arreglos entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Arreglos> update(@PathVariable int id, @RequestBody Arreglos entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
