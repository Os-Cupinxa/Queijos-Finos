package com.queijos_finos.main;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.queijos_finos.main.model.Usuarios;
import com.queijos_finos.main.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public Usuarios cadastrarAlterarUsuarioHash(Long id, String nome, String email, String senha) {
        BCryptPasswordEncoder hashGenerator = new BCryptPasswordEncoder();
        Usuarios usuario = new Usuarios();

        if (id != -1) {
            return usuarioRepo.findById(id)
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
            return usuarioRepo.save(usuario);
        }
    }

    public Usuarios alterarSenhaUsuario(Long id, String novaSenha) {
        BCryptPasswordEncoder hashGenerator = new BCryptPasswordEncoder();
        return usuarioRepo.findById(id)
                .map(usuarios -> {
                    usuarios.setSenha(hashGenerator.encode(novaSenha));
                    return usuarioRepo.save(usuarios);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    public Long deleteUsuario(long id) {
        usuarioRepo.deleteById(id);
        return id;
    }
}
