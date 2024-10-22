package com.queijos_finos.main.repository;

import com.queijos_finos.main.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    @Query("SELECT a FROM Agenda a WHERE a.usuario.idUsuario = :userId AND a.data > :currentDate")
    List<Agenda> findFuturesAgendasByUserId(Long userId, Date currentDate);

    List<Agenda> findAllByDataAfter(Date currentDate);
}
