package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.Telefone;
import com.example.springthymeleaf.reposiories.TelefoneRepository;

@Service
public class TelefoneService {

    @Autowired
    private TelefoneRepository telefoneRepository;

    public Telefone save(Telefone telefone) {

        return telefoneRepository.save(telefone);
    }

    public void delete(Telefone telefone) {
        telefoneRepository.delete(telefone);
    }

    public Optional<Telefone> findById(long id) {

        return telefoneRepository.findById(id);
    }

}
