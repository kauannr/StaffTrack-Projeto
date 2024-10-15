package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.contrato.Beneficio;
import com.example.springthymeleaf.reposiories.BeneficioRepository;

@Service
public class BeneficioService {

    @Autowired
    private BeneficioRepository beneficioRepository;

    public Beneficio save(Beneficio beneficio) {
        
        return beneficioRepository.save(beneficio);
    }

    public Optional<Beneficio> findById(long id) {
        return beneficioRepository.findById(id);
    }
}
