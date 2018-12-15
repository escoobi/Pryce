package pryce.com.pryce;

import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class obterProdutos {

    public static String cod = null;
    public static String val = null;
    public String linha = null;


    public static String cnpj = null;
    public BufferedReader br;
    public String keyProduto = null;
    private DatabaseReference mDatabaseProtudo;


    public void carregaProdutos(URL url){
        int qtdTotal = 0;
        StringBuilder numeros = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");
                qtdTotal++;

            }

            Scanner scanProdutos = new Scanner(numeros.toString());
            int numLinha = 0;
            int produtoLinha = 157;
            int minhaLinhaValor = 159;


            while (scanProdutos.hasNextLine()) {
                linha = scanProdutos.nextLine();
                // Pegar qtd itens
            //    if (linha.contains("Qtd. total de itens:")) {
            //        linha = linha.substring(59, linha.indexOf("</span>"));
            //        Produtos.qtdProd = Integer.parseInt(linha);
            //        qtdTotal = produtoLinha + (6 * Produtos.qtdProd);
             //        break;

            //    }
                for (int x = 1; x <= Produtos.qtdProd; x++) {
                        if (qtdTotal != numLinha) {
                            // Obter itens
                            if (numLinha == produtoLinha) {

                                Produtos.descricaoSelect = linha.substring(38, linha.indexOf("</span>"));
                                Produtos.codigoSelect = linha.substring(linha.indexOf(":") + 1, linha.length());
                                Produtos.codigoSelect = Produtos.codigoSelect.substring(0, Produtos.codigoSelect.indexOf(")"));
                                Produtos.codigoSelect = Produtos.codigoSelect.trim();
                                produtoLinha = produtoLinha + 6;

                            }
                            // Obter valor itens
                            if (numLinha == minhaLinhaValor) {

                                Produtos.valorSelect = linha.substring(linha.indexOf("Vl. Unit.:</strong>&nbsp;") + 25, linha.indexOf("</span></td>"));
                                Produtos.valorSelect = Produtos.valorSelect.replace(",", ".");
                                minhaLinhaValor = minhaLinhaValor + 6;

                            }


                        }
                    gravarProdutos gravarProdut = new gravarProdutos();
                    gravarProdut.gravarProdutos(Produtos.descricaoSelect, Produtos.valorSelect, Produtos.codigoSelect);

                    }


                numLinha++;
            }

            br.close();

            scanProdutos.close();




        }
        catch (Exception e){
            String t = "t" + "a" + e.toString();
        }
    }
}
