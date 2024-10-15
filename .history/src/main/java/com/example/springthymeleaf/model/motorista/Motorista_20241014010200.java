package com.example.springthymeleaf.model.motorista;

import com.example.springthymeleaf.model.Pessoa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Motorista extends Pessoa{

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval=)
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


    
}
