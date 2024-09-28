package com.queijos_finos.main.controller;

import com.queijos_finos.main.model.Contrato;
import com.queijos_finos.main.model.Propriedade;
import com.queijos_finos.main.repository.ContratoRepository;
import com.queijos_finos.main.repository.PropriedadeRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContratosController {

    private final ContratoRepository contratoRepo;
    private final PropriedadeRepository propriedadeRepo;

    public ContratosController(
            ContratoRepository contratoRepo,
            PropriedadeRepository propriedadeRepo) {
        this.contratoRepo = contratoRepo;
        this.propriedadeRepo = propriedadeRepo;
    }

    @GetMapping("/contratos")
    public String showContratos(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Contrato> contratos;

        if (query != null && !query.isEmpty()) {
            Contrato contratoExample = new Contrato();
            contratoExample.setNome(query);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Example<Contrato> example = Example.of(contratoExample, matcher);
            contratos = contratoRepo.findAll(example, pageable);
        } else {
            contratos = contratoRepo.findAll(pageable);
        }

        model.addAttribute("contratos", contratos.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contratos.getTotalPages());
        model.addAttribute("totalItems", contratos.getTotalElements());
        model.addAttribute("query", query);

        return "contratos";
    }

    @GetMapping("/contratos/cadastrar")
    public String createContratoView(
            @RequestParam(required = false) Long idContrato,
            Model model) {

        if (idContrato != null) {
            Optional<Contrato> contrato = contratoRepo.findById(idContrato);

            if (contrato.isPresent()) {
                model.addAttribute("contrato", contrato.get());
            } else {
                model.addAttribute("error", "Contrato not found");
            }
        }

        List<Propriedade> propriedades = propriedadeRepo.findWithoutContrato();

        model.addAttribute("propriedades", propriedades);
        return "subPages/contratosCadastrar";
    }

    @PostMapping("/contratos")
    public String createContrato(
            @RequestParam("id") Long id,
            @RequestParam("nome") String nome,
            @RequestParam("dataEmissao") String dataEmissao,
            @RequestParam("dataVencimento") String dataVencimento,
            @RequestParam("idPropriedade") Long idPropriedade,
            Model model)
            throws ParseException {

        Propriedade propriedade = new Propriedade();
        propriedade.setIdPropriedade(idPropriedade);

        SimpleDateFormat formato = new SimpleDateFormat("y-M-d");
        Date dataEmissaoConv = formato.parse(dataEmissao);
        Date dataVencimentoConv = formato.parse(dataVencimento);

        if (id != -1) {
            contratoRepo.findById(id).map(contrato -> {
                contrato.setNome(nome);
                contrato.setDataEmissao(dataEmissaoConv);
                contrato.setDataVercimento(dataVencimentoConv);
                contrato.setPropriedade(propriedade);
                return contratoRepo.save(contrato);
            }).orElseThrow(() -> new RuntimeException("Contrato não encontrado com o ID: " + id));
        } else {
            Contrato contrato = new Contrato(nome, dataEmissaoConv, dataVencimentoConv, propriedade);
            contratoRepo.save(contrato);
        }

        return "redirect:/contratos";
    }


    @PostMapping("/contratos/delete/{id}")
    public String deleteContrato(@PathVariable("id") Long id) {
        contratoRepo.deleteById(id);
        return "subPages/contratosCadastrar";
    }
}
