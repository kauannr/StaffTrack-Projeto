package com.example.springthymeleaf.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.Telefone;
import com.example.springthymeleaf.model.enums.Cargo;
import com.example.springthymeleaf.service.PessoaService;
import com.example.springthymeleaf.service.TelefoneService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PessoaController {

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    PessoaService pessoaService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexMethod(ModelAndView modelAndView) {
        modelAndView.setViewName("index");

        modelAndView.addObject("cargos", Cargo.values());

        return modelAndView;
    }

    @RequestMapping(value = "**/teladeinicio", method = RequestMethod.GET)
    public ModelAndView telaDeInicio(@RequestParam(defaultValue = "0") int page, 
    @RequestParam(defaultValue = "4") int size) {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");
        modelAndView.addObject("listaPessoasFront",
                pessoaService.findAllPage(page, size, "id"));

        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("cargos", Cargo.values());

        return modelAndView;
    }

    @RequestMapping(value = "/pessoaspag", method = RequestMethod.GET)
    public ModelAndView carregarPessoaPorPaginacao(@PageableDefault(size = 4) Pageable pageable,
            ModelAndView modelAndView, @RequestParam(value = "nome", required = false) String nome) {

        Page<Pessoa> pagePessoa = null;
        if (nome == null || nome.trim().isEmpty()) {
            pagePessoa = pessoaService.findAllPage(pageable);
        }

        modelAndView.addObject("listaPessoasFront", pagePessoa);
        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("nomePesquisa", nome);
        modelAndView.setViewName("cadastro/cadastropessoa.html");

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarpessoa", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

        // VALIDAÇÕES E ERROS:
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");

        if (!Pessoa.validarCPF(pessoa.getCpf())) {
            modelAndView.addObject("msgPraIterar", "CPF inválido");
            modelAndView.addObject("objPessoa", pessoa);
            modelAndView.addObject("cargos", Cargo.values());
            modelAndView.addObject("listaPessoasFront", pessoaService.findAllPage(0, 2, "id"));
            return modelAndView;
        }

        if (bindingResult.hasErrors()) {
            // VOLTAR PRA TELA COM OS DADOS DA PESSOA:
            modelAndView.addObject("objPessoa", pessoa);

            // PRA LISTA DE PESSOAS CONTINUAR NA TELA:
            modelAndView.addObject("listaPessoasFront",
                    pessoaService.findAllPage(0, 2, "id"));

            modelAndView.addObject("cargos", Cargo.values());

            List<String> listaMensagensErro = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                listaMensagensErro.add(objectError.getDefaultMessage()); // Mensagem que vem do @NotNull
            }
            modelAndView.addObject("msgPraIterar", listaMensagensErro);
            modelAndView.addObject("cargos", Cargo.values());

            return modelAndView;
        }

        // SALVAMENTO:
        String msgRetornadaPraTela = pessoa.getId() == null ? "Usuário salvo com sucesso!"
                : "Usuário atualizado com sucesso!";

        pessoaService.save(pessoa);

        modelAndView.addObject("msgPraIterar", msgRetornadaPraTela);

        modelAndView.addObject("listaPessoasFront",
                pessoaService.findAllPage(0, 2, "id"));

        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("cargos", Cargo.values());

        return modelAndView;

    }

    @RequestMapping(value = "**/listartodos", method = RequestMethod.GET)
    public ModelAndView listarTodos() {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

        modelAndView.addObject("listaPessoasFront",
                pessoaService.findAllPage(0, 2, "id"));
        modelAndView.addObject("objPessoa", new Pessoa());

        return modelAndView;
    }

    @RequestMapping(value = "**/atualizarpessoa/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id) {

        Optional<Pessoa> pessoa = pessoaService.findById(id);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("objPessoa", pessoa.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletar/{id}", method = RequestMethod.GET)
    public ModelAndView deletar(@PathVariable("id") Long id) {
        // deleto
        Pessoa pessoa = pessoaService.findById(id).get();
        pessoaService.delete(pessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

        // adiciono a lista completa sem a pessoa deletada
        modelAndView.addObject("listaPessoasFront",
                pessoaService.findAllPage(0, 2, "id"));

        // pessoa vazia pro formulário de inicio
        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("msgPraIterar", "Usuário deletado com sucesso");

        return modelAndView;
    }

    @RequestMapping(value = "**/pesquisar", method = RequestMethod.GET)
    public ModelAndView pesquisar(@RequestParam("nome") String nome, @PageableDefault(size = 2) Pageable pageable,
            ModelAndView modelAndView) {

        modelAndView.addObject("listaPessoasFront", pessoaService.realizarPesquisa(nome, pageable, modelAndView));

        return modelAndView;
    }

    @RequestMapping(value = "**/telefones/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView telefones(@PathVariable("idPessoa") Long idPessoa) {

        Optional<Pessoa> pessoa = pessoaService.findById(idPessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objPessoa", pessoa.get());

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", pessoa.get().getListaTelefones());

        return modelAndView;
    }

    @RequestMapping(value = "**/cadastrartelefone/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrartelefone(@PathVariable("idPessoa") Long idPessoa, @Valid Telefone telefone,
            BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        Optional<Pessoa> pessoa = pessoaService.findById(idPessoa);
        telefone.setPessoa(pessoa.get());
        telefoneService.save(telefone);

        pessoa.get().adicionarNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", pessoa.get().getListaTelefones());
        // PRA CONTINUAR COM OS DADOS DA PESSOA NA TELA
        modelAndView.addObject("objPessoa", pessoa.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletarTelefone/{idTelefone}", method = RequestMethod.GET)
    public ModelAndView deletarTelefone(@PathVariable("idTelefone") Long idTelefone) {

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        Optional<Telefone> telefone = telefoneService.findById(idTelefone);
        telefoneService.delete(telefone.get());

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objPessoa", telefone.get().getPessoa());

        // MOSTRAR LISTA DE TELEFONES
        modelAndView.addObject("listaTelefones", telefone.get().getPessoa().getListaTelefones());
        modelAndView.addObject("msgPraIterar", "Telefone deletado com sucesso!");

        return modelAndView;
    }

}
