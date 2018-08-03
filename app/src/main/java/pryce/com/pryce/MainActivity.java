package pryce.com.pryce;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.StandardSocketOptions;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    Button btnScan;
    String qrcode;

    public static int qtditens = 0;
    public static String razao = null;
    public static String cnpj = null;
    public static String cnpjSelect = null;
    public static String logradouro = null;
    public static String numero = null;
    public static String bairro = null;
    public static String cidade = null;
    public static String uf = null;
    public static String descr = null;
    public static String cod = null;
    public static String val = null;
    public static String data = null;
    public static String hora = null;
    public static String lat = null;
    public static String log = null;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = (Button) findViewById(R.id.btnQrCode);
        final Activity act = this;
        btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(act);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Ler QRCode - NFc");
                integrator.setCameraId(0);
                integrator.initiateScan();
            }

        });
        if (savedInstanceState != null) {
            qrcode = savedInstanceState.getString("cod");

        }
        mDatabase = FirebaseDatabase.getInstance().getReference("Emitente");


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                //alert(result.getContents());
                qrcode = result.getContents();
                qrcode = qrcode.substring(qrcode.indexOf(".") + 1, qrcode.indexOf("&n"));
                qrcode = "http://" + qrcode + "&nVersao=100&tpAmb=1";
            } else {
                alert("Leitura cancelada.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("cod", qrcode);
        MTask task = new MTask();
        task.execute(qrcode);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }

    private void carregarTxt(String texto) {
        TextView text = (TextView) findViewById(R.id.txtCod);
        text.setText(texto);

    }

    public void onResume() {
        super.onResume();
        carregarTxt(qrcode);

    }

    public class MTask extends AsyncTask<String, Long, String> {


        @Override
        protected String doInBackground(String... urls) {

            URL url = null;
            try {
                url = new URL(qrcode);
                if(qrcode.length() == 124) {
                    obterEmitente(url);
                    obterItens(url);
                }
                else{
                    alert("Erro ao ler o QRCode.");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String resultValue) {
        }
    }

    public void obterEmitente(URL url) throws IOException {
        StringBuilder numeros = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String minhaLinha;
            while ((minhaLinha = br.readLine()) != null) {
                numeros.append(minhaLinha).append("\n");

            }

            Scanner scan = new Scanner(numeros.toString());
            int nLinha = 0;
            while (scan.hasNextLine()) {
                String linha = scan.nextLine();
                // Pega razão social
                if (linha.contains("u20")) {
                    linha = linha.substring(30, linha.indexOf("</"));
                    razao = linha;
                }
                // Pega cnpj
                if (nLinha == 147) {
                    linha = linha.substring(6, linha.indexOf("</"));
                    cnpj = linha;
                    cnpjSelect = linha;
                }
                // Pega logradouro
                if (nLinha == 148) {
                    linha = linha.substring(18, linha.indexOf(","));
                    logradouro = linha;
                }
                // Pega numero
                if (nLinha == 149) {
                    linha = linha.substring(2, linha.indexOf(","));
                    numero = linha;
                }
                // Pega bairro
                if (nLinha == 151) {
                    linha = linha.substring(2, linha.indexOf(","));
                    bairro = linha;
                }
                // Pega cidade
                if (nLinha == 152) {
                    linha = linha.substring(2, linha.indexOf(","));
                    cidade = linha;
                }
                // Pega uf
                if (nLinha == 153) {
                    linha = linha.substring(2, linha.indexOf("</"));
                    uf = linha;
                }
                // Pegar qtd itens
                if (linha.contains("Qtd. total de itens:")) {
                    linha = linha.substring(59, linha.indexOf("</span>"));
                    qtditens = Integer.parseInt(linha);
                }
                // Pegar data emissão
                if (linha.contains("Protocolo de Autoriza")) {
                    data = linha.substring(79, 89);
                    data = data.replace("/", ".");
                    hora = linha.substring(90, 98);
                }
                nLinha++;

                if (razao != null && cnpj != null && logradouro != null && numero != null && bairro != null
                        && cidade != null && uf != null) {
                    //obter lat e log via endereço
                    obterLatLog(logradouro, bairro, numero, cidade, uf);
                  //inserir e atualizar emitente
                    gravarEmitente(razao, cnpj, logradouro, bairro, numero,cidade, uf, lat, log);


                    razao = null;
                    cnpj = null;
                    logradouro = null;
                    numero = null;
                    bairro = null;
                    cidade = null;
                    uf = null;
                    lat = null;
                    log = null;
                }
            }
            scan.close();
            br.close();
        } catch (Exception localException) {
        }
    }

    public void obterLatLog(String logradouro, String bairro, String numero, String cidade, String uf){
        StringBuilder numeros = new StringBuilder();


        try {
            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+logradouro.replaceAll(" ", "+")+","+numero+","+bairro.replaceAll(" ", "+")+","+cidade.replaceAll(" ", "+")+","+uf);
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

    public static class Emitente {
        String razao;
        String cnpj;
        String logradouro;
        String bairro;
        String numero;
        String cidade;
        String uf;
        String lat;
        String log;



        public Emitente(String razao, String cnpj, String logradouro, String bairro, String numero, String cidade, String uf, String lat, String log) {
            this.razao = razao;
            this.cnpj = cnpj;
            this.logradouro = logradouro;
            this.bairro = bairro;
            this.numero = numero;
            this.cidade = cidade;
            this.uf = uf;
            this.lat = lat;
            this.log = log;
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

            return result;
        }

    }

    private void gravarEmitente (final String razao, final String cnpj, final String logradouro, final String bairro, final String numero, final String cidade, final String uf, final String lat, final String log){

        mDatabase.orderByChild("cnpj").equalTo(cnpj).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Emitente emitente = dataSnapshot.getValue(Emitente.class);

                String key = null;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    key = snapshot.getKey();

                }
                    if (dataSnapshot.exists()) {

                        Map<String, Object> emitenteUpdates = new HashMap<>();
                        emitenteUpdates.put(key + "/bairro", bairro);
                        emitenteUpdates.put(key + "/cidade", cidade);
                        emitenteUpdates.put(key + "/cnpj", cnpj);
                        emitenteUpdates.put(key + "/logradouro", logradouro);
                        emitenteUpdates.put(key + "/numero", numero);
                        emitenteUpdates.put(key + "/razao", razao);
                        emitenteUpdates.put(key + "/uf", uf);
                        emitenteUpdates.put(key + "/lat", lat);
                        emitenteUpdates.put(key + "/log", log);

                        mDatabase.updateChildren(emitenteUpdates);


                    } else {
                        emitente = new Emitente(razao, cnpj, logradouro, bairro, numero, cidade, uf, lat, log);
                        mDatabase.push().setValue(emitente);

                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
   }

    public void obterItens(URL caminho) throws IOException {
        StringBuilder numeros = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(caminho.openStream()));


            String minhaLinhaDaXexeca;
            while ((minhaLinhaDaXexeca = br.readLine()) != null) {
                numeros.append(minhaLinhaDaXexeca).append("\n");

            }


            Scanner scan = new Scanner(numeros.toString());
            int numLinha = 0;
            int minhaLinha = 157;
            int minhaLinhaValor = 159;
            int qtdTotal = minhaLinha + (6 * qtditens);
            while (scan.hasNextLine()) {
                String linha = scan.nextLine();

                for (int x = 0; x <= qtditens; x++) {
                    if (qtdTotal != numLinha) {
                        // Obter itens
                        if (numLinha == minhaLinha) {

                            descr = linha.substring(38, linha.indexOf("</span>"));
                            linha = linha.replaceAll(" ", "");
                            cod = linha.substring(linha.indexOf(":") + 1, linha.length());
                            cod = cod.substring(0, cod.indexOf(")"));
                            minhaLinha = minhaLinha + 6;

                        }
                        // Obter valor itens
                        if (numLinha == minhaLinhaValor) {

                            val = linha.substring(linha.indexOf("Vl. Unit.:</strong>&nbsp;") + 25, linha.indexOf("</span></td>"));
                            val = val.replace(",", ".");
                            minhaLinhaValor = minhaLinhaValor + 6;

                        }

                    }

                }
                if (descr != null && cod != null && val != null) {
                    insertProd(descr, cod, val);
                    descr = null;
                    cod = null;
                    val = null;
                }
                numLinha++;

            }
            scan.close();
            br.close();
            insertProd(descr, cod, val);
        } catch (Exception localException) {
        }
    }

    public void insertProd(String desc, String cod, String val) {
        try {
            OkHttpClient client = new OkHttpClient();
            URL url = new URL("http://spark.gruporondomotos.com.br/pryceInsertProd.php?desc=" + desc + "&cod=" + cod + "&val=" + val + "&dt=" + data + "&hr=" + hora + "&cnpj=" + cnpjSelect);
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();



        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}

