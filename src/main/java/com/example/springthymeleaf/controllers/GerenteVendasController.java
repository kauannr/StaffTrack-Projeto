package com.example.springthymeleaf.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.enums.Cargo;
import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.service.GerenteVendasService;

import jakarta.validation.Valid;

@Controller
public class GerenteVendasController {

    @Autowired
    private GerenteVendasService gerenteVendasService;

    @RequestMapping(value = "**/inicialgerente", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrogerentevendas.html");
        modelAndView.addObject("objGerenteVendas", new GerenteVendas());
        modelAndView.addObject("listaPessoasFront", gerenteVendasService.findAllPage(page, size, "id"));

        return modelAndView;
    }

    @RequestMapping(value = "**/gerentevendas", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid GerenteVendas gerenteVendas, BindingResult bindingResult) {

        // VALIDAÇÕES E ERROS:
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastrogerentevendas.html");

        if (!Pessoa.validarCPF(gerenteVendas.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objGerenteVendas", gerenteVendas);
            // modelAndView.addObject("listaPessoasFront",
            // gerenteVendasService.findAllPage(0, 2, "id"));
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {
            // VOLTAR PRA TELA COM OS DADOS DA PESSOA:
            modelAndView.addObject("objGerenteVendas", gerenteVendas);

            // PRA LISTA DE PESSOAS CONTINUAR NA TELA:
            // modelAndView.addObject("listaPessoasFront",
            // gerenteVendasService.findAllPage(0, 2, "id"));

            // modelAndView.addObject("cargos", Cargo.values());

            List<String> listaMensagensErro = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                listaMensagensErro.add(objectError.getDefaultMessage()); // Mensagem que vem do @NotNull
            }
            modelAndView.addObject("msgPraIterar", listaMensagensErro);

            return modelAndView;
        }

        // SALVAMENTO:
        String msgRetornadaPraTela = gerenteVendas.getId() == null ? "Gerente salvo com sucesso!"
                : "Gerente atualizado com sucesso!";

        gerenteVendasService.save(gerenteVendas);

        modelAndView.addObject("msgPraIterar", msgRetornadaPraTela);

        modelAndView.addObject("listaPessoasFront",
                gerenteVendasService.findAllPage(0, 2, "id"));

        modelAndView.addObject("objGerenteVendas", new GerenteVendas());
        modelAndView.addObject("cargos", Cargo.values());

        return modelAndView;
    }

    @RequestMapping(value = "**/atualizargerentevendas/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id, ModelAndView modelAndView) {

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(id);

        modelAndView.setViewName("cadastro/cadastrogerentevendas");
        modelAndView.addObject("objGerenteVendas", gerente.get());
        modelAndView.addObject("listaPessoasFront", gerenteVendasService.findAllPage(0, 2, "id"));
        return modelAndView;
    }

    @RequestMapping(value = "**/deletargerentevendas/{id}", method = RequestMethod.GET)
    public ModelAndView deletar(@PathVariable("id") Long id, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrogerentevendas");

        gerenteVendasService.deleteById(id);

        // adiciono a lista completa sem a pessoa deletada
        modelAndView.addObject("listaPessoasFront",
                gerenteVendasService.findAllPage(0, 2, "id"));

        modelAndView.addObject("objGerenteVendas", new GerenteVendas());
        modelAndView.addObject("msgPraIterar", "Funcionário deletado com sucesso");

        return modelAndView;
    }

}
