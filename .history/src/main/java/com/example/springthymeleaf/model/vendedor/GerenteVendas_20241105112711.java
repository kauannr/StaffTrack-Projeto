package com.example.springthymeleaf.model.vendedor;

import java.util.ArrayList;
import java.util.List;

import com.example.springthymeleaf.model.Pessoa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class GerenteVendas extends Pessoa {

    @NotNull(message = "Preencha a meta de vendas mensal")
    @Min(value = 500, message = "Meta mínima: 500")
    private Double metaVendas;

    @OneToMany(mappedBy = "gerente", cascade = CascadeType.ALL)
    private List<Vendedor> listaVendedores = new ArrayList<>();

    public Double getMetaVendas() {
        return metaVendas;
    }

    public void setMetaVendas(Double metaVendas) {
        this.metaVendas = metaVendas;
    }

    public List<Vendedor> getListaVendedores() {
        return listaVendedores;
    }

    public void setListaVendedores(List<Vendedor> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    @Override
    public String toString() {
        return "Gerente de vendas";
    }
}
