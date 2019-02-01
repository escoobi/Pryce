package pryce.com.pryce;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Scanner;




public class obterProdutos {

    public String linha = null;

    private DatabaseReference mDatabaseProtudo;


    public void carregaProdutos() {

        Object element = null;


        try {



            Scanner scanProdutos = new Scanner(element.toString());
            while (scanProdutos.hasNextLine()) {
                linha = scanProdutos.nextLine();
                if (linha.contains("RCod")) {
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
            scanProdutos.close();

        } catch (Exception e) {
        }
    }
}


