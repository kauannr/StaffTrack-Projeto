package com.example.springthymeleaf.reposiories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.motorista.Motorista;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    
    public Page<Motorista> findByNome(String nome, Pageable pageable);
    public Page<Motorista> findBySobrenome(String sobrenome, Pageable pageable);
    public Page<Motorista> findById(long id, Pageable pageable);
}
