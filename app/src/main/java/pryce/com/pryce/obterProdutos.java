package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class obterProdutos {

    public static String descr = null;
    public static String cod = null;
    public static String val = null;
    public String linha = null;
    public static int qtditens = 0;
    public static String cnpj = null;
    public BufferedReader br;


    public void carregaProdutos(URL url){
        StringBuilder numeros = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

            }

            Scanner scanProdutos = new Scanner(numeros.toString());
            int numLinha = 0;
            int produtoLinha = 157;
            int minhaLinhaValor = 159;
            int qtdTotal = produtoLinha + (6 * qtditens);

            while (scanProdutos.hasNextLine()) {
                linha = scanProdutos.nextLine();

                // Pega cnpj
                if (numLinha == 147) {
                    linha = linha.substring(6, linha.indexOf("</"));
                    cnpj = linha;

                }


                // Pegar qtd itens
                if (linha.contains("Qtd. total de itens:")) {
                    linha = linha.substring(59, linha.indexOf("</span>"));
                    qtditens = Integer.parseInt(linha);
                }
                for (int x = 0; x <= qtditens; x++) {
                    if (qtdTotal != numLinha) {
                        // Obter itens
                        if (numLinha == produtoLinha) {

                            descr = linha.substring(38, linha.indexOf("</span>"));
                           // linha = linha.replaceAll(" ", "");
                            cod = linha.substring(linha.indexOf(":") + 1, linha.length());
                            cod = cod.substring(0, cod.indexOf(")"));
                            produtoLinha = produtoLinha + 6;

                        }
                        // Obter valor itens
                        if (numLinha == minhaLinhaValor) {

                            val = linha.substring(linha.indexOf("Vl. Unit.:</strong>&nbsp;") + 25, linha.indexOf("</span></td>"));
                            val = val.replace(",", ".");
                            minhaLinhaValor = minhaLinhaValor + 6;

                        }

                    }


                }
                gravarProdutos gravarEssaBuceta = new gravarProdutos();
                gravarEssaBuceta.obterKeyEmitente(cnpj);
                numLinha++;

            }
            scanProdutos.close();

            br.close();


        }
        catch (Exception e){}
    }
}
