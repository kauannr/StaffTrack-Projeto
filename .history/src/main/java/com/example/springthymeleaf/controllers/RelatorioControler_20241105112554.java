package com.example.springthymeleaf.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.service.PdfService;
import com.example.springthymeleaf.service.PessoaService;

@Controller
public class RelatorioControler {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "/relatorios", method = RequestMethod.GET)
    public ModelAndView relatorios(ModelAndView modelAndView) {
        modelAndView.setViewName("relatorio/selecionarrelatorio");

        return modelAndView;
    }

    // Geração de relatório de funcionário específico
    @RequestMapping(value = "/gerarrelatoriofuncionario", method = RequestMethod.POST)
    public ModelAndView gerarRelatorioFuncionario(@RequestParam("idFuncionario") Long idFuncionario,
            ModelAndView modelAndView) {

        modelAndView.setViewName("relatorio/selecionarrelatorio");
        try {
            // Busca o funcionário pelo ID
            Optional<Pessoa> funcionario = pessoaService.findById(idFuncionario);

            // Gera o relatório em PDF
            byte[] pdfBytes = pdfService.gerarRelatorioFuncionario(funcionario.get());

            // Salva o PDF em um local temporário ou pasta de relatórios
            File diretorio = new File(
                    "C:\\Users\\kauan\\Downloads\\Faculdade\\ENG-DE-SOFTWARE\\ProjetoEngSoft\\relatorios");
            String pdfPath = diretorio.getAbsolutePath() + "\\relatorio_func_" + idFuncionario + ".pdf";

            try (FileOutputStream fos = new FileOutputStream(pdfPath)) {
                fos.write(pdfBytes);
            }

            // Adiciona a mensagem de sucesso ao objeto ModelAndView
            modelAndView.addObject("msgPraIterar",
                    "Relatório do funcionário " + idFuncionario + " gerado com sucesso.\nVerifique a pasta de relatórios");
            modelAndView.addObject("pdfDownloadLink", pdfPath); // Link para o PDF gerado

        } catch (Exception e) {
            modelAndView.addObject("msgPraIterar", "Erro ao gerar relatório: ");
            e.printStackTrace();
        }

        return modelAndView;
    }

    // Geração de relatório completo
    @RequestMapping(value = "/gerarrelatoriocompleto", method = RequestMethod.GET)
    public ModelAndView gerarRelatorioCompleto(ModelAndView modelAndView) {
        // Aqui você geraria o relatório completo de todos os funcionários
        // Use o iText para gerar o PDF
        modelAndView.addObject("mensagem", "Relatório completo gerado com sucesso.");
        modelAndView.setViewName("relatorios/selecionarrelatorio");
        return modelAndView;
    }
}
