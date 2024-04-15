package br.com.bancodigital.cdb.util;

import br.com.bancodigital.cdb.enums.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidacaoUtil {
    public static boolean validarCpf(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11)
            return false;

        // Verifica se todos os dígitos são iguais, o que torna o CPF inválido
        if (cpf.matches("(\\d)\\1{10}"))
            return false;

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (10 - i);
        }
        int resto = soma % 11;
        int digitoVerificador1 = resto < 2 ? 0 : 11 - resto;

        // Verifica o primeiro dígito verificador
        if (Integer.parseInt(String.valueOf(cpf.charAt(9))) != digitoVerificador1)
            return false;

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (11 - i);
        }
        resto = soma % 11;
        int digitoVerificador2 = resto < 2 ? 0 : 11 - resto;

        // Verifica o segundo dígito verificador
        if (Integer.parseInt(String.valueOf(cpf.charAt(10))) != digitoVerificador2)
            return false;

        return true;
    }

    public static boolean validarDataNascimento(String dataNascimento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate parsedDate = LocalDate.parse(dataNascimento, formatter);

            LocalDate dataAtual = LocalDate.now();
            if(parsedDate.isAfter(dataAtual)) {
                // Data de nascimento no futuro
                return false;
            }

            // Calcular a idade com base na data de nascimento
            int idade = Period.between(parsedDate, dataAtual).getYears();

            // Verificar se a idade é menor que 18 anos
            if (idade < 18) {
                // Cliente menor de 18 anos
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            // Erro ao fazer o parsing da data de nascimento
            return false;
        }
    }


    public static boolean validarTipoCliente(String tipoCliente) {
        try {
            TipoClienteEnum cliente = TipoClienteEnum.valueOf(tipoCliente.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean validarTipoConta(String tipoConta) {
        try {
            TipoContaEnum conta = TipoContaEnum.valueOf(tipoConta.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean validarTipoCartao(String tipoCartao) {
        try {
            TipoCartaoEnum cartao = TipoCartaoEnum.valueOf(tipoCartao.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean validarEstadoCartao(String estadoCartao) {
        try {
            EstadoCartaoEnum estadoCartaoEnum = EstadoCartaoEnum.valueOf(estadoCartao.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    public static boolean validarCep(String cep) {
        cep = cep.replaceAll("[^0-9]", "");

        if (cep.length() != 8)
            return false;

        return true;
    }

    public static boolean validarNome(String nome) {
        String regex = "^[\\p{L} .'-]+$";

        return nome.matches(regex);
    }

    public static boolean validarTipoApolice(String tipoApolice) {
        try {
            TipoApoliceEnum tipoApoliceEnum = TipoApoliceEnum.valueOf(tipoApolice.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
