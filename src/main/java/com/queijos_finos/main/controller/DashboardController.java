package com.queijos_finos.main.controller;

import com.queijos_finos.main.dto.TecnologiaCountProp;
import com.queijos_finos.main.model.Propriedade;
import com.queijos_finos.main.repository.PropriedadeRepository;
import com.queijos_finos.main.repository.TecnologiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final PropriedadeRepository propRepo;
    private final TecnologiaRepository tecnologiaRepository;

    public DashboardController(
            PropriedadeRepository propRepo,
            TecnologiaRepository tecnologiaRepository) {
        this.propRepo = propRepo;
        this.tecnologiaRepository = tecnologiaRepository;
    }

    private String getDashboardData(Model model) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Propriedade> top5Properties = propRepo.findTop5ByOrderByIdDesc(pageable).getContent();
        Page<Object[]> results = tecnologiaRepository.countTecnologiaPropriedadesNative(pageable);
        List<TecnologiaCountProp> tecnologiaCountProps = results.stream()
                .map(obj -> new TecnologiaCountProp((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());

        long type1Count = propRepo.countBystatus(2);
        long type2Count = propRepo.countBystatus(1);
        long type3Count = propRepo.countBystatus(0);

        model.addAttribute("type1Count", type1Count);
        model.addAttribute("type2Count", type2Count);
        model.addAttribute("type3Count", type3Count);
        model.addAttribute("propriedades", top5Properties);
        model.addAttribute("topTec", tecnologiaCountProps);
        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return getDashboardData(model);
    }
}
