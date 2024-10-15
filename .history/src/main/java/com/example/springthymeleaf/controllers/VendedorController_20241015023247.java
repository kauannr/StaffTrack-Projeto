package com.example.springthymeleaf.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.example.springthymeleaf.model.vendedor.Vendas;
import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.example.springthymeleaf.service.BeneficioService;
import com.example.springthymeleaf.service.ContratoService;
import com.example.springthymeleaf.service.GerenteVendasService;
import com.example.springthymeleaf.service.TelefoneService;
import com.example.springthymeleaf.service.VendasService;
import com.example.springthymeleaf.service.VendedorService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Controller
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private GerenteVendasService gerenteVendasService;

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private Validator validator;

    @Autowired
    BeneficioService beneficioService;

    @Autowired
    VendasService vendasService;

    @Autowired
    ContratoService contratoService;

    @RequestMapping(value = "**/inicialvendedor", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrovendedor.html");
        modelAndView.addObject("objVendedor", new Vendedor());
        modelAndView.addObject("gerentes", gerenteVendasService.findAll());
        modelAndView.addObject("listaPessoasFront", vendedorService.findAllPage(page, size, "id"));
        modelAndView.addObject("editMode", false);

        return modelAndView;
    }

    @RequestMapping(value = "**/vendedor", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(Vendedor vendedor, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastrovendedor.html");
        

        if(vendedor.getContrato().getId()==null || vendedor.getGerente().getId()==null){
            modelAndView.addObject("msgPraIterar", "Preencha os campos necessários");
            modelAndView.addObject("objVendedor", vendedor);
            modelAndView.addObject("gerentes", gerenteVendasService.findAll());
            modelAndView.addObject("listaPessoasFront",
                    vendedorService.findAllPage(0, 2, "id"));
            return modelAndView;
        }
        Optional<GerenteVendas> gerente = gerenteVendasService.findById(vendedor.getGerente().getId());
        vendedor.setGerente(gerente.get());

        if (!Pessoa.validarCPF(vendedor.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objVendedor", vendedor);
            modelAndView.addObject("gerentes", gerenteVendasService.findAll());
            modelAndView.addObject("listaPessoasFront",
                    vendedorService.findAllPage(0, 2, "id"));
            return modelAndView;
        }

        Set<ConstraintViolation<Vendedor>> violacoes = validator.validate(vendedor);
        if (!violacoes.isEmpty()) {

            List<String> listaMensagensErro = new ArrayList<>();
            for (ConstraintViolation<Vendedor> violacao : violacoes) {
                listaMensagensErro.add(violacao.getMessage());
            }
            modelAndView.addObject("objVendedor", vendedor);
            modelAndView.addObject("listaPessoasFront",
                    vendedorService.findAllPage(0, 2, "id"));
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
        modelAndView.addObject("editMode", false);

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

    // TELEFONES:
    @RequestMapping(value = "**/cadtelefonevendedor/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarTelefone(@PathVariable("idPessoa") Long idPessoa, @Valid Telefone telefone,
            BindingResult bindingResult, ModelAndView modelAndView) {

        modelAndView.setViewName("informacoes/infovendedor.html");

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        if (bindingResult.hasErrors()) {
            List<String> msgErros = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgErros.add(error.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErros);
            modelAndView.addObject("listaTelefones", vendedor.get().getListaTelefones());
            modelAndView.addObject("objVendedor", vendedor.get());
            return modelAndView;
        }

        telefone.setPessoa(vendedor.get());
        telefoneService.save(telefone);

        vendedor.get().adicionarNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        modelAndView.addObject("listaTelefones", vendedor.get().getListaTelefones());
        modelAndView.addObject("objVendedor", vendedor.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/deltelefonevendedor/{idTelefone}", method = RequestMethod.GET)
    public ModelAndView deletarTelefone(@PathVariable("idTelefone") Long idTelefone) {

        ModelAndView modelAndView = new ModelAndView("informacoes/infovendedor.html");

        Optional<Telefone> telefone = telefoneService.findById(idTelefone);
        telefoneService.delete(telefone.get());

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objVendedor", telefone.get().getPessoa());

        // MOSTRAR LISTA DE TELEFONES
        modelAndView.addObject("listaTelefones", telefone.get().getPessoa().getListaTelefones());
        modelAndView.addObject("msgPraIterar", "Telefone excluído com sucesso!");

        return modelAndView;
    }
    //

    @RequestMapping(value = "**/infovendedor/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView infoGerenteVendas(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        modelAndView.setViewName("informacoes/infovendedor.html");

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objVendedor", vendedor.get());
        modelAndView.addObject("objTelefone", new Telefone());
        modelAndView.addObject("objContrato", new Contrato());

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", vendedor.get().getListaTelefones());

        return modelAndView;
    }

    @RequestMapping(value = "**/contratovendedor/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView exibirContrato(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratovendedor.html");
        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        modelAndView.addObject("objContrato", vendedor.get().getContrato());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objVendedor", vendedor.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/updcontratovendedor/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView atualizarContrato(@PathVariable("idPessoa") Long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratovendedor.html");

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        Contrato contrato = vendedor.get().getContrato();

        modelAndView.addObject("objContrato", contrato);
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objVendedor", vendedor.get());

        // Adiciona um indicador para exibir o formulário de edição
        modelAndView.addObject("editMode", true);

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarcontratovendedor/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarContrato(@PathVariable("idPessoa") long idPessoa, @Valid Contrato contrato,
            BindingResult bindingResult,
            ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratovendedor.html");

        if (bindingResult.hasErrors()) {
            List<String> msgErros = new ArrayList<>();
            for (ObjectError erro : bindingResult.getAllErrors()) {
                msgErros.add(erro.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErros);
            modelAndView.addObject("objContrato", contrato);
            return modelAndView;
        }

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        vendedor.get().getContrato().setDataFim(contrato.getDataFim());
        vendedor.get().getContrato().setDataInicio(contrato.getDataInicio());
        vendedor.get().getContrato().setNumeroContrato(contrato.getNumeroContrato());
        vendedor.get().getContrato().setTipo(contrato.getTipo());
        vendedor.get().getContrato().setValor(contrato.getValor());

        vendedorService.save(vendedor.get());

        // contratoService.save(contrato);
        modelAndView.addObject("msgPraIterar", "Contrato salvo com sucesso!");
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objContrato", vendedor.get().getContrato());
        modelAndView.addObject("objVendedor", vendedor.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/exibirbeneficiosvendedor/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView exibirBeneficios(@PathVariable("idPessoa") long idPessoa, ModelAndView modelAndView) {

        modelAndView.setViewName("contrato/contratovendedor.html");

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        List<Beneficio> beneficios = vendedor.get().getContrato().getBeneficios();
        if (beneficios.isEmpty()) {
            modelAndView.addObject("msgPraIterar", "Sem benefícios cadastrados");
            modelAndView.addObject("objContrato", vendedor.get().getContrato());
            modelAndView.addObject("objBeneficio", new Beneficio());
            modelAndView.addObject("objVendedor", vendedor.get());
            return modelAndView;
        }

        modelAndView.addObject("objContrato", vendedor.get().getContrato());
        modelAndView.addObject("objBeneficio", new Beneficio());
        modelAndView.addObject("objVendedor", vendedor.get());
        modelAndView.addObject("beneficios", beneficios);

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarbeneficiovendedor/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrarBenefício(@PathVariable("idPessoa") long idPessoa, Beneficio beneficio,
            BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);
        Contrato contrato = vendedor.get().getContrato();
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
            modelAndView.addObject("objVendedor", vendedor.get());
            modelAndView.setViewName("contrato/contratovendedor.html");

            return modelAndView;
        }
        beneficioService.save(beneficio);

        redirectAttributes.addFlashAttribute("msgPraIterar", "Benefício adicionado!");
        redirectAttributes.addFlashAttribute("objContrato", vendedor.get().getContrato());
        redirectAttributes.addFlashAttribute("objBeneficio", new Beneficio());

        return new ModelAndView("redirect:/contratovendedor/" + vendedor.get().getId());
    }

    @RequestMapping(value = "atualizarbeneficiovendedor/{idBeneficio}", method = RequestMethod.POST)
    public ModelAndView atualizarBeneficio(@PathVariable("idBeneficio") long idBeneficio, ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {

        Beneficio beneficio = beneficioService.findById(idBeneficio).get();
        beneficio.setAtivo(!beneficio.isAtivo());

        beneficioService.save(beneficio);

        redirectAttributes.addFlashAttribute("msgPraIterar", "Status do benefício atualizado com sucesso!");

        modelAndView.setViewName("redirect:/exibirbeneficiosgerente/" + beneficio.getContrato().getPessoa().getId());

        return modelAndView;
    }

    // VENDAS AQUI:
    @RequestMapping(value = "vendas/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView vendasVendedor(@PathVariable("idPessoa") long idPessoa,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size,
            ModelAndView modelAndView) {

        modelAndView.setViewName("vendas/vendasvendedor.html");

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);
        if (vendedor.get().getVendas().isEmpty()) {
            modelAndView.addObject("msgPraIterar", "Sem vendas cadastradas");
            modelAndView.addObject("objVenda", new Vendas());
            modelAndView.addObject("objVendedor", vendedor.get());
            return modelAndView;
        }

        modelAndView.addObject("listaVendas", vendasService.vendasPage(vendedor.get(), page, size, "id"));
        modelAndView.addObject("objVenda", new Vendas());
        modelAndView.addObject("objVendedor", vendedor.get());

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarvendavendedor/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView salvarVenda(@PathVariable("idPessoa") long idPessoa, @Valid Vendas venda,
            ModelAndView modelAndView,
            BindingResult bindingResult) {

        modelAndView.setViewName("vendas/vendasvendedor.html");

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);
        venda.setVendedor(vendedor.get());

        if (bindingResult.hasErrors()) {
            Set<String> msgErro = new HashSet<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgErro.add(error.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", msgErro);
            modelAndView.addObject("objVenda", new Vendas());
            modelAndView.addObject("objVendedor", vendedor.get());
            return modelAndView;
        }

        vendedor.get().adicionarVenda(venda);
        vendedorService.save(vendedor.get());

        modelAndView.addObject("msgPraIterar", "Venda salva com sucesso");
        modelAndView.addObject("listaVendas", vendasService.vendasPage(vendedor.get(), 0, 2, "id"));
        modelAndView.addObject("objVenda", new Vendas());
        modelAndView.addObject("objVendedor", vendedor.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/pesquisarvendas/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView pesquisarVendas(@PathVariable("idPessoa") long idPessoa,
            @RequestParam(required = false) Long idVenda,
            @RequestParam(required = false) Double valorVenda,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            ModelAndView modelAndView) {

        modelAndView.setViewName("vendas/vendasvendedor.html");
        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);
        Page<Vendas> listaVendas;

        if (idVenda != null) {
            listaVendas = new PageImpl<>(Collections.singletonList(vendasService.findById(idVenda).get()));
        } else if (valorVenda != null) {
            listaVendas = vendasService.findByValorVendaAndVendedor(valorVenda, vendedor.get(),
                    PageRequest.of(page, size));
        } else {
            listaVendas = vendasService.findAll(PageRequest.of(page, size, Sort.by("id")));
        }

        modelAndView.addObject("listaVendas", listaVendas);
        modelAndView.addObject("objVenda", new Vendas());
        modelAndView.addObject("objVendedor", listaVendas.getContent().get(0).getVendedor());

        return modelAndView;
    }

    @RequestMapping(value = "**/vendasnoperiodo/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView vendasNoPeriodo(@PathVariable("idPessoa") long idPessoa,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date de,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date ate, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("vendas/vendasvendedor.html");

        Optional<Vendedor> vendedor = vendedorService.findById(idPessoa);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Vendas> listaVendas = vendasService.vendasNoPeriodo(vendedor.get(), de, ate, pageable);

        modelAndView.addObject("listaVendas", listaVendas);
        modelAndView.addObject("objVenda", new Vendas());
        modelAndView.addObject("objVendedor", vendedor.get());

        return modelAndView;
    }

}
