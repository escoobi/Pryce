package pryce.com.pryce;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;



public class obterCordenadasEmitente {



    public void obterLatLog(String logradouro, String bairro, String numero, String cidade, String uf){
        StringBuilder numeros = new StringBuilder();

        //http://maps.googleapis.com/maps/api/geocode/json?address=AV.+CAPITAO+SILVIO,3790,GRANDES+AREAS,ARIQUEMES,RO
        //https://maps.googleapis.com/maps/api/geocode/json?address=Rua+Gavi%C3%A3o+Real+4695+Ariquemes+RO&key=AIzaSyBTs9ShHOmdlmcFOqUAuxzes0nspUtUOW4
//"https://maps.googleapis.com/maps/api/geocode/json?address="+logradouro.replaceAll(" ", "+")+"+"+numero+"+"+bairro.replaceAll(" ", "+")+"+"+cidade.replaceAll(" ", "+")+"+"+uf+"&key=AIzaSyBTs9ShHOmdlmcFOqUAuxzes0nspUtUOW4"
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+logradouro.replaceAll(" ", "+")+"+"+numero+"+"+bairro.replaceAll(" ", "+")+"+"+cidade.replaceAll(" ", "+")+"+"+uf+"&key=AIzaSyBTs9ShHOmdlmcFOqUAuxzes0nspUtUOW4");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            Emitente.lat = null;
            Emitente.log = null;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

            }




            Scanner scan = new Scanner(numeros.toString());

            while (scan.hasNextLine()) {
                String linha = scan.nextLine();
                // Pegar lat e log

                if (linha.contains("\"lat\" : ")) {
                    linha = linha.substring(linha.indexOf("-"), linha.indexOf(","));
                    Emitente.lat = linha;

                }
                if (linha.contains("\"lng\" : ")) {
                    linha = linha.substring(linha.indexOf("-"), linha.length());
                    Emitente.log = linha;
                }
                if(Emitente.log != null && Emitente.lat != null){
                    break;
                }
            }
            scan.close();

        }
        catch (Exception localException){
            System.out.println(localException.toString());


        }
    }
}
