package com.example.springthymeleaf.reposiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.motorista.Motorista;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    
}
