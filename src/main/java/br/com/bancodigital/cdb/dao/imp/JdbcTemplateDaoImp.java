package br.com.bancodigital.cdb.dao.imp;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import br.com.bancodigital.cdb.dto.AlterarSenhaCartaoRequestDto;
import br.com.bancodigital.cdb.dto.CartaoRequestDto;
import br.com.bancodigital.cdb.dto.PagarContaCartaoRequestDto;
import br.com.bancodigital.cdb.model.ApoliceSeguro;
import br.com.bancodigital.cdb.model.Cartao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import br.com.bancodigital.cdb.dao.IJdbcTemplateDao;
import br.com.bancodigital.cdb.model.Cliente;

@Service
public class JdbcTemplateDaoImp implements IJdbcTemplateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean clienteExiste(Cliente cliente) {
        @SuppressWarnings("deprecation")
        List<Cliente> clientes = jdbcTemplate.query(
                "SELECT * FROM Cliente WHERE cpf = ?",
                new Object[]{cliente.getCpf()},
                (rs, rowNum) -> {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setCpf(rs.getString("cpf"));
                    return c;
                });

        if (!clientes.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Cliente buscarCliente(String cpf) {
        List<Cliente> clientes = jdbcTemplate.query(
                "SELECT * FROM Cliente WHERE cpf = ?",
                new Object[]{cpf},
                (rs, rowNum) -> {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setNumeroConta(rs.getInt("numero_conta"));
                    cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    cliente.setRua(rs.getString("rua"));
                    cliente.setNumero(rs.getString("numero"));
                    cliente.setCep(rs.getString("cep"));
                    cliente.setCidade(rs.getString("cidade"));
                    cliente.setEstado(rs.getString("estado"));
                    cliente.setTipoCliente(rs.getString("tipo_cliente"));
                    cliente.setTipoConta(rs.getString("tipo_conta"));
                    cliente.setSaldo(rs.getDouble("saldo"));

                    return cliente;
                });

        if (!clientes.isEmpty()) {
            return clientes.get(0);
        } else {
            return null;
        }
    }

    public Cartao buscarCartao(Long numeroCartao) {
        List<Cartao> cartoes = jdbcTemplate.query(
                "SELECT * FROM Cartao WHERE numero_cartao = ?",
                new Object[]{numeroCartao},
                (rs, rowNum) -> {
                    Cartao cartao = new Cartao();
                    cartao.setId(rs.getInt("id")); // Corrigido para getInt
                    cartao.setNumeroCartao(rs.getLong("numero_cartao"));
                    cartao.setLimite(rs.getDouble("limite")); // Corrigido para getBigDecimal
                    cartao.setFatura(rs.getDouble("fatura")); // Corrigido para getBigDecimal
                    cartao.setSenha(rs.getInt("senha"));
                    cartao.setClienteCpf(rs.getString("cliente_cpf"));
                    cartao.setTipoCartao(rs.getString("tipo_cartao"));
                    cartao.setEstado(rs.getString("estado"));

                    return cartao;
                });

        if (!cartoes.isEmpty()) {
            return cartoes.get(0);
        } else {
            return null;
        }
    }

    public Cartao buscarCartao(int idCartao) {
        List<Cartao> cartoes = jdbcTemplate.query(
                "SELECT * FROM Cartao WHERE id = ?",
                new Object[]{idCartao},
                (rs, rowNum) -> {
                    Cartao cartao = new Cartao();
                    cartao.setId(rs.getInt("id")); // Corrigido para getInt
                    cartao.setNumeroCartao(rs.getLong("numero_cartao"));
                    cartao.setLimite(rs.getDouble("limite")); // Corrigido para getBigDecimal
                    cartao.setFatura(rs.getDouble("fatura")); // Corrigido para getBigDecimal
                    cartao.setSenha(rs.getInt("senha"));
                    cartao.setClienteCpf(rs.getString("cliente_cpf"));
                    cartao.setTipoCartao(rs.getString("tipo_cartao"));
                    cartao.setEstado(rs.getString("estado"));

                    return cartao;
                });

        if (!cartoes.isEmpty()) {
            return cartoes.get(0);
        } else {
            return null;
        }
    }

    public Cliente buscarCliente(int numeroConta) {
        List<Cliente> clientes = jdbcTemplate.query(
                "SELECT * FROM Cliente WHERE numero_conta = ?",
                new Object[]{numeroConta},
                (rs, rowNum) -> {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setNumeroConta(rs.getInt("numero_conta"));
                    cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    cliente.setRua(rs.getString("rua"));
                    cliente.setNumero(rs.getString("numero"));
                    cliente.setCep(rs.getString("cep"));
                    cliente.setCidade(rs.getString("cidade"));
                    cliente.setEstado(rs.getString("estado"));
                    cliente.setTipoCliente(rs.getString("tipo_cliente"));
                    cliente.setTipoConta(rs.getString("tipo_conta"));
                    cliente.setSaldo(rs.getDouble("saldo"));

                    return cliente;
                });

        if (!clientes.isEmpty()) {
            return clientes.get(0);
        } else {
            return null;
        }
    }

    public void inserirNovoCliente(Cliente cliente)
    {

        String sql = "INSERT INTO Cliente (cpf, nome, data_nascimento, rua, numero, cep, cidade, estado, tipo_conta, saldo, tipo_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) preparedStatement -> {
            preparedStatement.setString(1, cliente.getCpf());
            preparedStatement.setString(2, cliente.getNome());
            preparedStatement.setDate(3, Date.valueOf(cliente.getDataNascimento()));
            preparedStatement.setString(4, cliente.getRua());
            preparedStatement.setString(5, cliente.getNumero());
            preparedStatement.setString(6, cliente.getCep());
            preparedStatement.setString(7, cliente.getCidade());
            preparedStatement.setString(8, cliente.getEstado());
            preparedStatement.setString(9, cliente.getTipoConta().toUpperCase());
            preparedStatement.setFloat(10, (float) cliente.getSaldo());
            preparedStatement.setString(11, cliente.getTipoCliente().toUpperCase());
            preparedStatement.execute();
            return null;
        });
    }

    public void gerarApolice(Cartao cartao, String tipoApolice, double valorAssegurado, LocalDate dataEmissao) throws IOException {
        String sql = "INSERT INTO SeguroApolice (tipo_apolice, valor_assegurado, data_emissao, id_cartao) VALUES (?, ?, ?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) preparedStatement -> {
            preparedStatement.setString(1, tipoApolice.toUpperCase());
            preparedStatement.setDouble(2, valorAssegurado);
            preparedStatement.setDate(3, Date.valueOf(dataEmissao));
            preparedStatement.setLong(4, cartao.getId());
            preparedStatement.execute();
            return null;
        });

        ApoliceSeguro apoliceSeguro = new ApoliceSeguro(tipoApolice, valorAssegurado, cartao.getId());
    }


    public void inserirNovoCartao(Cartao cartao)
    {
        String sql = "INSERT INTO Cartao (numero_cartao, limite, fatura, senha, cliente_cpf, tipo_cartao, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) preparedStatement -> {
            preparedStatement.setLong(1, cartao.getNumeroCartao());
            preparedStatement.setDouble(2, cartao.getLimite());
            preparedStatement.setDouble(3, cartao.getFatura());
            preparedStatement.setInt(4, cartao.getSenha());
            preparedStatement.setString(5, cartao.getClienteCpf());
            preparedStatement.setString(6, cartao.getTipoCartao());
            preparedStatement.setString(7, cartao.getEstado());
            preparedStatement.execute();
            return null;
        });
    }

    @Transactional
    public void depositarSaldo(int numeroConta, double valorDeposito)
    {
        Cliente cliente = buscarCliente(numeroConta);
        if (cliente != null) {
            double saldoAtual = cliente.getSaldo();
            double novoSaldo = saldoAtual + valorDeposito;

            String sql = "UPDATE Cliente SET saldo = ? WHERE numero_conta = ?";
            jdbcTemplate.update(sql, novoSaldo, numeroConta);
        }
    }

    @Transactional
    public void sacarValor(int numeroConta, double valorSaque)
    {
        Cliente cliente = buscarCliente(numeroConta);
        if (cliente != null) {
            double saldoAtual = cliente.getSaldo();
            double novoSaldo = saldoAtual - valorSaque;

            String sql = "UPDATE Cliente SET saldo = ? WHERE numero_conta = ?";
            jdbcTemplate.update(sql, novoSaldo, numeroConta);
        }
    }

    @Transactional
    public void transferenciaPix(int numeroContaOrigem, int numeroContaDestino, double valorTransferencia)
    {
        Cliente clienteOrigem = buscarCliente(numeroContaOrigem);
        Cliente clienteDestino = buscarCliente(numeroContaOrigem);

        if (clienteOrigem != null && clienteDestino != null)
        {
            double saldoAtualOrigem = clienteOrigem.getSaldo();
            double novoSaldoOrigem = saldoAtualOrigem - valorTransferencia;
            double saldoAtualDestino = clienteDestino.getSaldo();
            double novoSaldoDestino = saldoAtualDestino + valorTransferencia;

            String sql = "UPDATE Cliente SET saldo = ? WHERE numero_conta = ?";
            jdbcTemplate.update(sql, novoSaldoOrigem, numeroContaOrigem);
            jdbcTemplate.update(sql, novoSaldoDestino, numeroContaDestino);
        }
    }

    @Transactional
    public void pagarContaComCartao(PagarContaCartaoRequestDto request, Cartao cartao, double valorTransferencia) {
        if (request != null && cartao != null) {
            double faturaAtual = cartao.getFatura();
            double novoFatura = faturaAtual + valorTransferencia;
            double novoLimite = cartao.getLimite() - valorTransferencia;

            String sqlUpdateCartao = "UPDATE Cartao SET fatura = ?, limite = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdateCartao, novoFatura, novoLimite, cartao.getId());
        }
    }

    @Transactional
    public void alterarSenhaCartao(AlterarSenhaCartaoRequestDto request, Cartao cartao) {
        if (request != null && cartao != null) {

            String sqlUpdateCartao = "UPDATE Cartao SET senha = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdateCartao, request.getSenhaNova(), cartao.getId());
        }
    }
}
