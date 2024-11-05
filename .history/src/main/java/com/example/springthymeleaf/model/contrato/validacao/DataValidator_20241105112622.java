package com.example.springthymeleaf.model.contrato.validacao;

import java.time.LocalDate;

import com.example.springthymeleaf.model.contrato.Contrato;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DataValidator implements ConstraintValidator<ValidDatasContrato, Contrato> {

    @Override
    public boolean isValid(Contrato contrato, ConstraintValidatorContext context) {

        // Verifica se datas de início e fim são válidas
        if (contrato.getDataInicio() == null || contrato.getDataFim() == null) {
            return true;
        }

        // A data de fim deve ser depois da data de início
        if (!contrato.getDataFim().isAfter(contrato.getDataInicio())) {
            return false;
        }

        // Verifica se a pessoa associada tem data de nascimento válida
        if (contrato.getPessoa() != null && contrato.getPessoa().getDataNascimento() != null) {
            LocalDate dataNascimento = contrato.getPessoa().getDataNascimento();

            // A data de nascimento deve ser anterior à data de início e à data de fim do
            // contrato
            if (!dataNascimento.isBefore(contrato.getDataInicio()) || !dataNascimento.isBefore(contrato.getDataFim())) {
                return false;
            }
        }

        return true;
    }
}
