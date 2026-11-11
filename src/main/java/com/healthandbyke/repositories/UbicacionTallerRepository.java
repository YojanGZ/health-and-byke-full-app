package com.healthandbyke.repositories;

import com.healthandbyke.models.UbicacionTaller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionTallerRepository extends JpaRepository<UbicacionTaller, Integer> {
}
