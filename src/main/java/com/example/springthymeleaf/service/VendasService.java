package com.example.springthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.reposiories.VendasRepository;

@Service
public class VendasService {
    
    @Autowired
    VendasRepository vendasRepository;


}
