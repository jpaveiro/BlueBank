# BlueBank - Banco Digital CDB 🏦

## Requisitos minimos
- JDK >= 17.
- Maven.
- Postman (apenas para desenvolvimento).
- PostgreeSQL.
- PGAdmin.

## Observações
- Arquivo do [postman](./postman.json) está na formatação Collection 2.1.
- Todos as informações de teste já estão no arquivo do [postman](./postman.json)

## Uso (Desenvolvimento)
- Clone este repositório ```git clone https://github.com/jpaveiro/BlueBank.git```
- Instale as dependências.
- Inicialize o BancoDigitalCdbApplication.java
- Crie as tabelas SQL.
- Use o postman para acessar as rotas.

## Rotas
1. [POST] - /cliente/v1/cadastrar-cliente
1. [POST] - /cartao/v1/gerar-cartao
1. [GET] - /cliente/v1/ver-saldo/
1. [POST] - /cliente/v1/depositar
1. [POST] - /cliente/v1/sacar
1. [POST] - /cliente/v1/transferencia-pix
1. [POST] - /cartao/v1/pagar-conta-cartão
1. [POST] - /cartao/v1/alterar-senha-cartão
1. [GET] - /cartao/v1/ver-fatura/
1. [POST] - /apolice/v1/gerar-apolice

## Tabelas SQL
```CREATE TABLE Cliente (
	id SERIAL PRIMARY KEY,
	cpf VARCHAR(11) UNIQUE NOT NULL,
	nome VARCHAR(100) NOT NULL,
	data_nascimento DATE NOT NULL,
	rua VARCHAR(100),
	numero VARCHAR(10),
	cep VARCHAR(8),
	cidade VARCHAR(100),
	estado VARCHAR(50),
	tipo_conta VARCHAR(50),
	numero_conta SERIAL UNIQUE,
	saldo NUMERIC (15, 2),
	tipo_cliente VARCHAR(7)
);

CREATE TABLE Cartao (
	id SERIAL PRIMARY KEY,
	numero_cartao BIGINT,
	limite NUMERIC(15, 2),
	fatura NUMERIC (15, 2),
	senha INT,
	cliente_cpf VARCHAR(11) REFERENCES Cliente(cpf),
	tipo_cartao VARCHAR(7),
	estado VARCHAR(3)
);

CREATE TABLE SeguroApolice (
    numero_apolice SERIAL PRIMARY KEY,
	tipo_apolice VARCHAR(15) NOT NULL,
	valor_assegurado NUMERIC(15, 2),
	data_emissao DATE,
	id_cartao BigInt REFERENCES Cartao(id)
);
