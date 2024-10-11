package com.example.springthymeleaf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.vendedor.Vendas;
import com.example.springthymeleaf.reposiories.VendasRepository;

@Service
public class VendasService {

    @Autowired
    VendasRepository vendasRepository;

    public Optional<Vendas> findById(long id) {
        return vendasRepository.findById(id);
    }

    public List<Vendas> findByValorVenda(Double valor) {
        return vendasRepository.findByValorVenda(valor);
    }

}
