package com.healthandbyke.controllers;

import com.healthandbyke.models.Historial;
import com.healthandbyke.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/historial", "/api/historials", "/api/historiales"})
@CrossOrigin(origins = "*")
public class HistorialController {

    @Autowired
    private HistorialService service;

    @PostMapping
    public ResponseEntity<Historial> create(@RequestBody Historial entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<Historial>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historial> selectById(@PathVariable int id) {
        Historial entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Historial> update(@PathVariable int id, @RequestBody Historial entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
