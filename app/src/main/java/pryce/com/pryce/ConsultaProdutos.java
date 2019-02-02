package pryce.com.pryce;

import java.util.HashMap;
import java.util.Map;

public class ConsultaProdutos {

    public String fantasia;
    public String razao;
    public String descricao;
    public String valor;
    public String cnpj;
    public String logradouro;
    public String bairro;
    public String numero;
    public String cidade;
    public String cod;
    public String uf;
    public String data;
    public String hora;
    public String lat;
    public String log;

    public ConsultaProdutos(String fantasia, String razao, String descricao, String valor, String cnpj, String logradouro, String bairro, String numero, String cidade, String uf, String cod, String data, String hora, String lat, String log) {
        this.fantasia = fantasia;
        this.razao = razao;
        this.descricao = descricao;
        this.valor = valor;
        this.cnpj = cnpj;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.cod = cod;
        this.data = data;
        this.hora = hora;
        this.lat = lat;
        this.log = log;
    }

    public ConsultaProdutos() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fantasia", fantasia);
        result.put("razao", razao);
        result.put("descricao", descricao);
        result.put("valor", valor);
        result.put("cnpj", cnpj);
        result.put("logradouro", logradouro);
        result.put("bairro", bairro);
        result.put("numero", numero);
        result.put("cidade", cidade);
        result.put("uf", uf);
        result.put("cod", cod);
        result.put("data", data);
        result.put("hora", hora);
        result.put("lat", lat);
        result.put("log", log);
        return result;
    }

}

