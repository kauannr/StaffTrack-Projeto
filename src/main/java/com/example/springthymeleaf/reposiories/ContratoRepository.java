package com.example.springthymeleaf.reposiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.contrato.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    
}
