package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import static pryce.com.pryce.obterCordenadasEmitente.lat;
import static pryce.com.pryce.obterCordenadasEmitente.log;


public class obterNfc {

    public String linha = null;
    public BufferedReader br;
    public static StringBuilder numerosPublicos;





    public void carregaNfc(URL url) throws IOException {
        Emitente emitente = new Emitente();
        Produtos produto = new Produtos();

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            StringBuilder numeros = new StringBuilder();
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");


            }
            numerosPublicos = numeros;
            Scanner scan = new Scanner(numeros.toString());
            int nLinha = 0;
            while (scan.hasNextLine()) {
                linha = scan.nextLine();
                // Pega razão social
                if (linha.contains("u20")) {
                    linha = linha.substring(30, linha.indexOf("</"));
                    emitente.razao = linha;
                    Emitente.razaoSelect = emitente.razao;
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
                    emitente.logradouro = linha;
                    Emitente.logradouroSelect = emitente.logradouro;
                }
                // Pega numero
                if (nLinha == 149) {
                    linha = linha.substring(2, linha.indexOf(","));
                    emitente.numero = linha;
                    Emitente.numeroSelect = emitente.numero;
                }
                // Pega bairro
                if (nLinha == 151) {
                    linha = linha.substring(2, linha.indexOf(","));
                    emitente.bairro = linha;
                    Emitente.bairroSelect = emitente.bairro;
                }
                // Pega cidade
                if (nLinha == 152) {
                    linha = linha.substring(2, linha.indexOf(","));
                    emitente.cidade = linha;
                    Emitente.cidadeSelect = emitente.cidade;
                }
                // Pega uf
                if (nLinha == 153) {
                    linha = linha.substring(2, linha.indexOf("</"));
                    emitente.uf = linha;
                    Emitente.ufSelect = emitente.uf;
                }

             /*   // Pegar data emissão
                if (linha.contains("Protocolo de Autoriza")) {
                    produto.data = linha.substring(79, 89);
                    produto.data = produto.data.replace("/", ".");
                    produto.hora = linha.substring(90, 98);
                }


*/
             //Pega Quantidade de Itens
                    if (linha.contains("Qtd. total de itens:")) {
                        linha = linha.substring(59, linha.indexOf("</span>"));
                        Produtos.qtdProd = Integer.parseInt(linha);
                    }



                nLinha++;
            }
            if (emitente.razao != null && emitente.cnpj != null && emitente.logradouro != null && emitente.numero != null && emitente.bairro != null && emitente.cidade != null && emitente.uf != null) {

               /* while (lat == null & log == null) {
                    obterCordenadasEmitente obterCordenadasEmitente = new obterCordenadasEmitente();
                    obterCordenadasEmitente.obterLatLog(emitente.logradouro, emitente.bairro, emitente.numero, emitente.cidade, emitente.uf);
                }
                */
            }
            scan.close();
        } catch (Exception localException) {
        }
        finally {
            br.close();
        }
    }


}
