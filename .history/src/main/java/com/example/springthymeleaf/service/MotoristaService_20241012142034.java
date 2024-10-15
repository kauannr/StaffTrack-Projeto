package com.example.springthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.reposiories.MotoristaRepository;

@Service
public class MotoristaService {
    
    @Autowired
    MotoristaRepository motoristaRepository;

    public Paf findAllPage(int page, int size, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllPage'");
    }
}
