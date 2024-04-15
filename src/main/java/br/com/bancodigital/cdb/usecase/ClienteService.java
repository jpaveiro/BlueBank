package br.com.bancodigital.cdb.usecase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.bancodigital.cdb.dto.DepositoSaqueRequestDto;
import br.com.bancodigital.cdb.dto.PixRequestDto;
import br.com.bancodigital.cdb.model.ClienteSaldoInfo;
import br.com.bancodigital.cdb.model.StandardResponse;
import br.com.bancodigital.cdb.util.ValidacaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.bancodigital.cdb.dao.imp.JdbcTemplateDaoImp;
import br.com.bancodigital.cdb.dto.ClienteRequestDto;
import br.com.bancodigital.cdb.model.Cliente;
import org.springframework.validation.ValidationUtils;

@Service
public class ClienteService {
    @Autowired
    private JdbcTemplateDaoImp jdbcTemplateDaoImp;

    public ResponseEntity<StandardResponse> inserirNovoCliente(ClienteRequestDto request) {
        if (request.getCpf() == null || request.getNome() == null || request.getDataNascimento() == null ||
                request.getRua() == null || request.getNumero() == null || request.getCep() == null ||
                request.getCidade() == null || request.getEstado() == null || request.getTipoCliente() == null ||
                request.getTipoConta() == null)
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: Os campos cpf, nome, dataNascimento, rua, numero, cep, cidade, estado, tipoCliente e tipoConta são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarNome(request.getNome()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: O valor preenchido no campo nome é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarCpf(request.getCpf()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: O valor preenchido no campo cpf é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));

        if (!ValidacaoUtil.validarDataNascimento(request.getDataNascimento()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: O valor preenchido no campo dataNascimento é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarCep(request.getCep()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: O valor preenchido no campo CEP é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarTipoCliente(request.getTipoCliente()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: O valor preenchido no campo tipoCliente é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!ValidacaoUtil.validarTipoConta(request.getTipoConta()))
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: O valor preenchido no campo tipoConta é inválido.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dataNascimento = LocalDate.parse(request.getDataNascimento(), formatter);

        Cliente build = Cliente.builder()
                .cpf(request.getCpf())
                .nome(request.getNome())
                .dataNascimento(dataNascimento)
                .rua(request.getRua())
                .numero(request.getNumero())
                .cep(request.getCep())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .saldo(0)
                .tipoCliente(request.getTipoCliente())
                .tipoConta(request.getTipoConta().toUpperCase()).build();

        if (!jdbcTemplateDaoImp.clienteExiste(build))
        {
            jdbcTemplateDaoImp.inserirNovoCliente(build);
            return ResponseEntity.ok(StandardResponse.builder().message("Cliente cadastrado com sucesso.").build());
        } else {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao cadastrar cliente: Cliente já existe.")
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    public ResponseEntity<?> getSaldo(int numeroConta)
    {
        Cliente cliente = jdbcTemplateDaoImp.buscarCliente(numeroConta);
        if (cliente != null) {
            ClienteSaldoInfo response = ClienteSaldoInfo.builder()
                .numeroConta(cliente.getNumeroConta())
                .cpf(cliente.getCpf())
                .nome(cliente.getNome())
                .saldo(cliente.getSaldo()).build();

            return ResponseEntity.ok(response);
        } else {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: Cliente não encontrado").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    public ResponseEntity<?> depositar(DepositoSaqueRequestDto request)
    {
        if (request.getNumeroConta() == null || request.getValor() == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao depositar: numeroConta e valor são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Cliente cliente = jdbcTemplateDaoImp.buscarCliente(request.getNumeroConta());
        if (cliente != null) {

            jdbcTemplateDaoImp.depositarSaldo(request.getNumeroConta(), request.getValor());
            StandardResponse response = StandardResponse.builder()
                    .message("Sucesso: Depósito feito com sucesso.")
                    .build();

            return ResponseEntity.ok(response);
        } else {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: Cliente não encontrado").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<?> saque(DepositoSaqueRequestDto request)
    {
        if (request.getNumeroConta() == null || request.getValor() == null) {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao realizar saque: numeroConta e valor são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Cliente cliente = jdbcTemplateDaoImp.buscarCliente(request.getNumeroConta());
        if (cliente != null) {

            if (request.getValor() > cliente.getSaldo()) {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro: O cliente não tem saldo para isso.").build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            jdbcTemplateDaoImp.sacarValor(request.getNumeroConta(), request.getValor());
            StandardResponse response = StandardResponse.builder()
                    .message("Sucesso: Saque feito com sucesso.")
                    .build();

            return ResponseEntity.ok(response);
        } else {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: Cliente não encontrado").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<?> transferenciaPix(PixRequestDto request)
    {
        if (request.getNumeroContaDestino() == null || request.getNumeroContaOrigem() == null || request.getValorPix() == null)
        {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro ao realizar transferência PIX: numeroContaDestino, numeroContaOrigem e valorPix são obrigatórios.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Cliente clienteOrigem = jdbcTemplateDaoImp.buscarCliente(request.getNumeroContaOrigem());
        Cliente clienteDestino = jdbcTemplateDaoImp.buscarCliente(request.getNumeroContaDestino());

        if (clienteOrigem != null && clienteDestino != null) {

            if (request.getValorPix() > clienteOrigem.getSaldo()) {
                StandardResponse response = StandardResponse.builder()
                        .message("Erro: O cliente de origem não tem saldo para isso.").build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            jdbcTemplateDaoImp.transferenciaPix(clienteOrigem.getNumeroConta(), clienteDestino.getNumeroConta(), request.getValorPix());
            StandardResponse response = StandardResponse.builder()
                    .message("Sucesso: Transferência PIX efetuada com sucesso.")
                    .build();

            return ResponseEntity.ok(response);
        } else {
            StandardResponse response = StandardResponse.builder()
                    .message("Erro: Um dos clientes não foi encontrado.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
