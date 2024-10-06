package com.example.springthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.Usuario;
import com.example.springthymeleaf.reposiories.UsuarioRepository;

@Service
public class ImplemUserDetailsService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUserByLogin(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não foi encontrado");
        }

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(),
                true, true, true, usuario.getAuthorities());
    }

}
