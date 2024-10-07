package com.example.springthymeleaf.reposiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.caixa.OperadorCaixa;

@Repository
public interface OperadorCaixaRepository extends JpaRepository<OperadorCaixa, Long>{

} 