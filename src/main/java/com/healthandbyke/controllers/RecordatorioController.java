package com.healthandbyke.controllers;

import com.healthandbyke.models.Recordatorio;
import com.healthandbyke.services.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/recordatorios", "/api/recordatorio"})
@CrossOrigin(origins = "*")
public class RecordatorioController {

    @Autowired
    private RecordatorioService service;

    @PostMapping
    public ResponseEntity<Recordatorio> create(@RequestBody Recordatorio entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<Recordatorio>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recordatorio> selectById(@PathVariable int id) {
        Recordatorio entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recordatorio> update(@PathVariable int id, @RequestBody Recordatorio entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
