package pryce.com.pryce;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.net.MalformedURLException;
import java.net.URL;

import static pryce.com.pryce.obterCordenadasEmitente.lat;
import static pryce.com.pryce.obterCordenadasEmitente.log;
import static pryce.com.pryce.obterNfc.bairro;
import static pryce.com.pryce.obterNfc.cidade;
import static pryce.com.pryce.obterNfc.cnpj;
import static pryce.com.pryce.obterNfc.logradouro;
import static pryce.com.pryce.obterNfc.numero;
import static pryce.com.pryce.obterNfc.razao;
import static pryce.com.pryce.obterNfc.uf;


public class MainActivity extends AppCompatActivity {
    Button btnScan;
    String qrcode;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = (Button) findViewById(R.id.btnQrCode);
        mProgressBar = (ProgressBar) findViewById(R.id.barrinha);
        final Activity act = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //bloqueia orientação de tela.
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


    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                //alert(result.getContents());
                qrcode = result.getContents();
                qrcode = qrcode.substring(qrcode.indexOf(".") + 1, qrcode.indexOf("&n"));
                qrcode = "http://" + qrcode + "&nVersao=100&tpAmb=1";

                carregarTxt(qrcode);
                MTask task = new MTask();
                task.execute(qrcode);

                MTaskProduto produtoTask = new MTaskProduto();
                produtoTask.execute(qrcode);

            } else {
                alert("Leitura cancelada.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("cod", qrcode);



        super.onSaveInstanceState(savedInstanceState);
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }

    private void carregarTxt(String texto) {
        TextView text = (TextView) findViewById(R.id.txtCod);
        text.setText(texto);

    }

        public class MTask extends AsyncTask<String, Long, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected String doInBackground(String... urls) {

            URL url = null;
            try {
                url = new URL(qrcode);

                if(qrcode.length() == 124) {
                    obterNfc infoNfc = new obterNfc();
                    infoNfc.carregaNfc(url);
                }
                else{
                    alert("Erro ao ler o QRCode.");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Carregado";
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            exibirProgress(false);

            gravarEmitente gravar = new gravarEmitente();
            gravar.gravarEmitente(razao, cnpj, logradouro, bairro, numero, cidade, uf, lat, log);

        }

    }

    public class MTaskProduto extends AsyncTask<String, Long, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected String doInBackground(String... urls) {

            URL url = null;
            try {
                url = new URL(qrcode);

                if(qrcode.length() == 124) {
                   obterProdutos ch = new obterProdutos();
                   ch.carregaProdutos(url);
                }
                else{
                    alert("Erro ao ler o QRCode.");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Carregado";
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            exibirProgress(false);

            gravarProdutos produtos = new gravarProdutos();
            produtos.obterKeyEmitente(cnpj);


        }

    }

}

