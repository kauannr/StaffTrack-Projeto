package com.example.springthymeleaf.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.example.springthymeleaf.service.MotoristaService;

import jakarta.validation.Valid;

@Controller
public class MotoristaController {

    @Autowired
    MotoristaService motoristaService;

    @RequestMapping(value = "**/inicialmotorista", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastromotorista.html");
        modelAndView.addObject("objMotorista", new Motorista());
        modelAndView.addObject("listaPessoasFront",
                motoristaService.findAllPage(PageRequest.of(page, size, Sort.by("id"))));

        return modelAndView;
    }

    @RequestMapping(value = "**/vendedor", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid Motorista motorista, BindingResult bindingResult, ModelAndView modelAndView) {

        if (!Pessoa.validarCPF(motorista.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objMotorista", motorista);
            modelAndView.addObject("listaPessoasFront",
                    motoristarService.findAllPage(0, 2, "id"));
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("objMotorista", motorista);
            modelAndView.addObject("listaPessoasFront",
                    motoristaService.findAllPage(0, 2, "id"));

            List<String> listaMensagensErro = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                listaMensagensErro.add(objectError.getDefaultMessage()); // Mensagem que vem do @NotNull
            }
            modelAndView.addObject("gerente", gerenteVendasService.findAll());
            modelAndView.addObject("msgPraIterar", listaMensagensErro);

            return modelAndView;
        }

        // SALVAMENTO:
        String msgRetornadaPraTela = vendedor.getId() == null ? "vendedor salvo com sucesso!"
                : "vendedor atualizado com sucesso!";

        vendedorService.save(vendedor);

        modelAndView.addObject("msgPraIterar", msgRetornadaPraTela);

        modelAndView.addObject("gerentes", gerenteVendasService.findAll());
        modelAndView.addObject("listaPessoasFront", vendedorService.findAllPage(0, 2, "id"));
        modelAndView.addObject("objVendedor", new Vendedor());

        return modelAndView;
    }
}
