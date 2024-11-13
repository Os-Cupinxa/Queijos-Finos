package com.queijos_finos.main.controller;

import com.queijos_finos.main.dto.LogInDTO;
import com.queijos_finos.main.dto.SingUpDTO;
import com.queijos_finos.main.model.JwtToken;
import com.queijos_finos.main.repository.PropriedadeRepository;
import com.queijos_finos.main.repository.TecnologiaRepository;
import com.queijos_finos.main.utils.JwtUtils;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.queijos_finos.main.dto.TecnologiaCountProp;
import com.queijos_finos.main.model.Propriedade;
import com.queijos_finos.main.model.Usuarios;
import com.queijos_finos.main.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.queijos_finos.main.utils.PasswordUtils.encryptPassword;
import static com.queijos_finos.main.utils.PasswordUtils.verifyPassword;

@Controller
public class UsuarioController {

    private static final String MENSAGEM = "mensagem";
    private static final String REDIRECT_USUARIOS = "redirect:/usuarios";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";

    private final UsuarioRepository usuarioRepo;
    private final PropriedadeRepository propRepo;
    private final JwtUtils jwtUtils;
    private final TecnologiaRepository tecnologiaRepository;

    public UsuarioController(
            UsuarioRepository usuarioRepo,
            PropriedadeRepository propRepo,
            JwtUtils jwtUtils,
            TecnologiaRepository tecnologiaRepository) {
        this.usuarioRepo = usuarioRepo;
        this.propRepo = propRepo;
        this.jwtUtils = jwtUtils;
        this.tecnologiaRepository = tecnologiaRepository;
    }

    @GetMapping("/usuarios")
    public String showUsuarios(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Usuarios> usuariosPage;

        if (query != null && !query.isEmpty()) {
            Usuarios usuarioExample = new Usuarios();
            usuarioExample.setNome(query);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Example<Usuarios> example = Example.of(usuarioExample, matcher);
            usuariosPage = usuarioRepo.findAll(example, pageable);
        } else {
            usuariosPage = usuarioRepo.findAll(pageable);
        }

        model.addAttribute("usuarios", usuariosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());
        model.addAttribute("totalItems", usuariosPage.getTotalElements());
        model.addAttribute("query", query);

        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String createUsuario(
            @RequestParam("id") Long id,
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

        model.addAttribute(MENSAGEM, "Usuário salvo com sucesso");
        return REDIRECT_USUARIOS;
    }

    @PostMapping("/usuarios/alterarSenha")
    public String alterarSenhaUsuario(
            @RequestParam("id") Long id,
            @RequestParam("novaSenha") String novaSenha,
            Model model) {
        BCryptPasswordEncoder hashGenerator = new BCryptPasswordEncoder();
        usuarioRepo.findById(id)
                .map(usuarios -> {
                    usuarios.setSenha(hashGenerator.encode(novaSenha));
                    return usuarioRepo.save(usuarios);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        model.addAttribute(MENSAGEM, "Usuário salvo com sucesso");
        return REDIRECT_USUARIOS;
    }

    @PostMapping("/usuarios/delete/{id}")
    public String deleteUsuario(
            @PathVariable("id") Long id,
            Model model) {

        usuarioRepo.deleteById(id);
        return REDIRECT_USUARIOS;
    }

    @GetMapping("/usuarios/cadastrar")
    public String createUsuarioView(
            @RequestParam(required = false) Long idUsuarios,
            Model model) {

        if (idUsuarios != null) {
            Optional<Usuarios> usuarioOptional = usuarioRepo.findById(idUsuarios);
            usuarioOptional.ifPresent(usuarios -> model.addAttribute("usuario", usuarios));
        }

        return "subPages/usuariosCadastrar";
    }

    @GetMapping("/")
    public ModelAndView login() {
        ModelAndView modelview = new ModelAndView();
        modelview.setViewName("login");
        return modelview;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(@RequestBody SingUpDTO user) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (findByEmail(user.getEmail()).isPresent()) {
                response.put(STATUS, ERROR);
                response.put(MESSAGE, "Email já cadastrado");
                return ResponseEntity.badRequest().body(response);
            }

            Usuarios newUser = new Usuarios();
            newUser.setNome(user.getNome());
            newUser.setEmail(user.getEmail());
            newUser.setSenha(encryptPassword(user.getSenha()));
            Usuarios createdUser = usuarioRepo.save(newUser);

            return ResponseEntity.ok().body(createdUser);
        } catch (Exception e) {
            response.put(STATUS, ERROR);
            response.put(MESSAGE, "Erro interno");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInDTO user) {

        Map<String, Object> response = new HashMap<>();
        Usuarios usu = usuarioRepo.findByEmail(user.getEmail());

        if (usu == null) {
            response.put(STATUS, ERROR);
            response.put(MESSAGE, "Usuário não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (verifyPassword(user.getSenha(), usu.getSenha())) {
            response.put(STATUS, SUCCESS);
            response.put(MESSAGE, "Login bem-sucedido");
            response.put("userId", usu.getIdUsuario());
            return ResponseEntity.ok().body(jwtUtils.generateToken(usu, 3600000));
        } else {
            response.put(STATUS, ERROR);
            response.put(MESSAGE, "Credenciais inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/dashboard")
    public String webLogin(
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            Model model) {

        System.out.println(email);
        System.out.println(senha);

        if (email.isBlank() || senha.isBlank()) {
            model.addAttribute("mensagem", "Por favor informe o e-mail e a senha");
            return "login";
        }

        Optional<Usuarios> existentUser = findByEmail(email);

        if (existentUser.isEmpty()) {
            model.addAttribute("mensagem", "E-mail ou senha incorretos");
            return "login";
        }

        if (verifyPassword(senha, existentUser.get().getSenha())) {
            JwtToken token = jwtUtils.generateToken(existentUser.get(), 3600000);
            model.addAttribute("usu", existentUser.get());
            model.addAttribute("token", token.getToken());
            return "dashboard";
        } else {
            model.addAttribute("mensagem", "E-mail ou senha incorretos");
            return "login";
        }
    }

    public Optional<Usuarios> findByEmail(String email) {
        Usuarios exampleUser = new Usuarios();
        exampleUser.setEmail(email);

        Example<Usuarios> example = Example.of(exampleUser);
        return usuarioRepo.findOne(example);
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
