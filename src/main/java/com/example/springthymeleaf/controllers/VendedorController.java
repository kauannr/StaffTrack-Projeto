package com.example.springthymeleaf.controllers;

import java.util.*;

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
import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.example.springthymeleaf.service.GerenteVendasService;
import com.example.springthymeleaf.service.VendedorService;

import jakarta.validation.Valid;

@Controller
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private GerenteVendasService gerenteVendasService;

    @RequestMapping(value = "**/inicialvendedor", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrovendedor.html");
        modelAndView.addObject("objVendedor", new Vendedor());
        modelAndView.addObject("gerentes", gerenteVendasService.findAll());
        modelAndView.addObject("listaPessoasFront", vendedorService.findAllPage(page, size, "id"));

        return modelAndView;
    }

    @RequestMapping(value = "**/vendedor", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid Vendedor vendedor, BindingResult bindingResult) {

        // VALIDAÇÕES E ERROS:
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastrovendedor.html");
        GerenteVendas gerente = gerenteVendasService.findById(vendedor.getGerente().getId()).get();
        vendedor.setGerente(gerente);

        if (!Pessoa.validarCPF(vendedor.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objVendedor", vendedor);
            modelAndView.addObject("gerentes", gerenteVendasService.findAll());
            modelAndView.addObject("listaPessoasFront",
                    vendedorService.findAllPage(0, 2, "id"));
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {
            // VOLTAR PRA TELA COM OS DADOS DA PESSOA:
            modelAndView.addObject("objVendedor", vendedor);

            // PRA LISTA DE PESSOAS CONTINUAR NA TELA:
            // modelAndView.addObject("listaPessoasFront",
            // gerenteVendasService.findAllPage(0, 2, "id"));

            // modelAndView.addObject("cargos", Cargo.values());

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

    @RequestMapping(value = "**/atualizarvendedor/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id, ModelAndView modelAndView) {

        Optional<Vendedor> vendedor = vendedorService.findById(id);

        modelAndView.setViewName("cadastro/cadastrovendedor");
        modelAndView.addObject("listaPessoasFront", vendedorService.findAllPage(0, 2, "id"));
        modelAndView.addObject("gerentes", gerenteVendasService.findAll());

        modelAndView.addObject("objVendedor", vendedor.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletarvendedor/{id}", method = RequestMethod.GET)
    public ModelAndView deletar(@PathVariable("id") long id, ModelAndView modelAndView) {

        vendedorService.deleteById(id);

        modelAndView.setViewName("cadastro/cadastrogerentevendas");

        // adiciono a lista completa sem a pessoa deletada
        modelAndView.addObject("listaPessoasFront",
                gerenteVendasService.findAllPage(0, 2, "id"));

        modelAndView.addObject("objVendedor", new Vendedor());
        modelAndView.addObject("msgPraIterar", "Funcionário deletado com sucesso");

        return modelAndView;
    }

}
