package com.example.springthymeleaf.controllers;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "**/motorista", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid Motorista motorista, BindingResult bindingResult, ModelAndView modelAndView) {

        if (!Pessoa.validarCPF(motorista.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objMotorista", motorista);
            modelAndView.addObject("listaPessoasFront",
                    motoristaService.findAllPage(PageRequest.of(0, 2, Sort.by("id"))));
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("objMotorista", motorista);
            modelAndView.addObject("listaPessoasFront",
                    motoristaService.findAllPage(PageRequest.of(0, 2, Sort.by("id"))));

            List<String> listaMensagensErro = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                listaMensagensErro.add(objectError.getDefaultMessage()); // Mensagem que vem do @NotNull
            }
            modelAndView.addObject("msgPraIterar", listaMensagensErro);

            return modelAndView;
        }

        // SALVAMENTO:
        String msgRetornadaPraTela = motorista.getId() == null ? "Motorista salvo com sucesso!"
                : "Motorista atualizado com sucesso!";

        motoristaService.save(motorista);

        modelAndView.addObject("msgPraIterar", msgRetornadaPraTela);
        modelAndView.addObject("listaPessoasFront",
                motoristaService.findAllPage(PageRequest.of(0, 2, Sort.by("id"))));
        modelAndView.addObject("objMotorista", motorista.ge);

        return modelAndView;
    }
}
