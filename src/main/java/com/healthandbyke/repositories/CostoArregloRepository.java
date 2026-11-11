package com.healthandbyke.repositories;

import com.healthandbyke.models.CostoArreglo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostoArregloRepository extends JpaRepository<CostoArreglo, Integer> {
}
