package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.contrato.Contrato;
import com.example.springthymeleaf.reposiories.ContratoRepository;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    public Contrato save(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public Optional<Contrato> findById(long id) {
        return contratoRepository.findById(id);
    }
}
