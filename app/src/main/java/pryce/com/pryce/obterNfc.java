package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class obterNfc {

    public String linha = null;
    public BufferedReader br;
    public static StringBuilder numerosPublicos;
    public static ArrayList listaHtml;

    public void carregaNfc(URL url) throws IOException {
        Emitente emitente = new Emitente();
        Produtos produto = new Produtos();
        StringBuilder numeros = new StringBuilder();
        listaHtml = new ArrayList();

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream(), "windows-1252"));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

                listaHtml.add(numeros);
            }
            br.close();
            numerosPublicos = numeros;
            Scanner scan = new Scanner(numeros.toString());
            int nLinha = 0;
            while (scan.hasNextLine()) {
                linha = scan.nextLine();
                linha = java.net.URLDecoder.decode(linha, "UTF-8");
                // Pega razão social
                if (linha.contains("u20")) {
                    linha = linha.substring(30, linha.indexOf("</"));
                    emitente.razao = linha;
                    Emitente.razaoSelect = emitente.razao.toUpperCase();
                }
                // Pega cnpj
                if (nLinha == 147) {
                    linha = linha.substring(6, linha.indexOf("</"));
                    emitente.cnpj = linha;
                    Emitente.cnpjSelect = emitente.cnpj;
                }
                // Pega logradouro
                if (nLinha == 148) {

                    linha = linha.substring(18, linha.indexOf(","));
                    emitente.logradouro = new String (linha.getBytes("ISO-8859-1"), "windows-1252");
                    Emitente.logradouroSelect = emitente.logradouro.toUpperCase();

                }
                // Pega numero
                if (nLinha == 149) {
                    linha = linha.substring(2, linha.indexOf(","));
                    emitente.numero = linha;
                    Emitente.numeroSelect = emitente.numero.toUpperCase();

                }
                // Pega bairro
                if (nLinha == 151) {
                    linha = linha.substring(2, linha.indexOf(","));
                    emitente.bairro = linha;
                    Emitente.bairroSelect = emitente.bairro.toUpperCase();
                }
                // Pega cidade
                if (nLinha == 152) {
                    linha = linha.substring(2, linha.indexOf(","));
                    emitente.cidade = linha;
                    Emitente.cidadeSelect = emitente.cidade.toUpperCase();
                }
                // Pega uf
                if (nLinha == 153) {
                    linha = linha.substring(2, linha.indexOf("</"));
                    emitente.uf = linha;
                    Emitente.ufSelect = emitente.uf.toUpperCase();
                }

                // Pegar data emissão
                if (linha.contains("Protocolo de Autoriza")) {
                    produto.data = linha.substring(79, 89);
                    produto.data = produto.data.replace("/", ".");
                    produto.hora = linha.substring(90, 98);
                }

                //Pega Quantidade de Itens
                if (linha.contains("Qtd. total de itens:")) {
                    linha = linha.substring(59, linha.indexOf("</span>"));
                    Produtos.qtdProd = Integer.parseInt(linha);
                }
                nLinha++;
            }

            obterCordenadasEmitente obter = new obterCordenadasEmitente();
            obter.obterLatLog(emitente.logradouro, emitente.bairro, emitente.numero, emitente.cidade, emitente.uf);
            scan.close();
            br.close();
        } catch (Exception localException) {
            String t = "t" + "a" + localException.toString();
        }

    }


}
