package com.example.springthymeleaf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.vendedor.Vendedor;

@Controller
public class MotoristaController {


    @RequestMapping(value = "**/inicialmotorista", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastrovendedor.html");
        modelAndView.addObject("objVendedor", new Vendedor());
        modelAndView.addObject("gerentes", gerenteVendasService.findAll());
        modelAndView.addObject("listaPessoasFront", vendedorService.findAllPage(page, size, "id"));

        return modelAndView;
    }
}
