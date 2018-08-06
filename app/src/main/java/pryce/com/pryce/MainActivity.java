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
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {
    Button btnScan;
    String qrcode;







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
                  //  obterInfoEmitente obterInfoEmitente = new obterInfoEmitente();
                 //   obterInfoEmitente.obterEmitente(url);

                    obterNfc infoNfc = new obterNfc();
                    infoNfc.carregaNfc(url);
                    //obterInfoEmitente.obterItens();


                  //  obterInfoProdutos ObterInfoProdutos = new obterInfoProdutos();
                  //  ObterInfoProdutos.obterItens(pryce.com.pryce.obterInfoEmitente.br);


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



    /*



*/
}

