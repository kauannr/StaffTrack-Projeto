package com.example.springthymeleaf.reposiories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.vendedor.GerenteVendas;

@Repository
public interface GerenteVendasRepository extends JpaRepository<GerenteVendas, Long> {
    public Page<GerenteVendas> findByNome(String nome, Pageable pageable);

    public Page<GerenteVendas> findBySobrenome(String sobrenome, Pageable pageable);

    public Page<GerenteVendas> findById(Long id, Pageable pageable);

}
