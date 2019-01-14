package pryce.com.pryce;

import java.util.HashMap;
import java.util.Map;

public class Produtos {
    public  static int qtdProd = 0;
    public String descricao;
    public static String descricaoSelect;
    public String valor;
    public static String valorSelect;
    public String codigo;
    public static String codigoSelect;
    public static String data;
    public static String hora;


    public Produtos(String valor) {
        this.valor = valor;


    }

    public Produtos(){};

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("valor", valor);
        result.put("data", data);
        result.put("hora", hora);
        return result;
    }

}

