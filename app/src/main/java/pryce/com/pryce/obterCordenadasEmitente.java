package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;



public class obterCordenadasEmitente {

    public static String lat = null;
    public static String log = null;

    public void obterLatLog(String logradouro, String bairro, String numero, String cidade, String uf){
        StringBuilder numeros = new StringBuilder();

        //http://maps.googleapis.com/maps/api/geocode/json?address=AV.+CAPITAO+SILVIO,3790,GRANDES+AREAS,ARIQUEMES,RO

        try {
            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+logradouro.replaceAll(" ", "+")+"+"+numero+"+"+bairro.replaceAll(" ", "+")+"+"+cidade.replaceAll(" ", "+")+"+"+uf);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

            }




            Scanner scan = new Scanner(numeros.toString());

            while (scan.hasNextLine()) {
                String linha = scan.nextLine();
                // Pegar lat e log

                if (linha.contains("\"lat\" : ")) {
                    linha = linha.substring(linha.indexOf("-"), linha.indexOf(","));
                    lat = linha;

                }
                if (linha.contains("\"lng\" : ")) {
                    linha = linha.substring(linha.indexOf("-"), linha.length());
                    log = linha;
                }
                if(log != null && lat != null){
                    break;
                }
            }
            scan.close();

        }
        catch (Exception localException){

        }
    }
}
