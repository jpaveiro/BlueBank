package br.com.bancodigital.cdb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaturaInfo {
    private long numeroCartao;
    private double valorFatura;
}
