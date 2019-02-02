package pryce.com.pryce;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class obterProdutos {

    public BufferedReader br;
    private DatabaseReference mDatabaseProtudo;


    public void carregaProdutos(URL url) throws IOException {
        br = new BufferedReader(new InputStreamReader(url.openStream(), "windows-1252"));
        String minhaLinha;
        int nLinha = 0;
        while ((minhaLinha = br.readLine()) != null) {

            nLinha++;
            if (minhaLinha.contains("RCod")) {
                Produtos.descricaoSelect = minhaLinha.substring(38, minhaLinha.indexOf("</span>"));
                Produtos.codigoSelect = minhaLinha.substring(minhaLinha.indexOf(":") + 1, minhaLinha.length());
                Produtos.codigoSelect = Produtos.codigoSelect.substring(0, Produtos.codigoSelect.indexOf(")"));
                Produtos.codigoSelect = Produtos.codigoSelect.trim();
            }
            if (minhaLinha.contains("RvlUnit")) {
                Produtos.valorSelect = minhaLinha.substring(minhaLinha.indexOf(";") + 1, minhaLinha.indexOf("</span></td>"));
                Produtos.valorSelect = Produtos.valorSelect.replace(",", ".");
                mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("produtos");
                ConsultaProdutos prdt = new ConsultaProdutos();
                prdt.descricao = Produtos.descricaoSelect;
                prdt.valor = Produtos.valorSelect;
                prdt.cidade = Emitente.cidadeSelect;
                prdt.numero = Emitente.numeroSelect;
                prdt.logradouro = Emitente.logradouroSelect;
                prdt.cnpj = Emitente.cnpjSelect;
                prdt.fantasia = Emitente.fantasiaSelect;
                prdt.razao = Emitente.razaoSelect;
                prdt.bairro = Emitente.bairroSelect;
                prdt.uf = Emitente.ufSelect;
                prdt.data = Produtos.data;
                prdt.hora = Produtos.hora;
                prdt.lat = Emitente.lat;
                prdt.log = Emitente.log;
                mDatabaseProtudo.push().setValue(prdt);

            }


        }
    }
}


