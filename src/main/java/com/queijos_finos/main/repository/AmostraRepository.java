package com.queijos_finos.main.repository;

import com.queijos_finos.main.model.Amostra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmostraRepository extends JpaRepository<Amostra, Long> {
}