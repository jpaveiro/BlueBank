package br.com.bancodigital.cdb.usecase;

import br.com.bancodigital.cdb.dao.imp.JdbcTemplateDaoImp;
import br.com.bancodigital.cdb.dto.AlterarSenhaCartaoRequestDto;
import br.com.bancodigital.cdb.dto.CartaoRequestDto;
import br.com.bancodigital.cdb.dto.ClienteRequestDto;
import br.com.bancodigital.cdb.dto.PagarContaCartaoRequestDto;
import br.com.bancodigital.cdb.enums.EstadoCartaoEnum;
import br.com.bancodigital.cdb.enums.TipoCartaoEnum;
import br.com.bancodigital.cdb.model.*;
import br.com.bancodigital.cdb.util.GeracaoUtil;
import br.com.bancodigital.cdb.util.ValidacaoUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {
    @Autowired
    private JdbcTemplateDaoImp jdbcTemplateDaoImp;

    public ResponseEntity<StandardResponse> inserirNovoCartao(CartaoRequestDto request)
    {
        if (request.getClienteCpf() == null || request.getSenha() == null || request.getTipoCartao() == null)
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao gerar cartão: Os campos clienteCpf, senha e tipoCartao são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarTipoCartao(request.getTipoCartao()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao gerar cartão: O valor preenchido no campo tipo_cartao é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        request.setClienteCpf(request.getClienteCpf().replaceAll("[^0-9]", ""));
        Cliente clienteBuild = jdbcTemplateDaoImp.buscarCliente(request.getClienteCpf());

        Cartao cartaoBuild = Cartao.builder()
                .numeroCartao(GeracaoUtil.gerarNumeroCartao())
                .clienteCpf(request.getClienteCpf())
                .senha(request.getSenha())
                .tipoCartao(request.getTipoCartao().toUpperCase())
                .estado("ON")
                .fatura(0).build();


        if (clienteBuild != null)
        {
            if (clienteBuild.getTipoCliente().equals("PREMIUM")) {
                cartaoBuild.setLimite(10000.00);
            } else if (clienteBuild.getTipoCliente().equals("SUPER")) {
                cartaoBuild.setLimite(5000.00);
                cartaoBuild.setLimite(cartaoBuild.getLimite() - 12.00);
                cartaoBuild.setFatura(cartaoBuild.getFatura() + 8.00);
            } else if (clienteBuild.getTipoCliente().equals("COMUM")) {
                cartaoBuild.setLimite(1000.00);
                cartaoBuild.setLimite(cartaoBuild.getLimite() - 12.00);
                cartaoBuild.setFatura(cartaoBuild.getFatura() + 12.00);
            }

            jdbcTemplateDaoImp.inserirNovoCartao(cartaoBuild);
            return ResponseEntity.ok(StandardResponse.builder().message("Cartão gerado com sucesso.").build());
        }
        else
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao gerar cartão: Cliente não existe.")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<?> pagarContaComCartao(PagarContaCartaoRequestDto request) {
        if (request.getNumeroCartao() == null || request.getSenhaCartao() == null || request.getValorConta() == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao pagar conta: numeroCartao, senhaCartao e valorConta são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Cartao cartao = jdbcTemplateDaoImp.buscarCartao(request.getNumeroCartao());
        if (cartao == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: O cartão não foi encontrado.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Cliente cliente = jdbcTemplateDaoImp.buscarCliente(cartao.getClienteCpf());
        if (cliente == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: O cliente não foi encontrado.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!request.getSenhaCartao().equals(cartao.getSenha())) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: Senha do cartão incorreta.")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        double valorConta = request.getValorConta();

        if (cartao.getTipoCartao().equalsIgnoreCase("credito")) {
            double totalGastoMes = cartao.getFatura() + valorConta;

            if (totalGastoMes > cartao.getLimite() * 0.8) {
                double taxaUtilizacao = totalGastoMes * 0.05;
                valorConta += taxaUtilizacao;
            }

            if (valorConta > cartao.getLimite() - cartao.getFatura()) {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro: O cartão não tem limite disponível para isso.")
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else if (cartao.getTipoCartao().equalsIgnoreCase("debito")) {
            double limiteDebito = cartao.getLimite() * 0.25;

            if (valorConta > limiteDebito) {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro: O valor da transação excede o limite diário do cartão de débito.")
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }

        jdbcTemplateDaoImp.pagarContaComCartao(request, cartao, valorConta);

        StandardResponse response = StandardResponse.builder()
                .message("Sucesso: A conta foi paga.")
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> alterarSenhaCartao(AlterarSenhaCartaoRequestDto request) {
        if (request.getNumeroCartao() == null || request.getSenhaAntiga() == null || request.getSenhaNova() == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao alterar senha: numeroCartao, senhaAntiga e senhaNova são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Cartao cartao = jdbcTemplateDaoImp.buscarCartao(request.getNumeroCartao());
        if (cartao == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: O cartão não foi encontrado.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!request.getSenhaAntiga().equals(cartao.getSenha())) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: Senha antiga incorreta.")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        jdbcTemplateDaoImp.alterarSenhaCartao(request, cartao);

        StandardResponse response = StandardResponse.builder()
                .message("Sucesso: A senha foi alterada.")
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> verFatura(long numeroCartao)
    {
        Cartao cartao = jdbcTemplateDaoImp.buscarCartao(numeroCartao);
        if (cartao == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: O cartão não foi encontrado.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        FaturaInfo response = FaturaInfo.builder()
                .numeroCartao(cartao.getNumeroCartao())
                .valorFatura(cartao.getFatura())
                .build();

        return ResponseEntity.ok(response);
    }

}
