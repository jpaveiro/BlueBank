package br.com.bancodigital.cdb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlterarSenhaCartaoRequestDto {
    private Long numeroCartao;
    private Integer senhaAntiga;
    private Integer senhaNova;
}
