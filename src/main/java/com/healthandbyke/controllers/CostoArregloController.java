package com.healthandbyke.controllers;

import com.healthandbyke.models.CostoArreglo;
import com.healthandbyke.services.CostoArregloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/costoarreglos", "/api/costoarreglo"})
@CrossOrigin(origins = "*")
public class CostoArregloController {

    @Autowired
    private CostoArregloService service;

    @PostMapping
    public ResponseEntity<CostoArreglo> create(@RequestBody CostoArreglo entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<CostoArreglo>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostoArreglo> selectById(@PathVariable int id) {
        CostoArreglo entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostoArreglo> update(@PathVariable int id, @RequestBody CostoArreglo entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
