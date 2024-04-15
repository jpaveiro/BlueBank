package br.com.bancodigital.cdb.controller;

import br.com.bancodigital.cdb.dto.ClienteRequestDto;
import br.com.bancodigital.cdb.dto.DepositoSaqueRequestDto;
import br.com.bancodigital.cdb.dto.PixRequestDto;
import br.com.bancodigital.cdb.model.StandardResponse;
import br.com.bancodigital.cdb.usecase.ClienteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);

    @PostMapping("/v1/cadastrar-cliente")
    public ResponseEntity<?> cadastrarCliente(@RequestBody ClienteRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<StandardResponse> response = clienteService.inserirNovoCliente(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @PostMapping("/v1/depositar")
    public ResponseEntity<?> depositar(@RequestBody DepositoSaqueRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = clienteService.depositar(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @PostMapping("/v1/sacar")
    public ResponseEntity<?> saque(@RequestBody DepositoSaqueRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = clienteService.saque(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @PostMapping("/v1/transferencia-pix")
    public ResponseEntity<?> transferenciaPix(@RequestBody PixRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = clienteService.transferenciaPix(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @GetMapping("/v1/ver-saldo/{numeroConta}")
    public ResponseEntity<?> verSaldo(@PathVariable int numeroConta)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = clienteService.getSaldo(numeroConta);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }
}
