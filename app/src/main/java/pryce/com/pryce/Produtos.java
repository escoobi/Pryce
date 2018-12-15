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

    public Produtos(String descricao, String valor, String codigo) {
        this.descricao = descricao;
        this.valor = valor;
        this.codigo = codigo;
    }

    public Produtos(){};

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("descricao", descricao);
        result.put("valor", valor);
        result.put("codigo", codigo);
        return result;
    }

}
