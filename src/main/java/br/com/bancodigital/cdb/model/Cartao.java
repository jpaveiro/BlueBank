package br.com.bancodigital.cdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cartao")
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="numero_cartao", nullable = false)
    private long numeroCartao;

    @Column(name = "limite", nullable = false)
    private double limite;

    @Column(name = "fatura", nullable = false)
    private double fatura;

    @Column(name = "senha", nullable = false)
    private int senha;

    @Column(name = "cliente_cpf", length = 11, nullable = false)
    private String clienteCpf;

    @Column(name= "tipo_cartao", length = 7, nullable = false)
    private String tipoCartao;

    @Column(name="estado", length = 3, nullable = false)
    private String estado;
}
