package com.healthandbyke.repositories;

import com.healthandbyke.models.EstadoMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoMotoRepository extends JpaRepository<EstadoMoto, Integer> {
}
