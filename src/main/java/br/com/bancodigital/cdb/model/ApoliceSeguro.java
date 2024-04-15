package br.com.bancodigital.cdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "apoliceseguro")
public class ApoliceSeguro {
    private int idCartao;

    public ApoliceSeguro(String tipoApolice, double valorAssegurado, int idCartao) throws IOException {
        this.idCartao = idCartao;
        FileWriter writer = new FileWriter("apolice_" + tipoApolice.toLowerCase() + ".txt");

        DecimalFormat formatoDecimal = new DecimalFormat("#.00");
        String valorFormatado = formatoDecimal.format(valorAssegurado);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        String dataEmissao = sdf.format(new Date());

        writer.write("Emitido por BlueBank | " + dataEmissao + "\n");
        writer.write("Cartão nº " + idCartao + "\n");
        writer.write("Valor assegurado: " + valorFormatado + "\n");
        writer.write("Tipo: " + tipoApolice + "\n");

        if ("Viagem".equalsIgnoreCase(tipoApolice)) {
            writer.write("Descrição: Proteção global para suas aventuras. Viaje com tranquilidade, sabendo que você está coberto em caso de emergências médicas, perda de bagagem e cancelamentos inesperados.\n");
        } else if ("Fraude".equalsIgnoreCase(tipoApolice)) {
            writer.write("Descrição: Segurança em cada transação. Proteja-se contra atividades fraudulentas, desde roubo de identidade até transações não autorizadas, garantindo que suas finanças e informações pessoais estejam sempre seguras.\n");
        }
        writer.close();
    }
}
