package pryce.com.pryce;

import java.util.HashMap;
import java.util.Map;

public class Produtos {
    String descricao;
    String valor;
    String codigo;
    String data;
    String hora;

    public Produtos(String descricao, String valor, String codigo, String data, String hora) {
        this.descricao = descricao;
        this.valor = valor;
        this.codigo = codigo;
        this.data = data;
        this.hora = hora;
    }

    public Produtos(){};

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("descricao", descricao);
        result.put("valor", valor);
        result.put("codigo", codigo);
        result.put("data", data);
        result.put("hora", hora);
        return result;
    }

}
