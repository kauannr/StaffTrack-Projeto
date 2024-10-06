package com.example.springthymeleaf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AcessDaniedController {
    @RequestMapping(value = "/403", method=RequestMethod.GET)
    public String irParaTelaDeErro() {
        return "erro403.html";
    }
    
}
