package com.example.springthymeleaf.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.Telefone;
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

        modelAndView.setViewName("cadastro/cadastromotorista.html");

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
        modelAndView.addObject("objMotorista", new Motorista());

        return modelAndView;
    }

    @RequestMapping(value = "**/atualizarmotorista/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastromotorista.html");
        Optional<Motorista> motorista = motoristaService.findById(id);

        modelAndView.addObject("listaPessoasFront",
                motoristaService.findAllPage(PageRequest.of(0, 2, Sort.by("id"))));
        modelAndView.addObject("objMotorisa", motorista.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletarmotorista/{id}", method = RequestMethod.GET)
    public ModelAndView deletar(@PathVariable("id") long id, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastromotorista");
        motoristaService.deleteById(id);

        // adiciono a lista completa sem a pessoa deletada
        modelAndView.addObject("listaPessoasFront",
                motoristaService.findAllPage(PageRequest.of(0, 2, Sort.by("id"))));

        modelAndView.addObject("objMotorista", new Motorista());
        modelAndView.addObject("msgPraIterar", "Motorista deletado com sucesso");

        return modelAndView;
    }

     // TELEFONES:
    @RequestMapping(value = "**/cadtelefonemotorista/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarTelefone(@PathVariable("idPessoa") Long idPessoa, @Valid Telefone telefone,
            BindingResult bindingResult, ModelAndView modelAndView) {

        modelAndView.setViewName("informacoes/infomotorista.html");

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);

        if (bindingResult.hasErrors()) {
            List<String> msgErros = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgErros.add(error.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErros);
            modelAndView.addObject("listaTelefones", motorista.get().getListaTelefones());
            modelAndView.addObject("objMotorista", motorista.get());
            return modelAndView;
        }

        telefone.setPessoa(motorista.get());
        motoristaService.save(telefone);

        motorista.get().adicionarNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        modelAndView.addObject("listaTelefones", motorista.get().getListaTelefones());
        modelAndView.addObject("objVendedor", motorista.get());

        return modelAndView;
    }
}
