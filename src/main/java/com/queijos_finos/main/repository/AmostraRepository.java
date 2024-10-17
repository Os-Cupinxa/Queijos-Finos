package com.queijos_finos.main.repository;

import com.queijos_finos.main.model.Amostra;
import com.queijos_finos.main.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AmostraRepository extends JpaRepository<Amostra, Long> {
    @Query("SELECT a FROM Amostra a WHERE a.data BETWEEN :startDate AND :endDate ORDER BY a.data ASC")
    List<Amostra> findByDateBetween(Timestamp startDate, Timestamp endDate);
}
