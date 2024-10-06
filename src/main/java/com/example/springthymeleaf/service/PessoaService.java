package com.example.springthymeleaf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.reposiories.PessoaRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Page<Pessoa> realizarPesquisa(String nome, Pageable pageable, ModelAndView modelAndView) {
        Page<Pessoa> pagePessoas = null;

        // Se mandar o sexo na pesquisa, chama o consultaPorSexo

        if (nome.trim().equalsIgnoreCase("MASCULINO") || nome.trim().equalsIgnoreCase("FEMININO")) {
            pagePessoas = pessoaRepository.consultarPorSexo(nome, pageable);
        } else {
            // se mandar apenas o nome
            pagePessoas = pessoaRepository.consultarPornNome(nome.trim(), pageable);
        }

        return pagePessoas;
    }

    public Pessoa save(Pessoa pessoa) {

        return pessoaRepository.save(pessoa);

    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public void delete(Pessoa pessoa) {
        pessoaRepository.delete(pessoa);
    }

    public Optional<Pessoa> findById(long id) {
        return pessoaRepository.findById(id);
    }

    public Page<Pessoa> findAllPage(int page, int size, String sortedBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));

        return pessoaRepository.findAll(pageable);

    }

    public Page<Pessoa> findAllPage(Pageable pageable) {
        return pessoaRepository.findAll(pageable);

    }

}
