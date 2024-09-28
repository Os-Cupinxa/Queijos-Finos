package com.queijos_finos.main.controller;

import com.queijos_finos.main.model.Fornecedor;
import com.queijos_finos.main.repository.FornecedorRepository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class FornecedorController {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorController(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @GetMapping("/fornecedores")
    public String showFornecedores(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Fornecedor> fornecedores;

        if (query != null && !query.isEmpty()) {
            Fornecedor fornecedorExample = new Fornecedor();
            fornecedorExample.setNome(query);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Example<Fornecedor> example = Example.of(fornecedorExample, matcher);
            fornecedores = fornecedorRepository.findAll(example, pageable);
        } else {
            fornecedores = fornecedorRepository.findAll(pageable);
        }

        model.addAttribute("fornecedores", fornecedores);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fornecedores.getTotalPages());
        model.addAttribute("totalItems", fornecedores.getTotalElements());
        model.addAttribute("query", query);

        return "fornecedores";
    }

    @PostMapping("/fornecedores")
    public String createFornecedor(
            @RequestParam("id") Long id,
            @RequestParam("nome") String nome,
            @RequestParam("nicho") String nicho,
            @RequestParam("email") String email,
            @RequestParam("qualidade") Double qualidade,
            Model model) {

        if (id != -1) {
            fornecedorRepository.findById(id)
                    .map(fornecedor -> {
                        fornecedor.setNome(nome);
                        fornecedor.setNicho(nicho);
                        fornecedor.setEmail(email);
                        fornecedor.setQualidade(qualidade);
                        return fornecedorRepository.save(fornecedor);
                    })
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com o ID: " + id));
        } else {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome(nome);
            fornecedor.setNicho(nicho);
            fornecedor.setEmail(email);
            fornecedor.setQualidade(qualidade);
            fornecedorRepository.save(fornecedor);
        }

        model.addAttribute("mensagem", "Fornecedor salvo com sucesso");
        return "redirect:/fornecedores";
    }

    @Transactional
    @PostMapping("/fornecedor/delete/{id}")
    public String deleteFornecedor(
            @PathVariable("id") Long id,
            Model model) {

        fornecedorRepository.deleteFornecedorPropriedadeRelacionamento(id);
        fornecedorRepository.deleteById(id);

        return "redirect:/fornecedores";
    }


    @GetMapping("/fornecedores/cadastrar")
    public String createFornecedorView(
            @RequestParam(required = false) Long idFornecedor,
            Model model) {

        if (idFornecedor != null) {
            Optional<Fornecedor> fornecedor = fornecedorRepository.findById(idFornecedor);

            if (fornecedor.isPresent()) {
                model.addAttribute("fornecedor", fornecedor.get());
            } else {
                model.addAttribute("mensagem", "Fornecedor não encontrado");
                return "redirect:/fornecedores";
            }
        }

        return "subPages/fornecedoresCadastrar";
    }
}
