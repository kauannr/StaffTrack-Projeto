package com.example.springthymeleaf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.example.springthymeleaf.service.MotoristaService;

@Controller
public class MotoristaController {

    @Autowired
    MotoristaService motoristaService;

    @RequestMapping(value = "**/inicialmotorista", method = RequestMethod.GET)
    public ModelAndView indexMethodGer(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, ModelAndView modelAndView) {

        modelAndView.setViewName("cadastro/cadastromotorista.html");
        modelAndView.addObject("objMotorista", new Motorista());
        modelAndView.addObject("listaPessoasFront", motoristaService.findAllPage(PageRequest.of(page, size, s)));

        return modelAndView;
    }
}
