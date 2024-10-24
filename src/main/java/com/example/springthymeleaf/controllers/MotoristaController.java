package com.example.springthymeleaf.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.Telefone;
import com.example.springthymeleaf.model.contrato.Beneficio;
import com.example.springthymeleaf.model.contrato.Contrato;
import com.example.springthymeleaf.model.motorista.CNH;
import com.example.springthymeleaf.model.motorista.Entrega;
import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.service.BeneficioService;
import com.example.springthymeleaf.service.CNHService;
import com.example.springthymeleaf.service.EntregaService;
import com.example.springthymeleaf.service.MotoristaService;
import com.example.springthymeleaf.service.TelefoneService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Controller
public class MotoristaController {

    @Autowired
    MotoristaService motoristaService;

    @Autowired
    TelefoneService telefoneService;

    @Autowired
    Validator validator;

    @Autowired
    BeneficioService beneficioService;

    @Autowired
    CNHService cnhService;

    @Autowired
    EntregaService entregaService;

    @RequestMapping(value = "**/inicialmotorista", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastromotorista.html");

        modelAndView.addObject("objMotorista", new Motorista());
        modelAndView.addObject("listaPessoasFront",
                motoristaService.findAllPage(PageRequest.of(page, size, Sort.by("id"))));
        modelAndView.addObject("editMode", false);

        return modelAndView;
    }

    @RequestMapping(value = "**/motorista", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(Motorista motorista, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastromotorista.html");
        CNH cnh = motorista.getCnh();
        cnh.setMotorista(motorista);
        cnhService.save(cnh);

        if (!Pessoa.validarCPF(motorista.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objMotorista", motorista);
            modelAndView.addObject("listaPessoasFront",
                    motoristaService.findAllPage(PageRequest.of(0, 2, Sort.by("id"))));
            return modelAndView;
        }

        Set<ConstraintViolation<Motorista>> violacoes = validator.validate(motorista);
        if (!violacoes.isEmpty()) {
            List<String> msgErro = new ArrayList<>();
            for (ConstraintViolation<Motorista> violacao : violacoes) {
                System.out.println("Erro de validação: " + violacao.getMessage());
                msgErro.add(violacao.getMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErro);
            modelAndView.addObject("objMotorista", motorista);
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
        modelAndView.addObject("objMotorista", motorista.get());
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

    @RequestMapping(value = "pesquisarmotorista", method = RequestMethod.GET)
    public ModelAndView pesquisar(@RequestParam(required = false) String nomePesquisa,
            @RequestParam(required = false) String sobrenomePesquisa, @RequestParam(required = false) Long idPesquisa,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastromotorista");

        Page<Motorista> pageable = null;
        int numDeEncontrados = 0;

        if (nomePesquisa != null && !nomePesquisa.isEmpty()) {
            pageable = motoristaService.findByNamePage(nomePesquisa, page, size, "id");
            numDeEncontrados = pageable.getContent().size();
        } else if (sobrenomePesquisa != null && !sobrenomePesquisa.isEmpty()) {
            pageable = motoristaService.findBySobrenomePage(sobrenomePesquisa, page, size, "id");
            numDeEncontrados = pageable.getContent().size();
        } else if (idPesquisa != null) {
            pageable = motoristaService.findByIdPage(idPesquisa, page, size, "id");
            numDeEncontrados = pageable.getContent().size();
        }
        if (pageable == null || pageable.getContent().isEmpty()) {
            pageable = motoristaService.findAllPage(page, size, "id");
        }

        modelAndView.addObject("msgPraIterar", numDeEncontrados + " encontrados");
        modelAndView.addObject("listaPessoasFront", pageable);
        modelAndView.addObject("objMotorista", new Motorista());
        modelAndView.addObject("editMode", false);

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
        telefoneService.save(telefone);

        motorista.get().adicionarNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        modelAndView.addObject("listaTelefones", motorista.get().getListaTelefones());
        modelAndView.addObject("objMotorista", motorista.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/deltelefonemotorista/{idTelefone}", method = RequestMethod.GET)
    public ModelAndView deletarTelefone(@PathVariable("idTelefone") Long idTelefone) {

        ModelAndView modelAndView = new ModelAndView("informacoes/infomotorista.html");

        Optional<Telefone> telefone = telefoneService.findById(idTelefone);
        telefoneService.delete(telefone.get());

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objMotorista", telefone.get().getPessoa());

        // MOSTRAR LISTA DE TELEFONES
        modelAndView.addObject("listaTelefones", telefone.get().getPessoa().getListaTelefones());
        modelAndView.addObject("msgPraIterar", "Telefone excluído com sucesso!");

        return modelAndView;
    }
    //

    @RequestMapping(value = "**/infomotorista/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView infoGerenteVendas(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);

        modelAndView.setViewName("informacoes/infomotorista.html");

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objMotorista", motorista.get());
        modelAndView.addObject("objTelefone", new Telefone());
        modelAndView.addObject("objContrato", new Contrato());

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", motorista.get().getListaTelefones());

        return modelAndView;
    }

    @RequestMapping(value = "**/contratomotorista/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView exibirContrato(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratomotorista.html");
        Optional<Motorista> motorista = motoristaService.findById(idPessoa);

        modelAndView.addObject("objContrato", motorista.get().getContrato());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objMotorista", motorista.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/updcontratomotorista/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView atualizarContrato(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratomotorista.html");

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);
        Contrato contrato = motorista.get().getContrato();

        modelAndView.addObject("objContrato", contrato);
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objMotorista", motorista.get());

        // Adiciona um indicador para exibir o formulário de edição
        modelAndView.addObject("editMode", true);

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarcontratomotorista/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarContrato(@PathVariable("idPessoa") long idPessoa, @Valid Contrato contrato,
            BindingResult bindingResult,
            ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratomotorista.html");
        Optional<Motorista> motorista = motoristaService.findById(idPessoa);

        if (bindingResult.hasErrors()) {
            List<String> msgErros = new ArrayList<>();
            for (ObjectError erro : bindingResult.getAllErrors()) {
                msgErros.add(erro.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErros);
            modelAndView.addObject("objContrato", contrato);
            modelAndView.addObject("objBeneficio", new Beneficio());
            modelAndView.addObject("objMotorista", motorista.get());

            return modelAndView;
        }

        motorista.get().getContrato().setDataFim(contrato.getDataFim());
        motorista.get().getContrato().setDataInicio(contrato.getDataInicio());
        motorista.get().getContrato().setNumeroContrato(contrato.getNumeroContrato());
        motorista.get().getContrato().setTipo(contrato.getTipo());
        motorista.get().getContrato().setValor(contrato.getValor());

        motoristaService.save(motorista.get());

        modelAndView.addObject("msgPraIterar", "Contrato salvo com sucesso!");
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objContrato", motorista.get().getContrato());
        modelAndView.addObject("objMotorista", motorista.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/exibirbeneficiosmotorista/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView exibirBeneficios(@PathVariable("idPessoa") long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratomotorista.html");

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);

        List<Beneficio> beneficios = motorista.get().getContrato().getBeneficios();
        if (beneficios.isEmpty()) {
            modelAndView.addObject("msgPraIterar", "Sem benefícios cadastrados");
            modelAndView.addObject("objContrato", motorista.get().getContrato());
            modelAndView.addObject("objBeneficio", new Beneficio());
            modelAndView.addObject("objMotorista", motorista.get());
            return modelAndView;
        }

        modelAndView.addObject("objContrato", motorista.get().getContrato());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objMotorista", motorista.get());
        modelAndView.addObject("beneficios", beneficios);

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarbeneficiomotorista/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarBenefício(@PathVariable("idPessoa") long idPessoa, Beneficio beneficio,
            BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);
        Contrato contrato = motorista.get().getContrato();
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
            modelAndView.addObject("objMotorista", motorista.get());
            modelAndView.setViewName("contrato/contratomotorista.html");

            return modelAndView;
        }
        beneficioService.save(beneficio);

        redirectAttributes.addFlashAttribute("msgPraIterar", "Benefício adicionado!");
        redirectAttributes.addFlashAttribute("objContrato", motorista.get().getContrato());
        redirectAttributes.addFlashAttribute("objBeneficio", new Beneficio());

        return new ModelAndView("redirect:/contratomotorista/" + motorista.get().getId());
    }

    @RequestMapping(value = "atualizarbeneficiomotorista/{idBeneficio}", method = RequestMethod.POST)
    public ModelAndView atualizarBeneficio(@PathVariable("idBeneficio") long idBeneficio, ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {

        Beneficio beneficio = beneficioService.findById(idBeneficio).get();
        beneficio.setAtivo(!beneficio.isAtivo());

        beneficioService.save(beneficio);

        redirectAttributes.addFlashAttribute("msgPraIterar", "Status do benefício atualizado com sucesso!");

        modelAndView.setViewName("redirect:/exibirbeneficiosmotorista/" + beneficio.getContrato().getPessoa().getId());

        return modelAndView;
    }

    // ENTREGAS:
    @RequestMapping(value = "entregas/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView entregas(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("entrega/entregamotorista");

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);
        modelAndView.addObject("objEntrega", new Entrega());
        modelAndView.addObject("listaDeEntregas", motorista.get().getListaEntregas());
        modelAndView.addObject("ObjMotorista", motorista.get().getId());
        modelAndView.addObject("editMode", false);

        return modelAndView;
    }

    @RequestMapping(value = "salvarentregamotorista/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView requestMethodName(@PathVariable("idPessoa") long idPessoa, Entrega entrega,
            ModelAndView modelAndView) {
        modelAndView.setViewName("entrega/entregamotorista");

        Optional<Motorista> motorista = motoristaService.findById(idPessoa);
        Optional<Entrega> entrega2 = null;

        Set<ConstraintViolation<Entrega>> violacoes;;

        if (entregaService.isAtualizado(entrega)) {
            entrega2 = entregaService.findById(entrega.getId());
            entrega2.get().setStatusEntrega(entrega.getStatusEntrega());

            motorista.get().getListaEntregas().add(entrega2.get());
            entrega2.get().setMotorista(motorista.get());
            violacoes = validator.validate(entrega2.get());
        } else {

            motorista.get().getListaEntregas().add(entrega);
            entrega.setMotorista(motorista.get());
           violacoes = validator.validate(entrega);
        }

        if (!violacoes.isEmpty()) {
            List<String> msgErro = new ArrayList<>();
            for (ConstraintViolation<Entrega> constraintViolation : violacoes) {
                msgErro.add(constraintViolation.getMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErro);
            modelAndView.addObject("objEntrega", new Entrega());
            modelAndView.addObject("listaDeEntregas", motorista.get().getListaEntregas());
            modelAndView.addObject("ObjMotorista", motorista.get().getId());
            modelAndView.addObject("editMode", false);

            return modelAndView;
        }

        motoristaService.save(motorista.get());
        modelAndView.addObject("msgPraIterar", "Entrega salva com sucesso!");
        modelAndView.addObject("objEntrega", new Entrega());
        modelAndView.addObject("listaDeEntregas", motorista.get().getListaEntregas());
        modelAndView.addObject("ObjMotorista", motorista.get().getId());
        modelAndView.addObject("editMode", false);

        return modelAndView;
    }

    @RequestMapping(value = "editarentrega/{idEntrega}", method = RequestMethod.GET)
    public ModelAndView requestMethodName(@PathVariable("idEntrega") long idEntrega, ModelAndView modelAndView) {

        modelAndView.setViewName("entrega/entregamotorista");
        Optional<Entrega> entrega = entregaService.findById(idEntrega);
        Optional<Motorista> motorista = motoristaService.findById(entrega.get().getMotorista().getId());

        modelAndView.addObject("objEntrega", entrega.get());
        modelAndView.addObject("listaDeEntregas", motorista.get().getListaEntregas());
        modelAndView.addObject("ObjMotorista", motorista.get().getId());
        modelAndView.addObject("editMode", true);

        return modelAndView;
    }

}
