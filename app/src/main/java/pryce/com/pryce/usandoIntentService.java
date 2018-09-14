package pryce.com.pryce;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static pryce.com.pryce.MainActivity.qrcode;
import static pryce.com.pryce.obterCordenadasEmitente.lat;
import static pryce.com.pryce.obterCordenadasEmitente.log;

public class usandoIntentService extends IntentService{

    public static String keyEmitente = null;
    private DatabaseReference mDatabaseEmitente;



    public usandoIntentService() {
        super("usandoIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        URL url = null;
        try {
            url = new URL(qrcode);

            if(qrcode.length() == 124) {
                obterNfc infoNfc = new obterNfc();
                infoNfc.carregaNfc(url);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gravarEmitente gravar = new gravarEmitente();
        gravar.gravarEmitente(Emitente.razaoSelect, Emitente.cnpjSelect, Emitente.logradouroSelect, Emitente.bairroSelect, Emitente.numeroSelect, Emitente.cidadeSelect, Emitente.ufSelect, lat, log);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mDatabaseEmitente = FirebaseDatabase.getInstance().getReference("Emitente");
        mDatabaseEmitente.orderByChild("cnpj").equalTo(Emitente.cnpjSelect).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Emitente emitente = dataSnapshot.getValue(Emitente.class);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    keyEmitente = snapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gravarProdutos prod= new gravarProdutos();
        prod.gravarProdutos(qrcode, keyEmitente);





    }
}
