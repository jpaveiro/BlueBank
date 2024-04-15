package br.com.bancodigital.cdb.util;

import java.util.Random;

public class GeracaoUtil {

    public static long gerarNumeroCartao() {
        Random random = new Random();
        long numero = 0;

        // Gera 16 dígitos aleatórios para o número do cartão
        for (int i = 0; i < 16; i++) {
            numero = numero * 10 + random.nextInt(10);
        }

        return numero;
    }
}
