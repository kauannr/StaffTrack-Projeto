package com.example.springthymeleaf.model.enums;

public enum Cargo {
    
    VENDEDOR("Vendedor"),
    GEREN_VENDAS("Gerente de vendas"),
    CAIXA("Caixa"),
    GEREN_LOJA("Gerende de Loja"),
    ESTOQUISTA("Estoquista"),
    MONTADOR_MOVEIS("Montador de móveis"),
    TECNNICO_ELETRO("Técnico em Eletrodomésticos"),
    ATENDENTE_SAC("Atentende de SAC"),
    MOTORISTA("Motorista"),
    AJUDANTE_ENTREGA("Ajudante de entrega");
    
    private String nome;
    
    
    public String getNome() {
        return nome;
    }
    
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    Cargo(String nome) {
        this.nome = nome;
    }

}
