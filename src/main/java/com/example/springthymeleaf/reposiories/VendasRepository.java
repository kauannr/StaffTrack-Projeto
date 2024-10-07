package com.example.springthymeleaf.reposiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.vendedor.Vendas;

@Repository
public interface VendasRepository extends JpaRepository<Vendas, Long> {

}
