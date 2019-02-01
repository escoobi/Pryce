package pryce.com.pryce;

import java.util.HashMap;
import java.util.Map;

public class Emitente {
    public String fantasia;
    public static String fantasiaSelect;
    public String cnpj;
    public static  String cnpjSelect;
    public String logradouro;
    public static String logradouroSelect;
    public String bairro;
    public static String bairroSelect;
    public String numero;
    public static String numeroSelect;
    public String cidade;
    public static String cidadeSelect;
    public String uf;
    public static String ufSelect;
    public static String keyEmitente;
    public static String lat;
    public static String log;
    public static String qrcodeUrl;



    public Emitente(String fantasia, String cnpj, String logradouro, String bairro, String numero, String lat, String log) {
        this.fantasia = fantasia;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.numero = numero;
        this.lat = lat;
        this.log = log;
    }

    public Emitente(){}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fantasia", fantasia);
        result.put("cnpj", cnpj);
        result.put("logradouro", logradouro);
        result.put("bairro", bairro);
        result.put("numero", numero);
        result.put("lat", lat);
        result.put("log", log);

        return result;
    }
}
