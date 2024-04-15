package br.com.bancodigital.cdb.usecase;

import br.com.bancodigital.cdb.dao.imp.JdbcTemplateDaoImp;
import br.com.bancodigital.cdb.dto.ApoliceSeguroRequestDto;
import br.com.bancodigital.cdb.model.Cartao;
import br.com.bancodigital.cdb.model.Cliente;
import br.com.bancodigital.cdb.model.StandardResponse;
import br.com.bancodigital.cdb.util.GeracaoUtil;
import br.com.bancodigital.cdb.util.ValidacaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class ApoliceService {
    @Autowired
    private JdbcTemplateDaoImp jdbcTemplateDaoImp;

    public ResponseEntity<?> gerarApolice(ApoliceSeguroRequestDto request)
    {
        if (request.getTipoApolice() == null || request.getSenhaCartao() == null || request.getIdCartao() == null)
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao gerar apólice: Os campos tipoApolice, senhaCartao, e idCartao são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarTipoApolice(request.getTipoApolice().toUpperCase()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao gerar apólice: O valor preenchido no campo tipoApolice é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Cartao cartao = jdbcTemplateDaoImp.buscarCartao(request.getIdCartao());
        double valorAssegurado = 0;
        if (cartao != null)
        {
            if (cartao.getTipoCartao().equalsIgnoreCase("Debito"))
            {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro: Por ser um cartão de debito, você não pode contratar o seguro.")
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (cartao.getSenha() != request.getSenhaCartao())
            {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro: Senha inválida.")
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Cliente cliente = jdbcTemplateDaoImp.buscarCliente(cartao.getClienteCpf());

            double valorSeguro = 0.0;
            if (request.getTipoApolice().equalsIgnoreCase("Viagem")) {
                if (cliente.getTipoCliente().equalsIgnoreCase("PREMIUM")) {
                    // Seguro viagem gratuito para clientes Premium
                    valorAssegurado = 20000.00;
                    valorSeguro = 0.0;
                } else {
                    // Taxa de R$ 50,00 por mês para clientes Comum e Super
                    valorAssegurado = 20000.00;
                    valorSeguro = 50.0;
                    cartao.setFatura(cartao.getFatura() + valorSeguro);
                    cartao.setLimite(cartao.getLimite() - valorSeguro);
                }
            } else if (request.getTipoApolice().equalsIgnoreCase("Fraude")) {
                // Valor fixo de apólice de R$ 5.000,00 para todos os cartões
                valorAssegurado = 5000.0;
            } else {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro ao gerar apólice: Tipo de apólice não reconhecido.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Atualizar a fatura do cartão com o valor do seguro, se aplicável
            cartao.setFatura(cartao.getFatura() + valorSeguro);
            cartao.setLimite(cartao.getLimite() - valorSeguro);
            LocalDate dataEmissao = LocalDate.now();
            try {
                jdbcTemplateDaoImp.gerarApolice(cartao, request.getTipoApolice(), valorAssegurado, dataEmissao);
            } catch (IOException e)
            {
                // Nada.
            }
            return ResponseEntity.ok(StandardResponse.builder().message("Apólice gerada com sucesso.").build());
        }
        else
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao gerar apólice: Cartão não encontrado.")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
