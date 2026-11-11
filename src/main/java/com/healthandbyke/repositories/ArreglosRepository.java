package com.healthandbyke.repositories;

import com.healthandbyke.models.Arreglos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArreglosRepository extends JpaRepository<Arreglos, Integer> {
}
