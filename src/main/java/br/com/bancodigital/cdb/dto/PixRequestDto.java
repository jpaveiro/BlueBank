package br.com.bancodigital.cdb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PixRequestDto {
    private Integer numeroContaOrigem;
    private Integer numeroContaDestino;
    private Integer valorPix;
}
