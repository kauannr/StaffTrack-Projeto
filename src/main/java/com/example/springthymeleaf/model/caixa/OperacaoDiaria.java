package com.example.springthymeleaf.model.caixa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.*;

@Entity
public class OperacaoDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAberturaCaixa;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFechamentoCaixa;

    private Double faturamentoDiario;
    
    @ManyToOne
    @JoinColumn(name = "operador_id", nullable = false)
    private OperadorCaixa operador; // Referência ao operador

    public OperacaoDiaria(OperadorCaixa operador, Date dataAberturaCaixa, Date dataFechamentoCaixa,
            Double faturamentoDiario) {
        this.operador = operador;
        this.dataAberturaCaixa = dataAberturaCaixa;
        this.dataFechamentoCaixa = dataFechamentoCaixa;
        this.faturamentoDiario = faturamentoDiario;
    }

    public OperacaoDiaria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperadorCaixa getOperador() {
        return operador;
    }

    public void setOperador(OperadorCaixa operador) {
        this.operador = operador;
    }

    public Date getDataAberturaCaixa() {
        return dataAberturaCaixa;
    }

    public void setDataAberturaCaixa(Date dataAberturaCaixa) {
        this.dataAberturaCaixa = dataAberturaCaixa;
    }

    public Date getDataFechamentoCaixa() {
        return dataFechamentoCaixa;
    }

    public void setDataFechamentoCaixa(Date dataFechamentoCaixa) {
        this.dataFechamentoCaixa = dataFechamentoCaixa;
    }

    public Double getFaturamentoDiario() {
        return faturamentoDiario;
    }

    public void setFaturamentoDiario(Double faturamentoDiario) {
        this.faturamentoDiario = faturamentoDiario;
    }

}
