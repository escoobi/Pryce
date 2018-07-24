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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;


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


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                alert(result.getContents());
                qrcode = result.getContents();
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
                obterEmitente(url);
                obterItens(url);
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
                    System.out.println(linha);
                    razao = linha;
                }
                // Pega cnpj
                if (nLinha == 147) {
                    linha = linha.substring(6, linha.indexOf("</"));
                    System.out.println(linha);
                    cnpj = linha;
                    cnpjSelect = linha;
                }
                // Pega logradouro
                if (nLinha == 148) {
                    linha = linha.substring(18, linha.indexOf(","));
                    System.out.println(linha);
                    logradouro = linha;
                }
                // Pega numero
                if (nLinha == 149) {
                    linha = linha.substring(2, linha.indexOf(","));
                    System.out.println(linha);
                    numero = linha;
                }
                // Pega bairro
                if (nLinha == 151) {
                    linha = linha.substring(2, linha.indexOf(","));
                    System.out.println(linha);
                    bairro = linha;
                }
                // Pega cidade
                if (nLinha == 152) {
                    linha = linha.substring(2, linha.indexOf(","));
                    System.out.println(linha);
                    cidade = linha;
                }
                // Pega uf
                if (nLinha == 153) {
                    linha = linha.substring(2, linha.indexOf("</"));
                    System.out.println(linha);
                    uf = linha;
                }
                // Pegar qtd itens
                if (linha.contains("Qtd. total de itens:")) {
                    linha = linha.substring(59, linha.indexOf("</span>"));
                    qtditens = Integer.parseInt(linha);
                    System.out.println(linha);
                }
                // Pegar data emissão
                if (linha.contains("Via Consumidor")) {
                    linha = linha.substring(105, linha.indexOf("-"));
                    data = linha.substring(0, 10);
                    data = data.replace("/", ".");
                    hora = linha.substring(11, 19);
                    System.out.println(data + "\n" + hora);
                }
                nLinha++;
                if (razao != null && cnpj != null && logradouro != null && numero != null && bairro != null
                        && cidade != null && uf != null) {
                    insertEmitente(razao.toUpperCase(), cnpjSelect, logradouro.toUpperCase(), numero, bairro.toUpperCase(), cidade.toUpperCase(), uf.toUpperCase());
                    razao = null;
                    cnpj = null;
                    logradouro = null;
                    numero = null;
                    bairro = null;
                    cidade = null;
                    uf = null;
                }
            }
            scan.close();
            br.close();
        } catch (Exception localException) {
        }
    }

    public void insertEmitente(String razao, String cnpj, String logradouro, String numero, String bairro, String cidade, String uf) {
        try {
            URL url = new URL("http://spark.gruporondomotos.com.br/pryceInsertEmitente.php?");

            url.openConnection();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obterItens(URL caminho) throws IOException {
        StringBuilder numeros = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(caminho.openStream()));

            while (br.ready()) {
                numeros.append(br.readLine()).append("\n");

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
                            System.out.println(linha.substring(38, linha.indexOf("</span>")));
                            descr = linha.substring(38, linha.indexOf("</span>"));
                            System.out.println(linha.substring(linha.indexOf(":") + 1, linha.indexOf(")</span>")).replace(" ", ""));
                            cod = linha.substring(linha.indexOf(":") + 1, linha.indexOf(")</span>")).replace(" ", "");
                            minhaLinha = minhaLinha + 6;

                        }
                        // Obter valor itens
                        if (numLinha == minhaLinhaValor) {
                            System.out.println(linha.substring(linha.indexOf("Vl. Unit.:</strong>&nbsp;") + 25, linha.indexOf("</span></td>")));
                            val = linha.substring(linha.indexOf("Vl. Unit.:</strong>&nbsp;") + 25, linha.indexOf("</span></td>"));
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
            // insertProd(descr, cod, val);
        } catch (Exception localException) {
        }
    }

    public void insertProd(String desc, String cod, String val) {
        try {
          /*  String sql = "update or insert into tbproduto (descr, cod, val, idemitente, data, hora) values ('" + desc.toUpperCase().replace("'", "").replace(",", "") + "', '" + cod + "', '" + val.replace(",", ".")
                    + "', (SELECT idemitente FROM tbemitente WHERE cnpj = '" + cnpjSelect + "'), '" + data + "', '" + hora + "') matching (descr);";*/
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

