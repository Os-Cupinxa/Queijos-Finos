package com.queijos_finos.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.queijos_finos.main.repository.AmostraRepository;
import com.queijos_finos.main.repository.PropriedadeRepository;

@Controller
@RequestMapping("/amostras")
public class AmostraController {

    private final AmostraRepository amostraRepository;

    private final PropriedadeRepository propriedadeRepository;


    public AmostraController(AmostraRepository amostraRepository, PropriedadeRepository propriedadeRepository) {
        this.amostraRepository = amostraRepository;
        this.propriedadeRepository = propriedadeRepository;
    }
}
