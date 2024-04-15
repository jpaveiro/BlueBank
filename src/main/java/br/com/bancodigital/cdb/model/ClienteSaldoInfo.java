package br.com.bancodigital.cdb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteSaldoInfo {

    private int numeroConta;
    private String cpf;
    private String nome;
    private double saldo;

}