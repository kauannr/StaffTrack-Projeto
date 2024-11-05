package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.reposiories.MotoristaRepository;

@Service
public class MotoristaService {

    @Autowired
    MotoristaRepository motoristaRepository;

    public Motorista save(Motorista motorista) {
        return motoristaRepository.save(motorista);
    }

    public void deleteById(long id) {
        motoristaRepository.deleteById(id);
    }

    public Optional<Motorista> findById(long id) {
        return motoristaRepository.findById(id);
    }

    public Page<Motorista> findAllPage(Pageable pageable) {
        return motoristaRepository.findAll(pageable);
    }

    public Page<Motorista> findByNamePage(String nome, int page, int size, String SortedBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortedBy));
        return motoristaRepository.findByNome(nome, pageable);
    }

    public Page<Motorista> findBySobrenomePage(String sobrenome, int page, int size, String SortedBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortedBy));
        return motoristaRepository.findBySobrenome(sobrenome, pageable);
    }

    public Page<Motorista> findByIdPage(long id, int page, int size, String SortedBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortedBy));
        return motoristaRepository.findById(id, pageable);
    }

    public Page<Motorista> findAllPage(int page, int size, String sortedBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));
        return motoristaRepository.findAll(pageable);
    }

}
