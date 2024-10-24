package com.example.springthymeleaf.reposiories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.contrato.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    
        List<Contrato> findByDataFimBefore(LocalDate dataLimite);

}
