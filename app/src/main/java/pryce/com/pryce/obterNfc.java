package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class obterNfc {
    public BufferedReader br;

    public void carregaNfc(URL url) throws IOException {
        Emitente emitente = new Emitente();


        br = new BufferedReader(new InputStreamReader(url.openStream(), "windows-1252"));
        String minhaLinha;
        int nLinha = 0;
        while ((minhaLinha = br.readLine()) != null) {


            nLinha++;

            //pega razao
            if (nLinha == 126) {
                minhaLinha = minhaLinha.substring(44, minhaLinha.indexOf("</"));
                emitente.razao = minhaLinha;
                Emitente.razaoSelect = emitente.razao.toUpperCase();
            }


            //pega fantasia
            if (nLinha == 130) {
                minhaLinha = minhaLinha.substring(44, minhaLinha.indexOf("</"));
                emitente.fantasia = minhaLinha;
                Emitente.fantasiaSelect = emitente.fantasia.toUpperCase();
            }

            // pega logradouro
            if (nLinha == 176) {

                minhaLinha = minhaLinha.substring(44, minhaLinha.indexOf("-"));
                emitente.logradouro = minhaLinha;
                Emitente.logradouroSelect = emitente.logradouro.toUpperCase();

            }

            // Pega numero
            if (nLinha == 156) {
                minhaLinha = minhaLinha.substring(44, minhaLinha.indexOf("</"));
                emitente.numero = minhaLinha;
                Emitente.numeroSelect = emitente.numero.toUpperCase();

            }
            // Pega bairro
            if (nLinha == 154) {
                minhaLinha = minhaLinha.substring(44, minhaLinha.indexOf("</"));
                emitente.bairro = minhaLinha;
                Emitente.bairroSelect = emitente.bairro.toUpperCase();
            }
            // Pega cidade
            if (nLinha == 160) {
                minhaLinha = minhaLinha.substring(32, minhaLinha.indexOf("</"));
                emitente.cidade = minhaLinha;
                Emitente.cidadeSelect = emitente.cidade.toUpperCase();
            }
            // Pega uf
            if (nLinha == 166) {
                minhaLinha = minhaLinha.substring(44, minhaLinha.indexOf("</"));
                emitente.uf = minhaLinha;
                Emitente.ufSelect = emitente.uf.toUpperCase();
            }

        }
        br.close();
        obterCordenadasEmitente obter = new obterCordenadasEmitente();
        obter.obterLatLog(emitente.logradouro, emitente.bairro, emitente.numero, emitente.cidade, emitente.uf);

    }


}