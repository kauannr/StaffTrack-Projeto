package com.example.springthymeleaf.model.vendedor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.springthymeleaf.model.caixa.OperadorCaixa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Vendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Preencha a data da venda")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataVenda;

    @NotNull(message = "Preencha o valor da venda")
    private Double valorVenda;

    @NotNull(message = "Preencha a descrição")
    @NotEmpty(message = "Preencha a descrição")
    private String descricaoProduto;

    @NotNull(message = "Preencha o valor do produto")
    private Double valorProduto;

    @NotNull(message = "Preencha a quantidade do produto")
    private Integer quantidadeProduto;

    @NotNull(message = "Preencha a comissão")
    private Double comissaoVenda;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @ManyToOne
    private OperadorCaixa operadorCaixa;

    public Vendas(Date dataVenda, Double valorVenda, String descricaoProduto, Double valorProduto,
            Integer quantidadeProduto, Double comissaoVenda, Vendedor vendedor) {
        this.dataVenda = dataVenda;
        this.valorVenda = valorVenda;
        this.descricaoProduto = descricaoProduto;
        this.valorProduto = valorProduto;
        this.quantidadeProduto = quantidadeProduto;
        this.comissaoVenda = comissaoVenda;
        this.vendedor = vendedor;
    }

    public Vendas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    public Integer getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(Integer quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Double getComissaoVenda() {
        return comissaoVenda;
    }

    public void setComissaoVenda(Double comissaoVenda) {
        this.comissaoVenda = comissaoVenda;
    }

}
