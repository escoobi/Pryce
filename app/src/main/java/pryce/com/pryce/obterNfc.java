package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import static pryce.com.pryce.obterCordenadasEmitente.lat;
import static pryce.com.pryce.obterCordenadasEmitente.log;


public class obterNfc {

    public static String data = null;
    public static String hora = null;
    public static String cnpjSelect = null;

    public static String razao = null;
    public static String cnpj = null;
    public static String logradouro = null;
    public static String numero = null;
    public static String bairro = null;
    public static String cidade = null;
    public static String uf = null;
    public String linha = null;

    /*public static String descr = null;
    public static String cod = null;
    public static String val = null;*/
    public BufferedReader br;




    public void carregaNfc(URL url) throws IOException {
        StringBuilder numeros = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

            }

            Scanner scan = new Scanner(numeros.toString());
            int nLinha = 0;
            while (scan.hasNextLine()) {
                linha = scan.nextLine();
                // Pega razão social
                if (linha.contains("u20")) {
                    linha = linha.substring(30, linha.indexOf("</"));
                    razao = linha;
                }
                // Pega cnpj
                if (nLinha == 147) {
                    linha = linha.substring(6, linha.indexOf("</"));
                    cnpj = linha;
                    cnpjSelect = linha;
                }
                // Pega logradouro
                if (nLinha == 148) {
                    linha = linha.substring(18, linha.indexOf(","));
                    logradouro = linha;
                }
                // Pega numero
                if (nLinha == 149) {
                    linha = linha.substring(2, linha.indexOf(","));
                    numero = linha;
                }
                // Pega bairro
                if (nLinha == 151) {
                    linha = linha.substring(2, linha.indexOf(","));
                    bairro = linha;
                }
                // Pega cidade
                if (nLinha == 152) {
                    linha = linha.substring(2, linha.indexOf(","));
                    cidade = linha;
                }
                // Pega uf
                if (nLinha == 153) {
                    linha = linha.substring(2, linha.indexOf("</"));
                    uf = linha;
                }

                // Pegar data emissão
                if (linha.contains("Protocolo de Autoriza")) {
                    data = linha.substring(79, 89);
                    data = data.replace("/", ".");
                    hora = linha.substring(90, 98);
                }

                nLinha++;
            }
            if (razao != null && cnpj != null && logradouro != null && numero != null && bairro != null && cidade != null && uf != null) {

                while (lat == null & log == null) {
                    obterCordenadasEmitente obterCordenadasEmitente = new obterCordenadasEmitente();
                    obterCordenadasEmitente.obterLatLog(logradouro, bairro, numero, cidade, uf);
                }

            }
            scan.close();
/*
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

                    gravarProdutos gravarEssaBuceta = new gravarProdutos();
                    gravarEssaBuceta.obterKeyEmitente(cnpj);
                }

                numLinha++;

            }
            scanProdutos.close();
*/
            br.close();

        } catch (Exception localException) {
        }
    }

}
