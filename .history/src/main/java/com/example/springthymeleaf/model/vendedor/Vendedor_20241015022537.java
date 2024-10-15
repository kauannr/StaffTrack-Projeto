package com.example.springthymeleaf.model.vendedor;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.stream.Collectors;

import com.example.springthymeleaf.model.Pessoa;

import java.util.Date;

@Entity
public class Vendedor extends Pessoa {

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<Vendas> vendas;

    @NotNull(message = "Informe o valor da meta de vendas")
    @Min(value = 100, message = "Minimo de meta: 100")
    private Double metaVendas;

    @ManyToOne
    @NotNull(message = "Selecione o gerente do vendedor")
    private GerenteVendas gerente;

    public Vendedor(GerenteVendas gerente) {
        this.gerente = gerente;
    }

    public Vendedor() {

    }

    public void adicionarVenda(Vendas venda){
        this.vendas.add(venda);
    }

    public List<Vendas> vendasNoPeriodo(Date de, Date ate) {

        if (this.vendas == null || this.vendas.isEmpty()) {
            return null;
        }

        // Filtrar as vendas dentro do período e contar
        return this.vendas.stream()
        .filter(venda -> !venda.getDataVenda().before(de) && !venda.getDataVenda().after(ate))
        .collect(Collectors.toList());
    }

    public Double calcularComissoes(Date de, Date ate){
        List<Vendas> listaDeVendas = vendasNoPeriodo(de, ate);

        Double comissao = 0d;

        for (Vendas venda : listaDeVendas) {
            comissao += ((venda.getComissaoVenda() / 100) * venda.getValorVenda());
        }
        return comissao;
    }

    public List<Vendas> getVendas() {
        return vendas;
    }

    public GerenteVendas getGerente() {
        return gerente;
    }

    public void setGerente(GerenteVendas gerente) {
        this.gerente = gerente;
    }

    public void setVendas(List<Vendas> vendas) {
        this.vendas = vendas;
    }

    public Double getMetaVendas() {
        return metaVendas;
    }

    public void setMetaVendas(Double metaVendas) {
        this.metaVendas = metaVendas;
    }

}
