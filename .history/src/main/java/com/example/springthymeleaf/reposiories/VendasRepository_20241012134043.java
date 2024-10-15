package com.example.springthymeleaf.reposiories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springthymeleaf.model.vendedor.Vendas;
import com.example.springthymeleaf.model.vendedor.Vendedor;


@Repository
public interface VendasRepository extends JpaRepository<Vendas, Long> {

    public List<Vendas> findByValorVendaAndVendedor(Double valor, Vendedor vendedor);

    public Page<Vendas> findByValorVendaAndVendedor(Double valor, Vendedor vendedor, Pageable pageable);

    public Page<Vendas> findByVendedor(Vendedor vendedor, Pageable pageable);

    Page<Vendas> findAllByVendedorAndDataVendaBetween(Vendedor vendedor, Date de, Date ate, Pageable pageable);


}
