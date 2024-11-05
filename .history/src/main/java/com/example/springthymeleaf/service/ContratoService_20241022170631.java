package com.example.springthymeleaf.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.contrato.Beneficio;
import com.example.springthymeleaf.model.contrato.Contrato;
import com.example.springthymeleaf.reposiories.ContratoRepository;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TaskScheduler taskScheduler;

    public Contrato save(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public Optional<Contrato> findById(long id) {
        return contratoRepository.findById(id);
    }

    @Scheduled(cron = "0 0 12 * * ?") // Executa todos os dias ao meio-dia
    //@Scheduled(cron = "0 * * * * ?") // Verifica contratos a cada minuto para teste
    public void verificarContratosExpirando() {
        LocalDate seisMesesAFrente = LocalDate.now().plusMonths(6);

        List<Contrato> contratosExpirando = contratoRepository.findByDataFimBefore(seisMesesAFrente);

        if (!contratosExpirando.isEmpty()) {
             // Agenda o envio de e-mail para 5 minutos a partir de agora usando Instant e Duration
            taskScheduler.schedule(() -> enviarNotificacaoManager(contratosExpirando), 
                                    Instant.now().plus(Duration.ofMinutes(1)));
        }
    }

    private void enviarNotificacaoManager(List<Contrato> contratosExpirando) {
        StringBuilder conteudoEmail = new StringBuilder(
            "<h1>StaffTrack - ESTA É UMA MENSAGEM AUTOMÁTICA</h1>" +
            "<p>Os seguintes contratos irão expirar em até 6 meses:</p><br>");

        for (Contrato contrato : contratosExpirando) {

            List<String> nomeDoBeneficio = new ArrayList<>();
            for (Beneficio beneficio : contrato.getBeneficios()) {
                nomeDoBeneficio.add(beneficio.getTipoBeneficio());
            }

            conteudoEmail.append("<b>ID:</b> " + contrato.getId() + "<br>")
            .append("<b>Funcionário:</b> " + contrato.getPessoa().getNome() + "<br>")
            .append("<b>Data de inicio:</b> " + contrato.getDataInicio() + "<br>")
            .append("<b>Data de Término:</b> " + contrato.getDataFim() + "<br>")
            .append("<b>Valor:</b> " + contrato.getValor() + "<br>")
            .append("<b>Tipo:</b> " + contrato.getTipo() + "<br>")
            .append("<b>Benefícios:</b> " + nomeDoBeneficio + "<br>")
            .append("<b>Cargo:</b> " + contrato.getPessoa().toString() + "<br><br>");
        }

        // Envia o e-mail
        emailService.enviarEmailHtml("kauanfer10@gmail.com", "Contratos que irão expirar", conteudoEmail.toString());
    }
}
