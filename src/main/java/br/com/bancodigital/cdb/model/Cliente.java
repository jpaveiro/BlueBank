package br.com.bancodigital.cdb.model;

import lombok.*;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name="numero_conta")
    private Integer numeroConta;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 255)
    private String rua;

    @Column(length = 10)
    private String numero;

    @Column(nullable = false, length = 8)
    private String cep;

    @Column(nullable = false, length = 255)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false, name= "tipo_cliente", length = 7) // Comum, Super, Premium
    private String tipoCliente;

    @Column(nullable = false, name = "tipo_conta", length = 50) // Corrente, Poupança
    private String tipoConta;

    @Column(nullable = false)
    private double saldo;
}
