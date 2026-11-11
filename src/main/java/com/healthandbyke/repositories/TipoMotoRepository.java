package com.healthandbyke.repositories;

import com.healthandbyke.models.TipoMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMotoRepository extends JpaRepository<TipoMoto, Integer> {
}
