package com.example.springthymeleaf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.springthymeleaf.model.contrato.Contrato;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Preencha o nome")
    @NotEmpty(message = "Prencha o nome")
    private String nome;

    @NotNull(message = "Preencha o sobrenome")
    @NotEmpty(message = "Preencha o sobrenome")
    private String sobrenome;

    @NotNull(message = "Preencha o CPF")
    @NotEmpty(message = "Preencha o CPF")
    private String cpf;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Insira a data de nascimento do funcionário")
    private Date dataNascimento;

    //@NotNull(message = "Preencha o cargo do funcionário")
    //@NotEmpty(message = "Preencha o cargo do funcionário")
    //private String cargo;

    @Min(value = 1200, message = "Salario minimo: 1200")
    @NotNull(message = "Preencha o salário")
    private Double salario;

    @NotEmpty(message = "Preencha o cep")
    @NotNull(message = "Preencha o cep")
    private String cep;

    @NotEmpty(message = "Preencha a rua")
    @NotNull(message = "Preencha a rua")
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String ibge;
    private String sexo;

    @No
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private Contrato contrato;

    @OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Telefone> listaTelefones = new ArrayList<>();

    public Pessoa(String nome, String sobrenome) {
        this.nome = nome;
        this.sobrenome = sobrenome;
    }

    public Pessoa() {
    }

    public static boolean validarCPF(String cpf) {
        // Remover pontuações como ".", "-" e espaços
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (casos inválidos como "11111111111")
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula os dois dígitos verificadores
        int[] pesos = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int primeiroDigitoVerificador = calcularDigito(cpf.substring(0, 9), pesos);

        pesos = new int[] {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        int segundoDigitoVerificador = calcularDigito(cpf.substring(0, 10), pesos);

        // Verifica se os dois dígitos verificadores calculados são iguais aos do CPF informado
        return cpf.charAt(9) == Character.forDigit(primeiroDigitoVerificador, 10) &&
               cpf.charAt(10) == Character.forDigit(segundoDigitoVerificador, 10);
    }


    // auxiliar para CPF
    private static int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public List<Telefone> getListaTelefones() {
        return listaTelefones;
    }

    public void adicionarNaLista(Telefone telefone) {
        listaTelefones.add(telefone);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getbairro() {
        return bairro;
    }

    public void setbairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public void setListaTelefones(List<Telefone> listaTelefones) {
        this.listaTelefones = listaTelefones;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;

    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pessoa other = (Pessoa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
