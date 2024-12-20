package com.example.springthymeleaf.model.contrato;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.contrato.validacao.ValidDatasContrato;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@ValidDatasContrato
public class Contrato implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Preencha o número do contrato")
    @NotEmpty(message = "Preencha o número do contrato")
    private String numeroContrato;

    @NotNull(message = "Preencha o tipo do contrato")
    @NotEmpty(message = "Preencha o tipo do contrato")
    private String tipo;

    @NotNull(message = "Preencha o valor do contrato")
    @NotEmpty(message = "Preencha o valor do contrato")
    private String valor;
    
    @NotNull(message = "Data de inicio do contrato é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate dataFim;

    @OneToOne(mappedBy = "contrato")
    private Pessoa pessoa;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<Beneficio> beneficios = new ArrayList<>();

    public Contrato(String numeroContrato, String tipo, String valor, LocalDate dataInicio, LocalDate dataFim, Pessoa pessoa) {
        this.numeroContrato = numeroContrato;
        this.tipo = tipo;
        this.valor = valor;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.pessoa = pessoa;
    }

    public Contrato() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Beneficio> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(List<Beneficio> beneficios) {
        this.beneficios = beneficios;
    }

    public void adicionarBeneficio(Beneficio beneficio){
        beneficio.setContrato(this);
        this.beneficios.add(beneficio);
    }

    public void removerBeneficio(Beneficio beneficio){
        this.beneficios.remove(beneficio);
    }

    


}
