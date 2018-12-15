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

import com.google.firebase.database.DatabaseReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.net.MalformedURLException;
import java.net.URL;
import static pryce.com.pryce.obterCordenadasEmitente.lat;
import static pryce.com.pryce.obterCordenadasEmitente.log;



public class MainActivity extends AppCompatActivity {
    Button btnScan;
    public static String qrcode;
    static ProgressBar mProgressBar;
    private DatabaseReference mDatabaseEmitente;
    private DatabaseReference mDatabaseProtudo;

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

    private static void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                //alert(result.getContents());
                qrcode = result.getContents();
                qrcode = qrcode.substring(qrcode.indexOf("=") + 1, qrcode.length());
                qrcode = "http://www.nfce.sefin.ro.gov.br/consultanfce/consulta.jsp?p=" + qrcode;
                //qrcode = qrcode.substring(qrcode.indexOf(".") + 1, qrcode.indexOf("&n"));
                //qrcode = "http://" + qrcode + "&nVersao=100&tpAmb=1";

                //carregarTxt(qrcode);


                new MTask().execute(qrcode);


                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);

                /*while (Emitente.keyEmitente != null) {
                    new MTaskObterKeyEmitente().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    break;
                }

                } else {
                alert("Leitura cancelada.");*/
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

    public void carregarTxt(String texto) {
        TextView text = (TextView) findViewById(R.id.txtCod);
        text.setText(texto);

    }

    public class MTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);

        }

        @Override
        protected Boolean doInBackground(String... urls) {


            URL url = null;
            try {
                url = new URL(qrcode);

                if(qrcode.length() >= 124) {
                    obterNfc infoNfc = new obterNfc();
                    infoNfc.carregaNfc(url);
                    gravarEmitente gravar = new gravarEmitente();
                    gravar.gravarEmitente(Emitente.razaoSelect, Emitente.cnpjSelect, Emitente.logradouroSelect, Emitente.bairroSelect, Emitente.numeroSelect, Emitente.cidadeSelect, Emitente.ufSelect, lat, log);


                }
                else{
                    //   alert("Erro ao ler o QRCode.");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            exibirProgress(false);
        }
    }


}

