package com.queijos_finos.main.controller;

import com.queijos_finos.main.model.Curso;
import com.queijos_finos.main.repository.CursosRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cursos")
public class CursosController {

    private final CursosRepository cursosRepository;

    // Constantes para strings literais duplicadas
    private static final String CURSO_ATTR = "curso";
    private static final String MENSAGEM_ATTR = "mensagem";
    private static final String REDIRECT_CURSOS = "redirect:/cursos";
    private static final String CURSOS_CADASTRAR_VIEW = "subPages/cursosCadastrar";
    private static final String CURSOS_EDITAR_VIEW = "subPages/cursosEditar";

    public CursosController(CursosRepository cursosRepository) {
        this.cursosRepository = cursosRepository;
    }

    @GetMapping
    public String showCursos(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Curso> cursos;

        if (query != null && !query.isEmpty()) {
            Curso cursoExample = new Curso();
            cursoExample.setNome(query);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Example<Curso> example = Example.of(cursoExample, matcher);
            cursos = cursosRepository.findAll(example, pageable);
        } else {
            cursos = cursosRepository.findAll(pageable);
        }

        model.addAttribute("cursos", cursos.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cursos.getTotalPages());
        model.addAttribute("totalItems", cursos.getTotalElements());
        model.addAttribute("query", query);

        return "cursos";
    }

    @GetMapping("/cadastrar")
    public String createCursosView(Model model) {
        model.addAttribute(CURSO_ATTR, new Curso());
        return CURSOS_CADASTRAR_VIEW;
    }

    @PostMapping
    public String cadastrarCurso(@ModelAttribute Curso curso, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return CURSOS_CADASTRAR_VIEW;
        }

        cursosRepository.save(curso);
        return REDIRECT_CURSOS;
    }

    @GetMapping("/editar/{id}")
    public String editCursosView(@PathVariable Long id, Model model) {
        Optional<Curso> curso = cursosRepository.findById(id);

        if (curso.isPresent()) {
            model.addAttribute(CURSO_ATTR, curso.get());
            return CURSOS_EDITAR_VIEW;
        } else {
            model.addAttribute(MENSAGEM_ATTR, "Curso não encontrado");
            return REDIRECT_CURSOS;
        }
    }

    @PostMapping("/editar/{id}")
    public String editCursos(@PathVariable Long id, @ModelAttribute Curso curso, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(CURSO_ATTR, curso);
            return CURSOS_EDITAR_VIEW;
        }

        curso.setId(id);
        cursosRepository.save(curso);
        model.addAttribute(MENSAGEM_ATTR, "Curso atualizado com sucesso");

        return REDIRECT_CURSOS;
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteCursos(@PathVariable Long id, Model model) {
        cursosRepository.deleteCursoPropriedadeRelacionamento(id);
        cursosRepository.deleteById(id);
        model.addAttribute(MENSAGEM_ATTR, "Curso excluído com sucesso");

        return REDIRECT_CURSOS;
    }
}

