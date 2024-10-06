package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.example.springthymeleaf.reposiories.VendedorRepository;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    public Vendedor save(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    public Page<Vendedor> findAllPage(int page, int size, String sortedBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));

        return vendedorRepository.findAll(pageable);
    }

    public Optional<Vendedor> findById(long id) {
        return vendedorRepository.findById(id);
    }

    public void deleteById(long id){
        vendedorRepository.deleteById(id);
    }

}
