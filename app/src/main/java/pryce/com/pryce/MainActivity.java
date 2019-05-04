package pryce.com.pryce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {


    static ConstraintLayout constraintLayout;
    static TextView txtAguarde;
    static TextView txtEmpresa;
    static TextView txtProduto;
    static TextView txtValor;
    Button btnScan;
    TextView txtRazao;
    static ProgressBar mProgressBar;
    AutoCompleteTextView completa;
    public static String qrcode;
    public static String descProd;
    private DatabaseReference mDatabaseProtudo;
    private DatabaseReference mDatabaseNFC;
    ArrayAdapter<String> autoComplete;
    URL urlqrCode;
    public static String qrcodeUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Mapbox.getInstance(this, "pk.eyJ1IjoiZXNjb29iaSIsImEiOiJjanF3dGpyNDUwMGc3NDJtamNsMmUwajRnIn0.ISWkwRW5hAn0gZbX9tjAqQ");
        setContentView(R.layout.activity_main);
        completa = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        btnScan = (Button) findViewById(R.id.btnQrCode);
        txtAguarde = (TextView) findViewById(R.id.textViewAguarde);
        txtAguarde.setVisibility(View.INVISIBLE);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintDadosEmpresa);
        constraintLayout.setVisibility(View.INVISIBLE);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtEmpresa = (TextView) findViewById(R.id.textViewEmpresa);
        txtProduto = (TextView) findViewById(R.id.textViewProduto);
        txtValor = (TextView) findViewById(R.id.textViewValor);
        autoComplete = new ArrayAdapter<String>(this, R.layout.autocompleta_layout);
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

    private static void exibirAguarde(boolean exibir) {
        txtAguarde.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private static void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);

    }

    private static void exibirDados(boolean exibir) {
        constraintLayout.setVisibility(exibir ? View.VISIBLE : View.GONE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


            if (result != null) {
                if (result.getContents() != null) {

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
                                                Emitente.cnpjSelect = qrcode.substring(66, 80);
                                                try {
                                                    urlqrCode = new URL(qrcode);
                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                }

                                                new MTask().execute(qrcode);


                                            } else {
                                                qrcode = qrcode.substring(qrcode.indexOf("=") + 1, qrcode.length());
                                                qrcode = "http://www.nfce.sefin.ro.gov.br/consultanfce/consulta.jsp?chNFe=" + qrcode;
                                                Emitente.cnpjSelect = qrcode.substring(70, 84);
                                                try {
                                                    urlqrCode = new URL(qrcode);
                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                }
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
        exibirAguarde(false);

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("cod", qrcode);
        super.onSaveInstanceState(savedInstanceState);

    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }

    /*
        public void carregarTxt() {

            mapView.setVisibility(View.VISIBLE);

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mapboxMap.clear();
                    if (Emitente.fantasiaSelect.isEmpty()) {
                        Marker pontoVenda = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Emitente.lat), Double.parseDouble(Emitente.log))).title(Emitente.razaoSelect).snippet(Produtos.descricaoSelect + "\n R$ " + Produtos.valorSelect.replace(".", ",")));
                        mapboxMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble(Emitente.lat), Double.parseDouble(Emitente.log))).zoom(14).tilt(30).build());
                        mapboxMap.selectMarker(pontoVenda);
                        mapboxMap.setAllowConcurrentMultipleOpenInfoWindows(true);
                    } else {
                        Marker pontoVenda = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Emitente.lat), Double.parseDouble(Emitente.log))).title(Emitente.fantasiaSelect).snippet(Produtos.descricaoSelect + "\n R$ " + Produtos.valorSelect.replace(".", ",")));
                        mapboxMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble(Emitente.lat), Double.parseDouble(Emitente.log))).zoom(14).tilt(30).build());
                        mapboxMap.selectMarker(pontoVenda);
                        mapboxMap.setAllowConcurrentMultipleOpenInfoWindows(true);
                    }


                }
            });


            exibirProgress(false);
            exibirAguarde(false);


        }
    */
    public class MTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {


            URL url = null;
            try {

                url = new URL("https://portalcontribuinte.sefin.ro.gov.br/Publico/consultapublica.jsp?TipoDevedor=3&NuDevedor=" + Emitente.cnpjSelect);
                obterNfc infoNfc = new obterNfc();
                infoNfc.carregaNfc(url);


            } catch (MalformedURLException e) {
                e.printStackTrace();
                this.cancel(true);

            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);

            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
            exibirAguarde(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            new MTaskProduto().execute(urlqrCode);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            alert("ops! ao inesperado ocorreu.");

            mDatabaseNFC = FirebaseDatabase.getInstance().getReference("nfc");
            mDatabaseNFC.orderByChild("codeqr").equalTo(qrcode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chamaQrdCode chamaQrdCode = dataSnapshot.getValue(chamaQrdCode.class);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            qrcodeUrl = snapshot.getKey();
                            mDatabaseNFC.child(qrcodeUrl).removeValue();

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            exibirProgress(false);
            exibirAguarde(false);
        }
    }

    public class MTaskConsulta extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... descrprod) {


            mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("produtos");


            mDatabaseProtudo.orderByChild("descricao").equalTo(descProd).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot child : dataSnapshot.getChildren()) {


                        try {

                            Emitente.lat = child.child("lat").getValue().toString();
                            Emitente.log = child.child("log").getValue().toString();
                            Emitente.razaoSelect = child.child("razao").getValue().toString();
                            Emitente.fantasiaSelect = child.child("fantasia").getValue().toString();
                            Produtos.valorSelect = child.child("valor").getValue().toString();

                            if (!Emitente.fantasiaSelect.equals("")) {
                                txtEmpresa.setText(Emitente.fantasiaSelect);
                            } else {
                                txtEmpresa.setText(Emitente.razaoSelect);
                            }
                            BigDecimal bd = new BigDecimal(Double.parseDouble(Produtos.valorSelect));
                            NumberFormat nf = NumberFormat.getCurrencyInstance();
                            String formato = nf.format(bd);
                            txtProduto.setText(Produtos.descricaoSelect);
                            txtValor.setText(formato);

                            exibirDados(true);
                            exibirProgress(false);
                            exibirAguarde(false);

                        } catch (Exception ex) {
                            alert("Produto não localizado.");
                            exibirProgress(false);
                            exibirAguarde(false);
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    exibirProgress(false);
                    exibirAguarde(false);
                }
            });


            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
            exibirAguarde(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            exibirProgress(false);
            exibirAguarde(false);


        }
    }

    public class MTaskProduto extends AsyncTask<URL, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(URL... voids) {
            obterProdutos prod = new obterProdutos();
            try {
                prod.carregaProdutos(urlqrCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            exibirAguarde(true);
            autoComplete.clear();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            exibirProgress(false);
            exibirAguarde(false);

        }


    }
}



