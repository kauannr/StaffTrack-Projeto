package com.example.springthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.motorista.CNH;
import com.example.springthymeleaf.reposiories.CNHRepository;

@Service
public class CNHService {
    
    @Autowired
    private CNHRepository cnhRepository;

    public CNH save(CNH cnh){
        return cnhRepository.save(cnh);
    }
}
