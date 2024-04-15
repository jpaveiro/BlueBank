package br.com.bancodigital.cdb.controller;

import br.com.bancodigital.cdb.dto.AlterarSenhaCartaoRequestDto;
import br.com.bancodigital.cdb.dto.CartaoRequestDto;
import br.com.bancodigital.cdb.dto.ClienteRequestDto;
import br.com.bancodigital.cdb.dto.PagarContaCartaoRequestDto;
import br.com.bancodigital.cdb.model.StandardResponse;
import br.com.bancodigital.cdb.usecase.CartaoService;
import br.com.bancodigital.cdb.usecase.ClienteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartao")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);

    @PostMapping("/v1/gerar-cartao")
    public ResponseEntity<?> cadastrarCartao(@RequestBody CartaoRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<StandardResponse> response = cartaoService.inserirNovoCartao(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @PostMapping("/v1/pagar-conta-cartao")
    public ResponseEntity<?> pagarContaComCartao(@RequestBody PagarContaCartaoRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = cartaoService.pagarContaComCartao(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @PostMapping("/v1/alterar-senha-cartao")
    public ResponseEntity<?> alterarSenhaCartao(@RequestBody AlterarSenhaCartaoRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = cartaoService.alterarSenhaCartao(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }

    @GetMapping("/v1/ver-fatura/{numeroCartao}")
    public ResponseEntity<?> verFaturaCartao(@PathVariable long numeroCartao)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = cartaoService.verFatura(numeroCartao);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }
}
