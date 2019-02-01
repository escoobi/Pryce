package pryce.com.pryce;

import com.google.firebase.database.DatabaseReference;

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
/*
            if (nLinha == 135) {
                if (mi.contains("RCod")) {
                    Produtos.descricaoSelect = linha.substring(38, linha.indexOf("</span>"));
                    Produtos.codigoSelect = linha.substring(linha.indexOf(":") + 1, linha.length());
                    Produtos.codigoSelect = Produtos.codigoSelect.substring(0, Produtos.codigoSelect.indexOf(")"));
                    Produtos.codigoSelect = Produtos.codigoSelect.trim();
                } else {
                    if (linha.contains("RvlUnit")) {


                        Produtos.valorSelect = linha.substring(linha.indexOf(";") + 1, linha.indexOf("</span></td>"));
                        Produtos.valorSelect = Produtos.valorSelect.replace(",", ".");

                        mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("produtos");
                        ConsultaProdutos prdt = new ConsultaProdutos();
                        prdt.descricao = Produtos.descricaoSelect;
                        prdt.valor = Produtos.valorSelect;
                        prdt.cidade = Emitente.cidadeSelect;
                        prdt.numero = Emitente.numeroSelect;
                        prdt.logradouro = Emitente.logradouroSelect;
                        prdt.cnpj = Emitente.cnpjSelect;
                        prdt.razao = Emitente.fantasiaSelect;
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
                */
        }
    }
}


