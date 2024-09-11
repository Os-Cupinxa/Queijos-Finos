package com.queijos_finos.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.queijos_finos.main.model.Tecnologias;

public interface TecnologiaRepository extends JpaRepository<Tecnologias, Long> {
    List<Tecnologias> findByNome(String nome);

    @Query(value = "SELECT t FROM Tecnologias t ORDER BY t.id DESC LIMIT 1")
    Tecnologias findFirstByOrderByIdDesc();
    
    @Query(value = "SELECT t.nome AS nome, COUNT(tp.tecnologia_id_tecnologia) AS qtd " +
            "FROM tecnologias t " +
            "INNER JOIN tecnologia_has_propriedade tp ON t.id = tp.tecnologia_id_tecnologia " +
            "GROUP BY t.id " +
            "ORDER BY qtd DESC", nativeQuery = true)
    Page<Object[]> countTecnologiaPropriedadesNative(Pageable pageable);
}
