package com.example.springthymeleaf.model.caixa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.vendedor.Vendas;

import java.util.*;

@Entity
public class OperadorCaixa extends Pessoa {

    @OneToMany(mappedBy = "operadorCaixa")
    private List<Vendas> listaVendas = new ArrayList<>();

    @OneToMany(mappedBy = "operador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperacaoDiaria> listaOperacoesDiarias = new ArrayList<>();  // Relacionamento com OperacaoDiaria


    public OperadorCaixa(){

    }

    public String registrarVenda(Vendas venda){
        listaVendas.add(venda);
        return null;
    }

    public List<Vendas> getListaVendas() {
        return listaVendas;
    }
}
