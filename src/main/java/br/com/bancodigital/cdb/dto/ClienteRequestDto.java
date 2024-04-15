package br.com.bancodigital.cdb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDto {
    private String cpf;
    private String nome;
    private String dataNascimento;
    private String rua;
    private String numero;
    private String cep;
    private String cidade;
    private String estado;
    private String tipoConta;
    private String tipoCliente;
    private Double saldo;
}
