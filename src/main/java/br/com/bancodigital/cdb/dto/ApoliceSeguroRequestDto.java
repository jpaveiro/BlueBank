package br.com.bancodigital.cdb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApoliceSeguroRequestDto {
    private String tipoApolice;
    private Integer idCartao;
    private Integer senhaCartao;
}
