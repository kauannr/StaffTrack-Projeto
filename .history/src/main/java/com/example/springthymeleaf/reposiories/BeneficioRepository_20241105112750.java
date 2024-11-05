package com.example.springthymeleaf.reposiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.contrato.Beneficio;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {

}
