package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;



public class obterInfoEmitente {

    public static String data = null;
    public static String hora = null;
    public static String cnpjSelect = null;
    public static int qtditens = 0;
    public static String razao = null;
    public static String cnpj = null;
    public static String logradouro = null;
    public static String numero = null;
    public static String bairro = null;
    public static String cidade = null;
    public static String uf = null;




    public void obterEmitente(URL url) throws IOException {
        StringBuilder numeros = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

            }

            Scanner scan = new Scanner(numeros.toString());
            int nLinha = 0;
            while (scan.hasNextLine()) {
                String linha = scan.nextLine();
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
                // Pegar qtd itens
                if (linha.contains("Qtd. total de itens:")) {
                    linha = linha.substring(59, linha.indexOf("</span>"));
                    qtditens = Integer.parseInt(linha);
                }
                // Pegar data emissão
                if (linha.contains("Protocolo de Autoriza")) {
                    data = linha.substring(79, 89);
                    data = data.replace("/", ".");
                    hora = linha.substring(90, 98);
                }
                nLinha++;

                if (razao != null && cnpj != null && logradouro != null && numero != null && bairro != null
                        && cidade != null && uf != null) {
                    //obter lat e log via endereço
                    obterCordenadasEmitente obterCordenadasEmitente = new obterCordenadasEmitente();
                    obterCordenadasEmitente.obterLatLog(logradouro, bairro, numero, cidade, uf);
                    //inserir e atualizar emitente
                    gravar gravar = new gravar();
                    gravar.gravarEmitente(razao, cnpj, logradouro, bairro, numero,cidade, uf, pryce.com.pryce.obterCordenadasEmitente.lat, pryce.com.pryce.obterCordenadasEmitente.log);


                    razao = null;
                    cnpj = null;
                    logradouro = null;
                    numero = null;
                    bairro = null;
                    cidade = null;
                    uf = null;
                    data = null;
                    hora = null;
                    pryce.com.pryce.obterCordenadasEmitente.lat = null;
                    pryce.com.pryce.obterCordenadasEmitente.log = null;
                }
            }
            scan.close();
            br.close();
        } catch (Exception localException) {
        }
    }
}
