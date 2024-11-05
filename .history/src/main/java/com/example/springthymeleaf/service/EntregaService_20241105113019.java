package com.example.springthymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.motorista.Entrega;
import com.example.springthymeleaf.reposiories.EntregaRepository;

@Service
public class EntregaService {

    @Autowired
    EntregaRepository entregaRepository;

    public Optional<Entrega> findById(Long id) {
        if (id == null) {
            Optional<Entrega> optionalVazio = null;
            return optionalVazio;
        }
        return entregaRepository.findById(id);
    }

    public boolean isAtualizado(Entrega entrega) {
        if (entrega.getDataEntrega() == null && entrega.getDescricaoEntrega() == null
                && entrega.getEnderecoDestino() == null && entrega.getEnderecoOrigem() == null
                && entrega.getMotorista() == null) {
            return true;
        }
        return false;
    }
}
