package com.example.springthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.reposiories.MotoristaRepository;

@Service
public class MotoristaService {
    
    @Autowired
    MotoristaRepository motoristaRepository;

    public Page<Motorista> findAllPage(Pagea) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllPage'");
    }
}
