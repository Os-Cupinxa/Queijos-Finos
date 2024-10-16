package com.queijos_finos.main.controller;

import com.queijos_finos.main.dto.PropriedadeDTO;
import com.queijos_finos.main.model.*;
import com.queijos_finos.main.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MobileDTOsController {

    private final PropriedadeRepository propriedadeRepo;
    private final CursosRepository cursoRepo;
    private final TecnologiaRepository tecnologiaRepo;
    private final FornecedorRepository fornecedorRepo;
    private final AmostraRepository amostraRepo;

    public MobileDTOsController(
            PropriedadeRepository propriedadeRepo,
            CursosRepository cursoRepo,
            TecnologiaRepository tecnologiaRepo,
            FornecedorRepository fornecedorRepo,
            AmostraRepository amostraRepo) {
        this.propriedadeRepo = propriedadeRepo;
        this.cursoRepo = cursoRepo;
        this.tecnologiaRepo = tecnologiaRepo;
        this.fornecedorRepo = fornecedorRepo;
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

}

