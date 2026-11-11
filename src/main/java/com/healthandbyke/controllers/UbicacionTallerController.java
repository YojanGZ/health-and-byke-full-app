package com.healthandbyke.controllers;

import com.healthandbyke.models.UbicacionTaller;
import com.healthandbyke.services.UbicacionTallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/ubicaciontallers", "/api/ubicaciontaller", "/api/talleres"})
@CrossOrigin(origins = "*")
public class UbicacionTallerController {

    @Autowired
    private UbicacionTallerService service;

    @PostMapping
    public ResponseEntity<UbicacionTaller> create(@RequestBody UbicacionTaller entity) {
        service.create(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<UbicacionTaller>> selectAll() {
        return ResponseEntity.ok(service.selectAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionTaller> selectById(@PathVariable int id) {
        UbicacionTaller entity = service.selectById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionTaller> update(@PathVariable int id, @RequestBody UbicacionTaller entity) {
        service.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
