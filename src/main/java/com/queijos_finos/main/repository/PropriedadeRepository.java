package com.queijos_finos.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.queijos_finos.main.model.Propriedade;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {
    Page<Propriedade> findAll(Pageable pageable);

    long countBystatus(int status);


    @Query("SELECT p FROM Propriedade p WHERE p.id NOT IN (SELECT DISTINCT c.propriedade.id FROM Contrato c)")
    List<Propriedade> findWithoutContrato();
    
    @Query("SELECT p FROM Propriedade p ORDER BY p.id DESC")
    Page<Propriedade> findTop5ByOrderByIdDesc(Pageable pageable);

}
