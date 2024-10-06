package com.example.springthymeleaf;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.Usuario;
import com.example.springthymeleaf.reposiories.UsuarioRepository;

@SpringBootTest
class SpringthymeleafApplicationTests {
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	Usuario criptografarSenhaESalvar(String username, String password) {

		if (usuarioRepository.findUserByLogin(username) != null) {
			throw new IllegalArgumentException("Usuário já existe");
		}

		Usuario usuario = new Usuario(username, password);

		usuario.addRole("ROLE_USER");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String resultado = encoder.encode(usuario.getPassword());
		System.out.println(resultado);

		usuario.setPassword(resultado);
		usuarioRepository.save(usuario);

		return usuario;
	}

	@Test
	void inserirUsuario() {
		criptografarSenhaESalvar("manager", "manager123");
		
	}

	@Test
	void deletarUsuarioExistente() {
		List<Usuario> usuarios = usuarioRepository.findByLoginRepetido("teste");
		for (Usuario usuario : usuarios) {
			usuario.getAuthorities().clear();
			usuarioRepository.delete(usuario);
		}
	}

	@Test
	void adicionarRole() {
		Usuario user = usuarioRepository.findUserByLogin("manager");
		if (user != null) {
			user.addRole("ROLE_MANAGER");
			usuarioRepository.save(user);
		}
	}

	@Test
	void setarRoles() {
		Usuario user = usuarioRepository.findUserByLogin("manager");
		if (user != null) {
			user.getAuthorities().clear();

			Set<String> roles = new HashSet<>();
			roles.add("ROLE_MANAGER");
			
			user.setRoles(roles);
			usuarioRepository.save(user);
		}
	}

	@Test
	void validarCPF(){

		Pessoa pessoa = new Pessoa();
		pessoa.setCpf("0");
		 boolean resultado = Pessoa.validarCPF(pessoa.getCpf());
        
        // Verifica se o resultado é false, já que o CPF é inválido
        assertFalse(resultado);
		

	}

}
