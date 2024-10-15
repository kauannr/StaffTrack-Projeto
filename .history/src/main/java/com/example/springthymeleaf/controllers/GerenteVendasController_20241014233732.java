package com.example.springthymeleaf.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.Telefone;
import com.example.springthymeleaf.model.contrato.Beneficio;
import com.example.springthymeleaf.model.contrato.Contrato;
import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.service.BeneficioService;
import com.example.springthymeleaf.service.ContratoService;
import com.example.springthymeleaf.service.GerenteVendasService;
import com.example.springthymeleaf.service.TelefoneService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Controller
public class GerenteVendasController {

    @Autowired
    private GerenteVendasService gerenteVendasService;

    @Autowired
    TelefoneService telefoneService;

    @Autowired
    ContratoService contratoService;

    @Autowired
    private BeneficioService beneficioService;

    @Autowired
    private Validator validator;

    @RequestMapping(value = "**/inicialgerente", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrogerentevendas.html");
        modelAndView.addObject("objGerenteVendas", new GerenteVendas());
        modelAndView.addObject("listaPessoasFront", gerenteVendasService.findAllPage(page, size, "id"));
        modelAndView.addObject("editMode", false);

        return modelAndView;
    }

    @RequestMapping(value = "**/gerentevendas", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid GerenteVendas gerenteVendas, BindingResult bindingResult) {

        Contrato contrato = gerenteVendas.getContrato();
        contrato.setPessoa(gerenteVendas);

        // VALIDAÇÕES E ERROS:
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastrogerentevendas.html");

        if (!Pessoa.validarCPF(gerenteVendas.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objGerenteVendas", gerenteVendas);
            modelAndView.addObject("listaPessoasFront",
                    gerenteVendasService.findAllPage(0, 2, "id"));
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {

            modelAndView.addObject("objGerenteVendas", gerenteVendas);

            modelAndView.addObject("listaPessoasFront",
                    gerenteVendasService.findAllPage(0, 2, "id"));

            Set<String> listaMensagensErro = new HashSet<>();
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

        return modelAndView;
    }

    @RequestMapping(value = "**/atualizargerentevendas/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id, ModelAndView modelAndView) {

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(id);

        modelAndView.setViewName("cadastro/cadastrogerentevendas");
        modelAndView.addObject("objGerenteVendas", gerente.get());
        modelAndView.addObject("listaPessoasFront", gerenteVendasService.findAllPage(0, 2, "id"));
        modelAndView.addObject("editMode", true);
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

    @RequestMapping(value = "**/cadtelefonegerente/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarTelefone(@PathVariable("idPessoa") Long idPessoa, @Valid Telefone telefone,
            BindingResult bindingResult, ModelAndView modelAndView) {

        modelAndView.setViewName("informacoes/infogerentevendas.html");

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);

        if (bindingResult.hasErrors()) {
            List<String> msgErros = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgErros.add(error.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErros);
            modelAndView.addObject("listaTelefones", gerente.get().getListaTelefones());
            modelAndView.addObject("objGerenteVendas", gerente.get());
            return modelAndView;
        }

        telefone.setPessoa(gerente.get());
        telefoneService.save(telefone);

        gerente.get().adicionarNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        modelAndView.addObject("listaTelefones", gerente.get().getListaTelefones());
        modelAndView.addObject("objGerenteVendas", gerente.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/deltelefonegerente/{idTelefone}", method = RequestMethod.GET)
    public ModelAndView deletarTelefone(@PathVariable("idTelefone") Long idTelefone) {

        ModelAndView modelAndView = new ModelAndView("informacoes/infogerentevendas.html");

        Optional<Telefone> telefone = telefoneService.findById(idTelefone);
        telefoneService.delete(telefone.get());

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objGerenteVendas", telefone.get().getPessoa());

        // MOSTRAR LISTA DE TELEFONES
        modelAndView.addObject("listaTelefones", telefone.get().getPessoa().getListaTelefones());
        modelAndView.addObject("msgPraIterar", "Telefone excluído com sucesso!");

        return modelAndView;
    }

    @RequestMapping(value = "**/infogerentevendas/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView infoGerenteVendas(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);

        modelAndView.setViewName("informacoes/infogerentevendas.html");

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objGerenteVendas", gerente.get());
        modelAndView.addObject("objTelefone", new Telefone());
        modelAndView.addObject("objContrato", new Contrato());

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", gerente.get().getListaTelefones());

        return modelAndView;
    }

    @RequestMapping(value = "**/contratogerente/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView exibirContrato(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");
        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);

        modelAndView.addObject("objContrato", gerente.get().getContrato());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objGerenteVendas", gerente.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/salvarcontratogerente/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarContrato(@PathVariable("idPessoa") long idPessoa, @Valid Contrato contrato,
            BindingResult bindingResult,
            ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");

        if (bindingResult.hasErrors()) {
            List<String> msgErros = new ArrayList<>();
            for (ObjectError erro : bindingResult.getAllErrors()) {
                msgErros.add(erro.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErros);
            modelAndView.addObject("objContrato", contrato);
            return modelAndView;
        }

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);

        gerente.get().getContrato().setDataFim(contrato.getDataFim());
        gerente.get().getContrato().setDataInicio(contrato.getDataInicio());
        gerente.get().getContrato().setNumeroContrato(contrato.getNumeroContrato());
        gerente.get().getContrato().setTipo(contrato.getTipo());
        gerente.get().getContrato().setValor(contrato.getValor());

        gerenteVendasService.save(gerente.get());

        // contratoService.save(contrato);
        modelAndView.addObject("msgPraIterar", "Contrato salvo com sucesso!");
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objContrato", gerente.get().getContrato());
        modelAndView.addObject("objGerenteVendas", gerente.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/updcontratogerente/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView atualizarContrato(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);

        Contrato contrato = gerente.get().getContrato();

        modelAndView.addObject("objContrato", contrato);
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objGerenteVendas", gerente.get());

        // Adiciona um indicador para exibir o formulário de edição
        modelAndView.addObject("editMode", true);  // Ativando a flag de edição

        return modelAndView;
    }

    @RequestMapping(value = "**/exibirbeneficiosgerente/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView exibirBeneficios(@PathVariable("idPessoa") long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);

        List<Beneficio> beneficios = gerente.get().getContrato().getBeneficios();
        if (beneficios.isEmpty()) {
            modelAndView.addObject("msgPraIterar", "Sem benefícios cadastrados");
            modelAndView.addObject("objContrato", gerente.get().getContrato());
            modelAndView.addObject("objBeneficio", new Beneficio());
            modelAndView.addObject("objGerenteVendas", gerente.get());
            return modelAndView;
        }

        modelAndView.addObject("objContrato", gerente.get().getContrato());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objGerenteVendas", gerente.get());
        modelAndView.addObject("beneficios", beneficios);

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarbeneficiogerente/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarBenefício(@PathVariable("idPessoa") long idPessoa, Beneficio beneficio,
            BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {

        Optional<GerenteVendas> gerente = gerenteVendasService.findById(idPessoa);
        Contrato contrato = gerente.get().getContrato();
        contrato.adicionarBeneficio(beneficio);

        Set<ConstraintViolation<Beneficio>> violacoes = validator.validate(beneficio);
        if (!violacoes.isEmpty()) {
            List<String> msgErro = new ArrayList<>();
            for (ConstraintViolation<Beneficio> violacao : violacoes) {
                System.out.println("Erro de validação: " + violacao.getMessage());
                msgErro.add(violacao.getMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErro);
            modelAndView.addObject("objBeneficio", beneficio);
            modelAndView.addObject("objContrato", contrato);
            modelAndView.addObject("objGerenteVendas", gerente.get());
            modelAndView.setViewName("contrato/contratogerentevendas.html");

            return modelAndView;
        }
        beneficioService.save(beneficio);

        redirectAttributes.addFlashAttribute("msgPraIterar", "Benefício adicionado!");
        redirectAttributes.addFlashAttribute("objContrato", gerente.get().getContrato());
        redirectAttributes.addFlashAttribute("objBeneficio", new Beneficio());

        return new ModelAndView("redirect:/contratogerente/" + gerente.get().getId());
    }

    

}
