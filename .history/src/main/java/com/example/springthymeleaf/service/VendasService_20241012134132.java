package com.example.springthymeleaf.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.vendedor.Vendas;
import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.example.springthymeleaf.reposiories.VendasRepository;

@Service
public class VendasService {

    @Autowired
    VendasRepository vendasRepository;

    public Optional<Vendas> findById(long id) {
        return vendasRepository.findById(id);
    }

    public List<Vendas> findByValorVendaAndVendedor(Double valor) {
        return vendasRepository.findByValorVendaAndVendedor(valor);
    }

    public Page<Vendas> findByValorVendaAndVendedor(Double valorVenda, Vendedor vendedor, Pageable pageable) {
        return vendasRepository.findByValorVendaAndVendedor(valorVenda, vendedor, pageable);
    }

    public Page<Vendas> vendasPage(Vendedor vendedor, int page, int size, String sortedBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));

        if (vendedor == null) {
            return Page.empty(pageable);
        }

        return vendasRepository.findByVendedor(vendedor, pageable);
    }

    public Page<Vendas> findAll(Pageable pageable) {
        return vendasRepository.findAll(pageable);
    }

    public Page<Vendas> vendasNoPeriodo(Vendedor vendedor, Date de, Date ate, Pageable pageable) {

        return vendasRepository.findAllByVendedorAndDataVendaBetween(vendedor, de, ate, pageable);
    }

}
