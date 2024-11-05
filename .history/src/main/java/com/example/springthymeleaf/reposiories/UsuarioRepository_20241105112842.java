package com.example.springthymeleaf.reposiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.springthymeleaf.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.username = ?1")
    public Usuario findUserByLogin(String login);

    @Query("select u from Usuario u where u.username = ?1")
    public List<Usuario> findByLoginRepetido(String login);
}
