package br.com.bancodigital.cdb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagarContaCartaoRequestDto {
    private Long numeroCartao;
    private Integer senhaCartao;
    private Double valorConta;
}
