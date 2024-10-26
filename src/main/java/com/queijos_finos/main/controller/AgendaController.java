package com.queijos_finos.main.controller;

import com.queijos_finos.main.model.Agenda;
import com.queijos_finos.main.model.Usuarios;
import com.queijos_finos.main.repository.AgendaRepository;
import com.queijos_finos.main.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agenda")
public class AgendaController {

    private final AgendaRepository agendaRepository;
    private final UsuarioRepository usuarioRepo;

    public AgendaController(AgendaRepository agendaRepository, UsuarioRepository usuarioRepo) {
        this.agendaRepository = agendaRepository;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public String showAgenda(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Agenda> agenda;

        if (query != null && !query.isEmpty()) {
            Agenda agendaItemExample = new Agenda();
            agendaItemExample.setDescricao(query);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Example<Agenda> example = Example.of(agendaItemExample, matcher);
            agenda = agendaRepository.findAll(example, pageable);
        } else {
            agenda = agendaRepository.findAll(pageable);
        }

        model.addAttribute("agenda", agenda.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", agenda.getTotalPages());
        model.addAttribute("totalItems", agenda.getTotalElements());
        model.addAttribute("query", query);

        return "agenda";
    }

    @GetMapping("/cadastrar")
    public String createAgendaView(Model model) {
        List<Usuarios> usuarios = usuarioRepo.findAll();

        model.addAttribute("usuarios", usuarios);

        return "subPages/agendaCadastrar";
    }

    @PostMapping
    public String cadastrarAgendaItem(@ModelAttribute Agenda agendaItem, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "subPages/agendaCadastrar";
        }

        agendaRepository.save(agendaItem);
        return "redirect:/agenda";
    }

    @GetMapping("/editar/{id}")
    public String editAgendaView(@PathVariable Long id, Model model) {

        Optional<Agenda> agendaItem = agendaRepository.findById(id);
        List<Usuarios> usuarios = usuarioRepo.findAll();

        if (agendaItem.isPresent()) {
            model.addAttribute("agendaItem", agendaItem.get());
            model.addAttribute("usuarios", usuarios);
            return "subPages/agendaCadastrar";
        } else {
            model.addAttribute("mensagem", "AgendaItem não encontrado");
            return "redirect:/agenda";
        }
    }

    @PostMapping("/editar/{id}")
    public String editAgenda(@PathVariable Long id, @ModelAttribute Agenda agendaItem, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("agendaItem", agendaItem);
            return "subPages/agendaCadastrar";
        }

        agendaItem.setId(id);
        agendaRepository.save(agendaItem);
        model.addAttribute("mensagem", "AgendaItem atualizado com sucesso");

        return "redirect:/agenda";
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteAgenda(@PathVariable Long id, Model model) {
        agendaRepository.deleteById(id);
        model.addAttribute("mensagem", "Item da agenda excluído com sucesso");

        return "redirect:/agenda";
    }

}
