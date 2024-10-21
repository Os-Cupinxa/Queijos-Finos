package com.queijos_finos.main.controller;


import com.queijos_finos.main.dto.DataInsightDTO;
import com.queijos_finos.main.dto.DataPointDTO;
import com.queijos_finos.main.dto.DataPointDTOYear;
import com.queijos_finos.main.dto.PropriedadeDTO;
import com.queijos_finos.main.model.Amostra;
import com.queijos_finos.main.model.Propriedade;
import com.queijos_finos.main.repository.AmostraRepository;
import com.queijos_finos.main.repository.PropriedadeRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MobileControllerTest {
    @Mock
    private PropriedadeRepository propriedadeRepository;

    @Mock
    private AmostraRepository amostraRepository;

    @InjectMocks
    private MobileDTOsController mobileDTOsController;

    @Nested
    class GetDashboardData {

        @Test
        void shouldReturnDataInsightDTO() {
            long active = 10L;
            long activeInContemplation = 20L;
            long dropout = 5L;

            // Configurar o mock para retornar os valores corretos
            when(propriedadeRepository.countBystatus(2)).thenReturn(active);
            when(propriedadeRepository.countBystatus(1)).thenReturn(activeInContemplation);
            when(propriedadeRepository.countBystatus(0)).thenReturn(dropout);


            DataInsightDTO response = mobileDTOsController.getDashboardData();
            assertEquals(active, (long) response.getActive());
            assertEquals(activeInContemplation, (long) response.getActiveInContemplation());
            assertEquals(dropout, (long) response.getDropout());
        }

        @Test
        void shouldLeitePorDia() {
            LocalDate today = LocalDate.now();

            LocalDateTime startDate = today.minusDays(6).atTime(LocalTime.MIN);
            LocalDateTime endDate = today.minusDays(0).atTime(LocalTime.MAX);
            LocalDateTime pastStartDate = today.minusDays(13).atTime(LocalTime.MIN);
            LocalDateTime pastEndDate = today.minusDays(7).atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startDate);
            Timestamp endTimestamp = Timestamp.valueOf(endDate);
            Timestamp pastStartTimestamp = Timestamp.valueOf(pastStartDate);
            Timestamp pastEndTimestamp = Timestamp.valueOf(pastEndDate);


            Amostra curSample1 = new Amostra();
            curSample1.setData(new Date(Timestamp.valueOf(today.minusDays(1).atStartOfDay()).getTime()));
            curSample1.setQuantidadeleite(10.0);

            Amostra curSample2 = new Amostra();
            curSample2.setData(new Date(Timestamp.valueOf(today.minusDays(2).atStartOfDay()).getTime()));
            curSample2.setQuantidadeleite(20.0);

            List<Amostra> curWeekSamples = Arrays.asList(curSample1, curSample2);

            Amostra pastSample1 = new Amostra();
            pastSample1.setData(new Date(Timestamp.valueOf(today.minusDays(8).atStartOfDay()).getTime()));
            pastSample1.setQuantidadeleite(15.0);

            Amostra pastSample2 = new Amostra();
            pastSample2.setData(new Date(Timestamp.valueOf(today.minusDays(9).atStartOfDay()).getTime()));
            pastSample2.setQuantidadeleite(25.0);

            List<Amostra> pastWeekSamples = Arrays.asList(pastSample1, pastSample2);


            when(amostraRepository.findByDateBetween(startTimestamp, endTimestamp)).thenReturn(curWeekSamples);
            when(amostraRepository.findByDateBetween(pastStartTimestamp, pastEndTimestamp)).thenReturn(pastWeekSamples);


            DataPointDTO response = mobileDTOsController.getLeitePorDia();

            List<Double> expectedCurData = Arrays.asList(0.0, 0.0, 0.0, 0.0, 20.0, 10.0, 0.0);
            List<Double> expectedPastData = Arrays.asList(0.0, 0.0, 0.0, 0.0, 25.0, 15.0, 0.0);
            List<String> expectedCurWeekLabels = getTimeLabels(startDate.toLocalDate());
            List<String> expectedPastWeekLabels = getTimeLabels(pastStartDate.toLocalDate());

            assertEquals(expectedCurData, response.getCurData());
            assertEquals(expectedPastData, response.getPastData());
            assertEquals(expectedCurWeekLabels, response.getTimeCurWeek());
            assertEquals(expectedPastWeekLabels, response.getTimePastWeek());
        }

        @Test
        void shouldLeitePorMes() {
            LocalDate today = LocalDate.now();

            LocalDateTime startOfCurrentYear = today.withDayOfYear(1).atTime(LocalTime.MIN);
            LocalDateTime endOfCurrentYear = today.withDayOfYear(today.lengthOfYear()).atTime(LocalTime.MAX);
            LocalDateTime startOfLastYear = today.minusYears(1).withDayOfYear(1).atTime(LocalTime.MIN);
            LocalDateTime endOfLastYear = today.minusYears(1).withDayOfYear(today.minusYears(1).lengthOfYear()).atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startOfCurrentYear);
            Timestamp endTimestamp = Timestamp.valueOf(endOfCurrentYear);
            Timestamp pastStartTimestamp = Timestamp.valueOf(startOfLastYear);
            Timestamp pastEndTimestamp = Timestamp.valueOf(endOfLastYear);

            Amostra curSample1 = new Amostra();
            curSample1.setData(new Date(Timestamp.valueOf(today.withMonth(1).withDayOfMonth(15).atStartOfDay()).getTime()));
            curSample1.setQuantidadeleite(100.0);

            Amostra curSample2 = new Amostra();
            curSample2.setData(new Date(Timestamp.valueOf(today.withMonth(2).withDayOfMonth(10).atStartOfDay()).getTime()));
            curSample2.setQuantidadeleite(200.0);

            List<Amostra> curYearSamples = Arrays.asList(curSample1, curSample2);

            Amostra pastSample1 = new Amostra();
            pastSample1.setData(new Date(Timestamp.valueOf(today.minusYears(1).withMonth(1).withDayOfMonth(20).atStartOfDay()).getTime()));
            pastSample1.setQuantidadeleite(150.0);

            Amostra pastSample2 = new Amostra();
            pastSample2.setData(new Date(Timestamp.valueOf(today.minusYears(1).withMonth(2).withDayOfMonth(25).atStartOfDay()).getTime()));
            pastSample2.setQuantidadeleite(250.0);

            List<Amostra> pastYearSamples = Arrays.asList(pastSample1, pastSample2);

            when(amostraRepository.findByDateBetween(startTimestamp, endTimestamp)).thenReturn(curYearSamples);
            when(amostraRepository.findByDateBetween(pastStartTimestamp, pastEndTimestamp)).thenReturn(pastYearSamples);

            DataPointDTOYear response = mobileDTOsController.getLeitePorMes();

            List<Double> expectedCurData = Arrays.asList(100.0, 200.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0); // Quantidades do ano atual
            List<Double> expectedPastData = Arrays.asList(150.0, 250.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0); // Quantidades do ano passado
            List<String> expectedTimeLabels = getAbbreviatedMonthLabels();

            assertEquals(expectedCurData, response.getCurYear());
            assertEquals(expectedPastData, response.getPastYear());
            assertEquals(expectedTimeLabels, response.getTime());
        }
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