package br.com.bancodigital.cdb.controller;

import br.com.bancodigital.cdb.dto.ApoliceSeguroRequestDto;
import br.com.bancodigital.cdb.model.StandardResponse;
import br.com.bancodigital.cdb.usecase.ApoliceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apolice")
public class ApoliceController {
    @Autowired
    private ApoliceService apoliceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);

    @PostMapping("/v1/gerar-apolice")
    public ResponseEntity<?> gerarApolice(@RequestBody ApoliceSeguroRequestDto request)
    {
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> response = apoliceService.gerarApolice(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LOGGER.info("Tempo decorrido: " + elapsedTime + " milissegundos.");
        return response;
    }
}
