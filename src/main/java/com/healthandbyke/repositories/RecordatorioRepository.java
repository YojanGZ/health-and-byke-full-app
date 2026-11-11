package com.healthandbyke.repositories;

import com.healthandbyke.models.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Integer> {
}
