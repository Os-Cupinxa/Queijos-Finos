package com.queijos_finos.main.controller;

import com.queijos_finos.main.dto.DataPointDTO;
import com.queijos_finos.main.dto.PropriedadeDTO;
import com.queijos_finos.main.model.*;
import com.queijos_finos.main.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MobileDTOsController {

    private final PropriedadeRepository propriedadeRepo;
    private final AmostraRepository amostraRepo;

    public MobileDTOsController(
            PropriedadeRepository propriedadeRepo,
            AmostraRepository amostraRepo) {
        this.propriedadeRepo = propriedadeRepo;
        this.amostraRepo = amostraRepo;
    }

    @GetMapping("/propriedadesDTO")
    @ResponseBody
    public Page<PropriedadeDTO> showPropriedadesDTO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Propriedade> propriedadesPage = propriedadeRepo.findAll(pageable);

        return propriedadesPage.map(propriedade ->
                new PropriedadeDTO(
                        propriedade.getNomePropriedade(),
                        propriedade.getNomeProdutor(),
                        propriedade.getCidade(),
                        propriedade.getUF(),
                        propriedade.getStatus(),
                        propriedade.getLatitude(),
                        propriedade.getLongitude(),
                        propriedade.getContratos()
                )
        );
    }

    @GetMapping("/datapoint")
    @ResponseBody
    public DataPointDTO getLeitePorDia() {
        LocalDate localDate = LocalDate.now();

        LocalDateTime startDate = localDate.minusDays(6).atTime(LocalTime.MIN);
        LocalDateTime endDate = localDate.minusDays(0).atTime(LocalTime.MAX);

        LocalDateTime pastStartDate = localDate.minusDays(13).atTime(LocalTime.MIN);
        LocalDateTime pastEndDate = localDate.minusDays(7).atTime(LocalTime.MAX);

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        Timestamp pastStartTimestamp = Timestamp.valueOf(pastStartDate);
        Timestamp pastEndTimestamp = Timestamp.valueOf(pastEndDate);

        List<Amostra> curWeekSamples = amostraRepo.findByDateBetween(startTimestamp, endTimestamp);
        List<Amostra> pastWeekSamples = amostraRepo.findByDateBetween(pastStartTimestamp, pastEndTimestamp);

        List<Double> curData = getQuantitiesForWeek(curWeekSamples, startDate.toLocalDate());
        List<Double> pastData = getQuantitiesForWeek(pastWeekSamples, pastStartDate.toLocalDate());
        List<String> timeLabelsCurWeek = getTimeLabels(startDate.toLocalDate());
        List<String> timeLabelsPastWeek = getTimeLabels(pastStartDate.toLocalDate());

        return new DataPointDTO(curData, pastData, timeLabelsCurWeek, timeLabelsPastWeek);
    }

    private List<Double> getQuantitiesForWeek(
            List<Amostra> samples,
            LocalDate startDate) {
        List<Double> quantities = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDateTime dayStart = startDate.plusDays(i).atStartOfDay();
            LocalDateTime dayEnd = dayStart.plusDays(1);

            double totalForDay = samples.stream()
                    .filter(sample -> {
                        LocalDateTime sampleDateTime = sample.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        return !sampleDateTime.isBefore(dayStart) && sampleDateTime.isBefore(dayEnd);
                    })
                    .mapToDouble(Amostra::getQuantidadeleite)
                    .sum();

            quantities.add(totalForDay);
        }

        return quantities;
    }


    private List<String> getTimeLabels(LocalDate startDate) {
        List<String> labels = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = 0; i < 7; i++) {
            labels.add(startDate.plusDays(i).format(formatter));
        }
        return labels;
    }
}

