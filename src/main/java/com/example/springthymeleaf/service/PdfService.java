package com.example.springthymeleaf.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.contrato.Contrato;
import com.example.springthymeleaf.model.contrato.Beneficio;
import com.example.springthymeleaf.model.motorista.Motorista;
import com.example.springthymeleaf.model.vendedor.GerenteVendas;
import com.example.springthymeleaf.model.vendedor.Vendas;
import com.example.springthymeleaf.model.vendedor.Vendedor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

    public byte[] gerarRelatorioFuncionario(Pessoa funcionario) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(); // Criação do documento

        // Criação do PdfWriter com o ByteArrayOutputStream
        PdfWriter.getInstance(document, out);

        document.open(); // Abre o documento para adição de conteúdo

        // Fonte em negrito
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Informações genéricas da classe Pessoa
        document.add(new Paragraph("- Relatório do Funcionário", boldFont));

        // Adicionando cada campo em negrito e os valores normais separadamente
        document.add(new Paragraph("Nome: ", boldFont));
        document.add(new Paragraph(funcionario.getNome(), regularFont));

        document.add(new Paragraph("Data de Nascimento: ", boldFont));
        document.add(new Paragraph(funcionario.getDataNascimento().toString(), regularFont));

        document.add(new Paragraph("Endereço: ", boldFont));
        document.add(new Paragraph(funcionario.getRua() + "-" + funcionario.getCep(), regularFont));

        if (funcionario.getListaTelefones().get(0) == null) {
            document.add(new Paragraph("Telefone principal: ", boldFont));
            document.add(new Paragraph("Não informado", regularFont));

        } else {
            document.add(new Paragraph("Telefone principal: ", boldFont));
            document.add(new Paragraph(funcionario.getListaTelefones().get(0).getNumero(), regularFont));
        }

        // Verifica se o funcionário é um Vendedor
        if (funcionario instanceof Vendedor) {
            Vendedor vendedor = (Vendedor) funcionario;
            LocalDate agora = LocalDate.now();

            // Filtra as vendas do mês atual
            List<Vendas> vendasDoMesAtual = vendedor.getVendas().stream()
                    .filter(venda -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(venda.getDataVenda());
                        int anoVenda = cal.get(Calendar.YEAR);
                        int mesVenda = cal.get(Calendar.MONTH) + 1;
                        return anoVenda == agora.getYear() && mesVenda == agora.getMonthValue();
                    })
                    .collect(Collectors.toList());

            // Calcula o total das vendas e a comissão do mês
            Double totalDasVendas = 0.0;
            Double totalEmComissao = 0.0;
            for (Vendas vendas : vendasDoMesAtual) {
                totalDasVendas += vendas.getValorVenda();
                totalEmComissao += (vendas.getComissaoVenda() / 100) * vendas.getValorVenda();
            }

            // Verifica se a meta de vendas foi batida
            String metaBatida = totalDasVendas >= vendedor.getMetaVendas() ? "Sim" : "Não";

            document.add(new Paragraph("Meta de vendas mensais: ", boldFont));
            document.add(new Paragraph(vendedor.getMetaVendas().toString(), regularFont));
            document.add(new Paragraph("Meta batida: ", boldFont));
            document.add(new Paragraph(metaBatida, regularFont));
            document.add(new Paragraph("Comissão do mês: ", boldFont));
            document.add(new Paragraph(totalEmComissao.toString() + "R$", regularFont));

            // Cria a tabela com 5 colunas: Descrição do produto, Quantidade, Valor do
            // produto, Valor da venda, Comissão
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Cabeçalhos das colunas em negrito
            table.addCell(new Paragraph("Descrição do produto", boldFont));
            table.addCell(new Paragraph("Quantidade", boldFont));
            table.addCell(new Paragraph("Valor do produto", boldFont));
            table.addCell(new Paragraph("Valor da venda", boldFont));
            table.addCell(new Paragraph("Comissão", boldFont));

            // Adiciona as vendas na tabela
            for (Vendas venda : vendasDoMesAtual) {
                table.addCell(new Paragraph(venda.getDescricaoProduto(), regularFont));
                table.addCell(new Paragraph(String.valueOf(venda.getQuantidadeProduto()), regularFont));
                table.addCell(new Paragraph(String.valueOf(venda.getValorProduto()), regularFont));
                table.addCell(new Paragraph(String.valueOf(venda.getValorVenda()), regularFont));
                table.addCell(new Paragraph(String.valueOf(venda.getComissaoVenda() + "%"), regularFont));
            }

            document.add(new Paragraph("Vendas feitas no mês:\n\n", boldFont));
            document.add(table);
            document.add(new Paragraph("Total em vendas: ", boldFont));
            document.add(new Paragraph(totalDasVendas.toString() + "\n\n", regularFont));
        }

        // Adiciona as informações do Contrato, se o funcionário tiver um
        Contrato contrato = funcionario.getContrato(); // Acesse o contrato relacionado
        if (contrato != null) {
            document.add(new Paragraph("- Informações do Contrato", boldFont));

            document.add(new Paragraph("Número do Contrato: ", boldFont));
            document.add(new Paragraph(contrato.getNumeroContrato(), regularFont));

            document.add(new Paragraph("Tipo do Contrato: ", boldFont));
            document.add(new Paragraph(contrato.getTipo(), regularFont));

            document.add(new Paragraph("Valor do Contrato: ", boldFont));
            document.add(new Paragraph(contrato.getValor(), regularFont));

            document.add(new Paragraph("Data de Início: ", boldFont));
            document.add(new Paragraph(contrato.getDataInicio().toString(), regularFont));

            document.add(new Paragraph("Data de Fim: ", boldFont));
            if (contrato.getDataFim() != null) {
                document.add(new Paragraph(contrato.getDataFim().toString(), regularFont));
            } else {
                document.add(new Paragraph("Contrato sem data de término", regularFont));
            }

            // Adiciona uma tabela com as informações dos benefícios
            if (!contrato.getBeneficios().isEmpty()) {
                PdfPTable beneficioTable = new PdfPTable(4);
                beneficioTable.setWidthPercentage(100);

                // Cabeçalhos da tabela de benefícios
                beneficioTable.addCell(new Paragraph("Tipo de Benefício", boldFont));
                beneficioTable.addCell(new Paragraph("Descrição", boldFont));
                beneficioTable.addCell(new Paragraph("Valor", boldFont));
                beneficioTable.addCell(new Paragraph("Status", boldFont));

                // Adiciona os benefícios
                for (Beneficio beneficio : contrato.getBeneficios()) {
                    beneficioTable.addCell(new Paragraph(beneficio.getTipoBeneficio(), regularFont));
                    beneficioTable.addCell(new Paragraph(beneficio.getDescricao(), regularFont));
                    beneficioTable.addCell(new Paragraph(
                            beneficio.getValor() != null ? beneficio.getValor() + "R$" : "Sem valor", regularFont));
                    beneficioTable.addCell(new Paragraph(beneficio.isAtivo() ? "Ativo" : "Inativo", regularFont));
                }

                document.add(new Paragraph("Benefícios do Contrato:\n\n", boldFont));
                document.add(beneficioTable);
            }
        }

        document.close();
        return out.toByteArray();
    }
}
