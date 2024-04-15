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