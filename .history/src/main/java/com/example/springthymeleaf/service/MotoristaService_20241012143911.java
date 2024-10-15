package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.reposiories.MotoristaRepository;

@Service
public class MotoristaService {
    
    @Autowired
    MotoristaRepository motoristaRepository;

    public Motorista save(Motorista motorista){
        return motoristaRepository.save(motorista);
    }

    public Optional<Motorista> findById(long id){
        return motoristaRepository.findById(null)
    }

    public Page<Motorista> findAllPage(Pageable pageable) {
        return motoristaRepository.findAll(pageable);
    }
}
