package pryce.com.pryce;

import java.util.HashMap;
import java.util.Map;

public class chamaQrdCode {
    public String codeqr;
    public chamaQrdCode(String codqqr){
        this.codeqr = codqqr;
    }
    public chamaQrdCode(){}
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("codeqr", codeqr);
        return result;
    }
}
