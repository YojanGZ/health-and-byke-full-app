package com.healthandbyke.repositories;

import com.healthandbyke.models.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Integer> {
}
