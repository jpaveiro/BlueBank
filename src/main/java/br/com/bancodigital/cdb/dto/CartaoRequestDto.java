package br.com.bancodigital.cdb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequestDto {
    private Double limite;
    private Double fatura;
    private Integer senha;
    private String clienteCpf;
    private String tipoCartao;
    private String estado;
}
