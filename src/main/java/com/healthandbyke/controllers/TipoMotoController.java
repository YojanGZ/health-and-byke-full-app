package com.healthandbyke.controllers;

import com.healthandbyke.models.TipoMoto;
import com.healthandbyke.services.TipoMotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/tipomotos", "/api/tipomoto", "/api/motos"})
@CrossOrigin(origins = "*")
public class TipoMotoController {

    @Autowired
    private TipoMotoService service;

    @PostMapping
    public ResponseEntity<TipoMoto> create(@RequestBody TipoMoto entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<TipoMoto>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoMoto> selectById(@PathVariable int id) {
        TipoMoto entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoMoto> update(@PathVariable int id, @RequestBody TipoMoto entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
