package pryce.com.pryce;

import java.util.HashMap;
import java.util.Map;

public class Emitente {
    public String razao;
    public String cnpj;
    public String logradouro;
    public String bairro;
    public String numero;
    public String cidade;
    public String uf;
    public String lat;
    public String log;
    public String data;
    public String hora;

    public Emitente(String razao, String cnpj, String logradouro, String bairro, String numero, String cidade, String uf, String lat, String log, String data, String hora) {
        this.razao = razao;
        this.cnpj = cnpj;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.lat = lat;
        this.log = log;
        this.data = data;
        this.hora = hora;
    }

    public Emitente(){}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("razao", razao);
        result.put("cnpj", cnpj);
        result.put("logradouro", logradouro);
        result.put("bairro", bairro);
        result.put("numero", numero);
        result.put("cidade", cidade);
        result.put("uf", uf);
        result.put("lat", lat);
        result.put("log", log);
        result.put("data", data);
        result.put("hora", hora);

        return result;
    }
}
