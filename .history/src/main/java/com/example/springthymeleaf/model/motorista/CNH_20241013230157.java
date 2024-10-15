package com.example.springthymeleaf.model.motorista;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class CNH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Preencha o número  da carteira")
    @NotEmpty(message = "Preencha o número  da carteira")
    private String numeroCNH;

    @NotNull(message = "Preencha a categoria da carteira")
    @NotEmpty(message = "Preencha a categoria da carteira")
    private String categoriaCNH;

    @NotNull(message = "Preencha a data de vencimento da carteira")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataVencimento;

    @OneToOne(mappedBy = "cnh", orphanRemoval = true)
    @NotNull(message = "Preencha o motorista")
    private Motorista motorista;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumeroCNH() {
        return numeroCNH;
    }

    public void setNumeroCNH(String numeroCNH) {
        this.numeroCNH = numeroCNH;
    }

    public String getCategoriaCNH() {
        return categoriaCNH;
    }

    public void setCategoriaCNH(String categoriaCNH) {
        this.categoriaCNH = categoriaCNH;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }
}
