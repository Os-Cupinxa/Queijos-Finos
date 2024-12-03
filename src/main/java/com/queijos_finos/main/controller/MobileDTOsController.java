package com.queijos_finos.main.controller;

import com.queijos_finos.main.dto.*;
import com.queijos_finos.main.model.*;
import com.queijos_finos.main.repository.*;
import com.queijos_finos.main.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MobileDTOsController {

    private final PropriedadeRepository propriedadeRepo;
    private final AmostraRepository amostraRepo;
    private final ContratoRepository contratoRepository;
    private final AgendaRepository agendaRepository;
    private final JwtUtils jwtUtils;

    public MobileDTOsController(
            PropriedadeRepository propriedadeRepo,
            AmostraRepository amostraRepo,
            ContratoRepository contratoRepository,
            AgendaRepository agendaRepository,
            JwtUtils jwtUtils) {
        this.propriedadeRepo = propriedadeRepo;
        this.amostraRepo = amostraRepo;
        this.contratoRepository = contratoRepository;
        this.agendaRepository = agendaRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/dataInsight")
    @ResponseBody
    public DataInsightDTO getDashboardData() {
        Integer type1Count = Math.toIntExact(propriedadeRepo.countBystatus(2));
        Integer type2Count = Math.toIntExact(propriedadeRepo.countBystatus(1));
        Integer type3Count = Math.toIntExact(propriedadeRepo.countBystatus(0));

        return new DataInsightDTO(type1Count, type2Count, type3Count);
    }

    @GetMapping("/agendaAndExpiringContracts")
    @ResponseBody
    public List<AgendaItemsDTO> getFuturesAgendasAndExpiringContracts(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        // Usa o JwtUtils para extrair o userId
        Long userId = jwtUtils.extractClaims(token).get("userId", Long.class);
        LocalDate currentDate = LocalDate.now();
        Date currentDateSQL = Date.valueOf(currentDate);
        LocalDate futureDateAgenda = currentDate.plusDays(7);
        Date futureDateAgendaSQL = Date.valueOf(futureDateAgenda);
        LocalDate futureDateContracts = currentDate.plusDays(30);
        Date futureDateContractsSQL = Date.valueOf(futureDateContracts);

        List<Agenda> agendas = agendaRepository.findFuturesAgendasByUserId(userId, currentDateSQL, futureDateAgendaSQL);

        List<Contrato> expiringContracts = contratoRepository.findExpiringContracts(currentDateSQL, futureDateContractsSQL);

        List<AgendaItemsDTO> agendaItems = agendas.stream()
                .map(agenda -> new AgendaItemsDTO(agenda.getUsuario().getNome(), agenda.getDescricao(), agenda.getData(), "Visita"))
                .toList();

        List<AgendaItemsDTO> contractItems = expiringContracts.stream()
                .map(contract -> new AgendaItemsDTO(contract.getPropriedade().getNomePropriedade(), contract.getNome(), contract.getDataVercimento(), "Contrato"))
                .toList();

        List<AgendaItemsDTO> combinedItems = new ArrayList<>();
        combinedItems.addAll(agendaItems);
        combinedItems.addAll(contractItems);

        return combinedItems;
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
                        propriedade.getContratos(),
                        propriedade.getTecnologias()
                )
        );
    }

    @GetMapping("/propriedadesDTO/producerName")
    @ResponseBody
    public Page<PropriedadeDTO> showPropriedadesDTOByProducerName(
            @RequestParam String nameProducer,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Propriedade> propriedadesPage = propriedadeRepo.findByNomeProdutorContainingIgnoreCase(pageable, nameProducer);

        return propriedadesPage.map(propriedade ->
                new PropriedadeDTO(
                        propriedade.getNomePropriedade(),
                        propriedade.getNomeProdutor(),
                        propriedade.getCidade(),
                        propriedade.getUF(),
                        propriedade.getStatus(),
                        propriedade.getLatitude(),
                        propriedade.getLongitude(),
                        propriedade.getContratos(),
                        propriedade.getTecnologias()
                )
        );
    }



    @GetMapping("/dataPointYear")
    @ResponseBody
    public DataPointDTOYear getLeitePorMes() {
        LocalDate localDate = LocalDate.now();

        LocalDateTime startOfCurrentYear = localDate.withDayOfYear(1).atTime(LocalTime.MIN);
        LocalDateTime endOfCurrentYear = localDate.withDayOfYear(localDate.lengthOfYear()).atTime(LocalTime.MAX);

        LocalDateTime startOfLastYear = localDate.minusYears(1).withDayOfYear(1).atTime(LocalTime.MIN);
        LocalDateTime endOfLastYear = localDate.minusYears(1).withDayOfYear(localDate.minusYears(1).lengthOfYear()).atTime(LocalTime.MAX);

        Timestamp startTimestamp = Timestamp.valueOf(startOfCurrentYear);
        Timestamp endTimestamp = Timestamp.valueOf(endOfCurrentYear);
        Timestamp pastStartTimestamp = Timestamp.valueOf(startOfLastYear);
        Timestamp pastEndTimestamp = Timestamp.valueOf(endOfLastYear);

        int currentYear = startOfCurrentYear.getYear();
        int lastYear = startOfLastYear.getYear();

        List<Amostra> curWeekSamples = amostraRepo.findByDateBetween(startTimestamp, endTimestamp);
        List<Amostra> pastWeekSamples = amostraRepo.findByDateBetween(pastStartTimestamp, pastEndTimestamp);

        List<Double> curData = getQuantitiesForYear(curWeekSamples, currentYear);
        List<Double> pastData = getQuantitiesForYear(pastWeekSamples, lastYear);
        List<String> timeLabelsYear = getAbbreviatedMonthLabels();

        return new DataPointDTOYear(curData, pastData, timeLabelsYear);
    }

    @GetMapping("/dataPoint")
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

    private List<Double> getQuantitiesForYear(List<Amostra> samples, int year) {
        List<Double> quantities = new ArrayList<>(12);

        for (int i = 0; i < 12; i++) {
            quantities.add(0.0);
        }

        List<Amostra> filteredSamples = samples.stream()
                .filter(sample -> {
                    LocalDate sampleDate = sample.getData().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    return sampleDate.getYear() == year;
                })
                .toList();

        for (Amostra sample : filteredSamples) {
            LocalDate sampleDate = sample.getData().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            int monthIndex = sampleDate.getMonthValue() - 1;
            double quantity = sample.getQuantidadeleite();
            quantities.set(monthIndex, quantities.get(monthIndex) + quantity);
        }

        return quantities;
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

    private List<String> getAbbreviatedMonthLabels() {
        List<String> labels = new ArrayList<>();
        String[] monthNames = {
                "JAN", "FEV", "MAR", "ABR", "MAI", "JUN",
                "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"
        };

        Collections.addAll(labels, monthNames);

        return labels;
    }
}

