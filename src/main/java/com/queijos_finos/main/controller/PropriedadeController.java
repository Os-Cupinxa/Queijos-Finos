package com.queijos_finos.main.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.queijos_finos.main.model.*;
import com.queijos_finos.main.repository.*;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PropriedadeController {

    private static final String REDIRECT_PROPRIEDADE = "redirect:/propriedade";
    private static final String MENSAGEM = "mensagem";

    private final PropriedadeRepository propriedadeRepo;
    private final CursosRepository cursoRepo;
    private final TecnologiaRepository tecnologiaRepo;
    private final FornecedorRepository fornecedorRepo;
    private final AmostraRepository amostraRepo;

    public PropriedadeController(
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

    @GetMapping("/propriedade")
    public String showPropriedade(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Propriedade> propriedade;

        if (query != null && !query.isEmpty()) {
            Propriedade usuarioExample = new Propriedade();
            usuarioExample.setNomePropriedade(query);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("nomePropriedade", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Example<Propriedade> example = Example.of(usuarioExample, matcher);

            propriedade = propriedadeRepo.findAll(example, pageable);
        } else {
            propriedade = propriedadeRepo.findAll(pageable);
        }

        model.addAttribute("propriedades", propriedade.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", propriedade.getTotalPages());
        model.addAttribute("totalItems", propriedade.getTotalElements());
        model.addAttribute("query", query);

        return "propriedades";
    }

    @GetMapping("/propriedade/visualizar")
    public String detailsPropriedade(
            @RequestParam(required = false) Long idPropriedade,
            Model model) {

        Optional<Propriedade> propriedadeOptional = propriedadeRepo.findById(idPropriedade);
        Propriedade propriedade = propriedadeOptional.orElse(null);

        model.addAttribute("propriedade", propriedade);
        model.addAttribute("amostra", new Amostra());
        return "/subPages/propriedadeVisualizar";
    }

    @PostMapping("/amostras")
    public String createAmostra(
            @RequestParam Long propriedadeId,
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data,
            @RequestParam Double quantidadeleite,
            @RequestParam Double quantidadeQueijo,
            @RequestParam String observacao) {

        Propriedade propriedade = propriedadeRepo.findById(propriedadeId)
                .orElseThrow(() -> new IllegalArgumentException("Propriedade não encontrada"));

        Amostra amostra = new Amostra();
        amostra.setData(data);
        amostra.setQuantidadeleite(quantidadeleite);
        amostra.setQuantidadeQueijo(quantidadeQueijo);
        amostra.setObservacao(observacao);
        amostra.setPropriedade(propriedade);

        amostraRepo.save(amostra);

        return REDIRECT_PROPRIEDADE + "/visualizar?idPropriedade=" + propriedadeId;
    }

    @DeleteMapping("/amostra/delete/{id}")
    @ResponseBody
    public void deleteAmostra(@PathVariable("id") Long id) {
        amostraRepo.deleteById(id);
    }

    @GetMapping("/propriedade/cadastrar")
    public String createPropriedadeView(
            @RequestParam(required = false) Long idPropriedade,
            Model model) {

        if (idPropriedade != null) {
            Optional<Propriedade> propriedade = propriedadeRepo.findById(idPropriedade);

            if (propriedade.isPresent()) {
                model.addAttribute("propriedade", propriedade.get());
            } else {
                model.addAttribute(MENSAGEM, "Propriedade não encontrado");
                return REDIRECT_PROPRIEDADE;
            }
        }

        List<Curso> cursos = cursoRepo.findAll();
        List<Tecnologias> tecnologias = tecnologiaRepo.findAll();
        List<Fornecedor> fornecedores = fornecedorRepo.findAll();

        model.addAttribute("cursos", cursos);
        model.addAttribute("tecnologias", tecnologias);
        model.addAttribute("fornecedores", fornecedores);

        return "subPages/propriedadeCadastrar";
    }

    @PostMapping("/propriedade")
    public String createPropriedade(
            @RequestBody Propriedade propriedadeReq,
            Model model) {

        for (Tecnologias tecnologia : propriedadeReq.getTecnologias()) {
            List<Tecnologias> tecnologiaExistente = tecnologiaRepo.findByNome(tecnologia.getNome());

            if (tecnologiaExistente.isEmpty()) {
                tecnologia.setId(null);
                tecnologiaRepo.save(tecnologia);
                tecnologia.setId(tecnologiaRepo.findFirstByOrderByIdDesc().getId());
            }
        }

        if (propriedadeReq.getIdPropriedade() != -1) {
            propriedadeRepo.findById(propriedadeReq.getIdPropriedade())
                    .map(propriedade -> {
                        propriedade.setNomePropriedade(propriedadeReq.getNomePropriedade());
                        propriedade.setEmail(propriedadeReq.getEmail());
                        propriedade.setStatus(propriedadeReq.getStatus());
                        propriedade.setCPF(propriedadeReq.getCPF());
                        propriedade.setCNPJ(propriedadeReq.getCNPJ());
                        propriedade.setTelefone(propriedadeReq.getTelefone());
                        propriedade.setCelular(propriedadeReq.getCelular());
                        propriedade.setRua(propriedadeReq.getRua());
                        propriedade.setBairro(propriedadeReq.getBairro());
                        propriedade.setCidade(propriedadeReq.getCidade());
                        propriedade.setUF(propriedadeReq.getUF());
                        propriedade.setLatitude(propriedadeReq.getLatitude());
                        propriedade.setLongitude(propriedadeReq.getLongitude());
                        propriedade.setNomeProdutor(propriedadeReq.getNomeProdutor());
                        propriedade.setCursos(propriedadeReq.getCursos());
                        propriedade.setTecnologias(propriedadeReq.getTecnologias());
                        propriedade.setFornecedores(propriedadeReq.getFornecedores());
                        return propriedadeRepo.save(propriedade);
                    })
                    .orElseThrow(() -> new RuntimeException("Propriedade não encontrada com o ID: " +
                            propriedadeReq.getIdPropriedade()));
        } else {
            propriedadeReq.setIdPropriedade(null);
            propriedadeRepo.save(propriedadeReq);
        }

        model.addAttribute(MENSAGEM, "Propriedade salva com sucesso");
        return REDIRECT_PROPRIEDADE;
    }

    @PostMapping("/propriedade/delete/{id}")
    public String createContrato(
            @PathVariable("id") Long idPropriedade,
            Model model) {

        propriedadeRepo.deleteById(idPropriedade);
        model.addAttribute(MENSAGEM, "Propriedade deletada com sucesso");
        return REDIRECT_PROPRIEDADE;
    }
}
