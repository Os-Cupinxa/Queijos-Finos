package com.queijos_finos.main.controller;

import com.queijos_finos.main.repository.PropriedadeRepository;
import com.queijos_finos.main.repository.TecnologiaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.queijos_finos.main.dto.TecnologiaCountProp;
import com.queijos_finos.main.model.Propriedade;
import com.queijos_finos.main.model.Usuarios;
import com.queijos_finos.main.repository.UsuarioRepository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private PropriedadeRepository propRepo;
    @Autowired
    private TecnologiaRepository tecnologiaRepository;


    @GetMapping("/usuarios")
    public String showUsuarios(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Usuarios> usuarios;
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        usuarios = usuarioRepo.findAll(pageable).getContent();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }


    @PostMapping("/usuarios")
    public String createUsuario(@RequestParam("id") Long id,
                                @RequestParam("nome") String nome,
                                @RequestParam("email") String email,
                                @RequestParam(name = "senha", required = false) String senha,
                                Model model) {

        BCryptPasswordEncoder hashGenerator = new BCryptPasswordEncoder();
        Usuarios usuario = new Usuarios();

        if (id != -1) {
            usuarioRepo.findById(id)
                    .map(usuarios -> {
                        usuarios.setNome(nome);
                        usuarios.setEmail(email);
                        return usuarioRepo.save(usuarios);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        } else {
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(hashGenerator.encode(senha));
            usuarioRepo.save(usuario);
        }

        model.addAttribute("mensagem", "Usuário salvo com sucesso");
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/alterarSenha")
    public String alterarSenhaUsuario(@RequestParam("id") Long id,
                                      @RequestParam("novaSenha") String novaSenha,
                                      Model model) {
        BCryptPasswordEncoder hashGenerator = new BCryptPasswordEncoder();
        usuarioRepo.findById(id)
                .map(usuarios -> {
                    usuarios.setSenha(hashGenerator.encode(novaSenha));
                    return usuarioRepo.save(usuarios);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        model.addAttribute("mensagem", "Usuário salvo com sucesso");
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/delete/{id}")
    public String deleteUsuario(@PathVariable("id") Long id,
                                Model model) {

        usuarioRepo.deleteById(id);
        return "redirect:/usuarios";
    }


    @GetMapping("/usuarios/cadastrar")
    public String createUsuarioView(@RequestParam(required = false) Long idUsuarios, Model model) {
        if (idUsuarios != null) {
            Optional<Usuarios> usuarioOptional = usuarioRepo.findById(idUsuarios);
            usuarioOptional.ifPresent(usuarios -> model.addAttribute("usuario", usuarios));
        }

        return "usuariosCadastrar";
    }


    @GetMapping("/")
    public ModelAndView login() {
        ModelAndView modelview = new ModelAndView();
        modelview.setViewName("login");
        return modelview;
    }


    @PostMapping("/paginaInicial")
    public String login(@RequestParam("email") String email,
                        @RequestParam("senha") String senha,
                        Model model) {

        Usuarios usu = usuarioRepo.findByEmail(email);

        BCryptPasswordEncoder hashGenerator = new BCryptPasswordEncoder();

        if (hashGenerator.matches(senha, usu.getSenha())) {
            model.addAttribute("usu", usu);
            long type1Count = propRepo.countBystatus(2);
            long type2Count = propRepo.countBystatus(1);
            long type3Count = propRepo.countBystatus(0);
            Pageable pageable = PageRequest.of(0, 5); // First page with 5 items
        	List<Propriedade> top5Properties = propRepo.findTop5ByOrderByIdDesc(pageable).getContent();
        	
        	Page<Object[]> results = tecnologiaRepository.countTecnologiaPropriedadesNative(pageable);
            List<TecnologiaCountProp> tecnologiaCountProps = results.stream()
                .map(obj -> new TecnologiaCountProp((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
                
            model.addAttribute("type1Count", type1Count);
            model.addAttribute("type2Count", type2Count);
            model.addAttribute("type3Count", type3Count);
            model.addAttribute("propriedades", top5Properties);
            model.addAttribute("topTec", tecnologiaCountProps);
            System.out.println(usu.getNome());
            return "paginaInicial";
        } else {

            model.addAttribute("mensagem", "Credenciais invalidas");
            return "login";
        }
    }

    @GetMapping("/paginaInicial")
    public String paginaIncial(Model model) {

    	Pageable pageable = PageRequest.of(0, 5); // First page with 5 items
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
        return "paginaInicial";

    }

}
