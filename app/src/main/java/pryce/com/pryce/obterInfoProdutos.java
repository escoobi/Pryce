package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;


import static pryce.com.pryce.obterInfoEmitente.cnpjSelect;
import static pryce.com.pryce.obterInfoEmitente.hora;
import static pryce.com.pryce.obterInfoEmitente.data;
import static pryce.com.pryce.obterInfoEmitente.qtditens;


public class obterInfoProdutos {
    public static String descr = null;
    public static String cod = null;
    public static String val = null;
    public String linha = null;

    public void obterItens(BufferedReader br) throws IOException {

        StringBuilder numeros = new StringBuilder();

        try {
           // BufferedReader br = new BufferedReader(new InputStreamReader(caminho.openStream()));


            String minhaLinhaDaXexeca;
            while ((minhaLinhaDaXexeca = br.readLine()) != null) {
                numeros.append(minhaLinhaDaXexeca).append("\n");

            }


            Scanner scanProdutos = new Scanner(numeros.toString());
            int numLinha = 0;
            int produtoLinha = 157;
            int minhaLinhaValor = 159;
            int qtdTotal = produtoLinha + (6 * qtditens);
            while (scanProdutos.hasNextLine()) {
                linha = scanProdutos.nextLine();

                for (int x = 0; x <= qtditens; x++) {
                    if (qtdTotal != numLinha) {
                        // Obter itens
                        if (numLinha == produtoLinha) {

                            descr = linha.substring(38, linha.indexOf("</span>"));
                            linha = linha.replaceAll(" ", "");
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
                if (descr != null && cod != null && val != null) {
                    gravarProdutos gravarProdutos = new gravarProdutos();
                    gravarProdutos.gravarProdutos(descr, val, cod, data, hora, cnpjSelect);
                    descr = null;
                    cod = null;
                    val = null;
                }
                numLinha++;

            }
            scanProdutos.close();
            br.close();

        } catch (Exception localException) {
        }
    }
}
