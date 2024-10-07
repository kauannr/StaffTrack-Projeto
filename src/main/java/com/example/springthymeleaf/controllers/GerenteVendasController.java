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
import com.example.springthymeleaf.model.Telefone;
import com.example.springthymeleaf.model.contrato.Beneficio;
import com.example.springthymeleaf.model.contrato.Contrato;
import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.service.BeneficiosService;
import com.example.springthymeleaf.service.ContratoService;
import com.example.springthymeleaf.service.GerenteVendasService;
import com.example.springthymeleaf.service.TelefoneService;

import jakarta.validation.Valid;

@Controller
public class GerenteVendasController {

    @Autowired
    private GerenteVendasService gerenteVendasService;

    @Autowired
    TelefoneService telefoneService;

    @Autowired
    ContratoService contratoService;

    @Autowired
    private BeneficiosService beneficiosService;

    @RequestMapping(value = "**/inicialgerente", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrogerentevendas.html");
        modelAndView.addObject("objGerenteVendas", new GerenteVendas());
        modelAndView.addObject("listaPessoasFront", gerenteVendasService.findAllPage(page, size, "id"));

        return modelAndView;
    }

    @RequestMapping(value = "**/gerentevendas", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid GerenteVendas gerenteVendas, BindingResult bindingResult, Contrato contrato) {

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
        return modelAndView;
    }

    @RequestMapping(value = "**/salvarcontratogerente", method = RequestMethod.POST)
    public ModelAndView cadastrarContrato(@Valid Contrato contrato, BindingResult bindingResult,
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

        contratoService.save(contrato);
        modelAndView.addObject("msgPraIterar", "Contrato salvo com sucesso!");

        modelAndView.addObject("objContrato", contrato);
        return modelAndView;
    }

    @RequestMapping(value = "**/updcontratogerente/{idContrato}", method = RequestMethod.GET)
    public ModelAndView atualizarContrato(@PathVariable("idContrato") Long idContrato, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");

        Optional<Contrato> contrato = contratoService.findById(idContrato);

        modelAndView.addObject("objContrato", contrato.get());
        modelAndView.addObject("objBeneficio", new Beneficio());

        // Adiciona um indicador para exibir o formulário de edição
        modelAndView.addObject("editMode", true);

        return modelAndView;
    }

    @RequestMapping(value = "exibirbeneficiosgerente/{idContrato}", method = RequestMethod.GET)
    public ModelAndView exibirBeneficios(@PathVariable("idContrato") long id, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");

        Optional<Contrato> contrato = contratoService.findById(id);
        List<Beneficio> beneficios = contrato.get().getBeneficios();
        if (beneficios.isEmpty()) {
            modelAndView.addObject("msgPraIterar", "Sem benefícios cadastrados");
            modelAndView.addObject("objContrato", contrato.get());
            modelAndView.addObject("objBeneficio", new Beneficio());
            return modelAndView;
        }

        modelAndView.addObject("objContrato", contrato.get());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("beneficios", beneficios);

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarbeneficiogerente/{idContrato}", method = RequestMethod.POST)
    public ModelAndView cadastrarBenefício(@PathVariable("idContrato") long idContrato, @Valid Beneficio beneficio,
            BindingResult bindingResult, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratogerentevendas.html");

        if (bindingResult.hasErrors()) {
            List<String> msgErro = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgErro.add(error.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErro);
            modelAndView.addObject("objBeneficio", beneficio);
            // ver se precisa do objContrato
        }

        Optional<Contrato> contrato = contratoService.findById(idContrato);
        beneficio.setContrato(contrato.get());
        contrato.get().adicionarBeneficio(beneficio);
        beneficiosService.save(beneficio);

        modelAndView.addObject("msgPraIterar", "Benefício adicionado!");
        modelAndView.addObject("objContrato", new Contrato());
        modelAndView.addObject("objBeneficio", new Beneficio());

        return modelAndView;
    }

}
