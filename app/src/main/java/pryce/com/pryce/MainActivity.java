package pryce.com.pryce;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {
    Button btnScan;
    public static String qrcode;
    static ProgressBar mProgressBar;



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

    public static void exibirProgress(boolean exibir) {
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
                Intent intent = new Intent(this, usandoIntentService.class);
                startService(intent);

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

    public void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }

    private void carregarTxt(String texto) {
        TextView text = (TextView) findViewById(R.id.txtCod);
        text.setText(texto);

    }
}

