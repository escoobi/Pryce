package pryce.com.pryce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import static pryce.com.pryce.Emitente.*;


public class MainActivity extends AppCompatActivity {

    Button btnScan;
    TextView txtRazao;
    static ProgressBar mProgressBar;
    AutoCompleteTextView completa;
    public static String qrcode;
    public static String descProd;
    private DatabaseReference mDatabaseProtudo;
    private DatabaseReference mDatabaseNFC;
    ArrayAdapter<String> autoComplete;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiZXNjb29iaSIsImEiOiJjanF3dGpyNDUwMGc3NDJtamNsMmUwajRnIn0.ISWkwRW5hAn0gZbX9tjAqQ");
        setContentView(R.layout.activity_main);
        completa = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        btnScan = (Button) findViewById(R.id.btnQrCode);
        txtRazao = (TextView) findViewById(R.id.txtRazao);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        autoComplete = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item);
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


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                    }
                })
        );


        new MTaskAutoCompleta().execute(descProd);


        completa.setAdapter(autoComplete);
        completa.setThreshold(1);

        completa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                descProd = adapterView.getItemAtPosition(i).toString();
                descProd = descProd.replace(descProd.substring(descProd.indexOf(" -"), descProd.length()), "");

                new MTaskConsulta().execute();

                Produtos.descricaoSelect = descProd;

                completa.clearListSelection();
                completa.setText("");
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (in != null) in.hideSoftInputFromWindow(completa.getApplicationWindowToken(), 0);
                descProd = null;


            }
        });


    }

    private static void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


            if (result != null) {
                if (result.getContents() != null) {
                    qrcodeUrl = "";
                    qrcode = result.getContents();
                    mDatabaseNFC = FirebaseDatabase.getInstance().getReference("nfc");
                    mDatabaseNFC.orderByChild("codeqr").equalTo(qrcode).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            chamaQrdCode chamaQrdCode = dataSnapshot.getValue(chamaQrdCode.class);
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    qrcodeUrl = snapshot.getKey();
                                    alert("QRCode já registrado.");

                                }

                            } else {
                                chamaQrdCode = new chamaQrdCode(qrcode);
                                mDatabaseNFC.push().setValue(chamaQrdCode, new DatabaseReference.CompletionListener() {

                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            alert(databaseError.getMessage());
                                        } else {


                                            if (!qrcode.contains("chNFe=")) {
                                                qrcode = qrcode.substring(qrcode.indexOf("=") + 1, qrcode.length());
                                                qrcode = "http://www.nfce.sefin.ro.gov.br/consultanfce/consulta.jsp?p=" + qrcode;
                                                new MTask().execute(qrcode);


                                            } else {
                                                qrcode = qrcode.substring(qrcode.indexOf("=") + 1, qrcode.length());
                                                qrcode = "http://www.nfce.sefin.ro.gov.br/consultanfce/consulta.jsp?chNFe=" + qrcode;
                                                new MTask().execute(qrcode);


                                            }


                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception ex) {
            alert("Ops! Algum erro encontrado.");
        }

        exibirProgress(false);

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("cod", qrcode);
        super.onSaveInstanceState(savedInstanceState);
        mapView.onSaveInstanceState(savedInstanceState);
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }

    public void carregarTxt(String razao, String cnpj, String bairro, String endereco, String numero, String cidade, String uf) {
        TextView textRazao = (TextView) findViewById(R.id.txtRazao);
        textRazao.setText(razao);

        TextView textCnpj = (TextView) findViewById(R.id.txtCnpj);
        textCnpj.setText(cnpj);

        TextView textBairro = (TextView) findViewById(R.id.txtBairro);
        textBairro.setText(bairro);

        TextView textEndereco = (TextView) findViewById(R.id.txtEndereco);
        textEndereco.setText(endereco);

        TextView textNumero = (TextView) findViewById(R.id.txtNumero);
        textNumero.setText(numero);

        TextView textCidade = (TextView) findViewById(R.id.txtCidade);
        textCidade.setText(cidade);

        TextView textUf = (TextView) findViewById(R.id.txtUf);
        textUf.setText(uf);
        exibirProgress(false);


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {


                mapboxMap.addMarker(new MarkerOptions()


                        .position(new LatLng(Double.parseDouble(Emitente.lat), Double.parseDouble(Emitente.log)))

                        .title(Produtos.descricaoSelect)
                        .snippet(Produtos.valorSelect));
                mapboxMap.getMinZoomLevel();


            }
        });


    }

    public class MTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {


            URL url = null;
            try {

                url = new URL(qrcode);
                obterNfc infoNfc = new obterNfc();
                infoNfc.carregaNfc(url);


            } catch (MalformedURLException e) {
                e.printStackTrace();
                alert("Endereço do QRCode inválido.");
            } catch (Exception e) {
                e.printStackTrace();
                alert("Ops! Algum erro encontrado.");
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            new MTaskProduto().execute();
        }

    }

    public class MTaskConsulta extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... descrprod) {


            mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("produtos");


            mDatabaseProtudo.orderByChild("descricao").equalTo(descProd).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    exibirProgress(true);

                    for (DataSnapshot child : dataSnapshot.getChildren()) {


                        try {
                            Emitente.lat = child.child("lat").getValue().toString();
                            Emitente.log = child.child("log").getValue().toString();
                            carregarTxt(child.child("razao").getValue().toString(), child.child("cnpj").getValue().toString(), child.child("bairro").getValue().toString(), child.child("logradouro").getValue().toString(), child.child("numero").getValue().toString(), child.child("cidade").getValue().toString(), child.child("uf").getValue().toString());

                        } catch (Exception ex) {
                            alert("Produto não localizado.");
                            exibirProgress(false);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            exibirProgress(false);


        }
    }

    public class MTaskProduto extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... voids) {
            obterProdutos prod = new obterProdutos();
            prod.carregaProdutos();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            alert("NFC registrada com sucesso.");
            new MTaskAutoCompleta().execute(descProd);
        }

    }

    public class MTaskAutoCompleta extends AsyncTask<String, Integer, Boolean> {
        @Override


        protected Boolean doInBackground(String... strings) {
            mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("produtos");
            mDatabaseProtudo.orderByChild("descricao").startAt(descProd).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        try {
                            String suggestion = child.child("descricao").getValue(String.class);
                            Float valorProd = Float.parseFloat(child.child("valor").getValue(String.class));
                            DecimalFormat df = new DecimalFormat("0.00");
                            df.setMaximumFractionDigits(2);
                            autoComplete.add(suggestion + " - R$ " + df.format(valorProd));

                        } catch (Exception ex) {
                            alert("Produto não localizado.");
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
            autoComplete.clear();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            exibirProgress(false);

        }


    }


}



