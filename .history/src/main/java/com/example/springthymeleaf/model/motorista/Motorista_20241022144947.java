package com.example.springthymeleaf.model.motorista;

import java.util.ArrayList;
import java.util.List;

import com.example.springthymeleaf.model.Pessoa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Motorista extends Pessoa{

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull(message = "CNH:" + "\n")
    private CNH cnh;

    public Motorista() {
    }

    public CNH getCnh() {
        return cnh;
    }

    public void setCnh(CNH cnh) {
        this.cnh = cnh;
    }

    @OneToMany(mappedBy = "motorista", cascade = CascadeType.ALL)
    private List<Entrega> listaEntregas = new ArrayList<>();

    public List<Entrega> getListaEntregas() {
        return listaEntregas;
    }

    public void setListaEntregas(List<Entrega> listaEntregas) {
        this.listaEntregas = listaEntregas;
    }

    @Override
    public String toString() {
        return "Motorista";
    }


    
}
