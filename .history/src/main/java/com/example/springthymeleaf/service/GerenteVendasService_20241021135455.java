package com.example.springthymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.reposiories.GerenteVendasRepository;

@Service
public class GerenteVendasService {

    @Autowired
    private GerenteVendasRepository gerenteVendasRepository;

    public GerenteVendas save(GerenteVendas gerenteVendas) {
        return gerenteVendasRepository.save(gerenteVendas);
    }

    public Page<GerenteVendas> findAllPage(int page, int size, String sortedBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));

        return gerenteVendasRepository.findAll(pageable);

    }

    public List<GerenteVendas> findAll(){
        return gerenteVendasRepository.findAll();
    }

    public Optional<GerenteVendas> findById(long id){
        return gerenteVendasRepository.findById(id);
    }

    public void deleteById(long id){
        gerenteVendasRepository.deleteById(id);
    }

    public Page<GerenteVendas> findByNamePage(String nome, int page, int size, ){
        
    }
}
