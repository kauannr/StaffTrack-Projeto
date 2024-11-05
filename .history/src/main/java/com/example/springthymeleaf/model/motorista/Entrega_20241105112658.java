package com.example.springthymeleaf.model.motorista;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Entrega implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Preencha o endereço de origem")
    @NotEmpty(message = "Preencha o endereço de origem")
    private String enderecoOrigem;

    @NotNull(message = "Preencha o endereço de destino")
    @NotEmpty(message = "Preencha o endereço de destino")
    private String enderecoDestino;

    @NotNull(message = "Preencha a descrição da entrega")
    @NotEmpty(message = "Preencha a descrição da entrega")
    private String descricaoEntrega;

    @NotNull(message = "Preencha a data da entrega")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEntrega;

    @NotNull(message = "Preencha o status da entrega")
    @NotEmpty(message = "Preencha o status da entrega")
    private String statusEntrega;

    @ManyToOne
    private Motorista motorista;

    public Entrega(String enderecoOrigem, String enderecoDestino, String descricaoEntrega, String statusEntrega,
            LocalDate dataEntrega) {
        this.enderecoOrigem = enderecoOrigem;
        this.enderecoDestino = enderecoDestino;
        this.descricaoEntrega = descricaoEntrega;
        this.dataEntrega = dataEntrega;
        this.statusEntrega = statusEntrega;
    }

    public Entrega() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnderecoOrigem() {
        return enderecoOrigem;
    }

    public void setEnderecoOrigem(String enderecoOrigem) {
        this.enderecoOrigem = enderecoOrigem;
    }

    public String getEnderecoDestino() {
        return enderecoDestino;
    }

    public void setEnderecoDestino(String enderecoDestino) {
        this.enderecoDestino = enderecoDestino;
    }

    public String getDescricaoEntrega() {
        return descricaoEntrega;
    }

    public void setDescricaoEntrega(String descricaoEntrega) {
        this.descricaoEntrega = descricaoEntrega;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }
}
