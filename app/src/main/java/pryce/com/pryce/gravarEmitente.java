package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class gravarEmitente {
    public static String keyEmitente = null;
    private DatabaseReference mDatabaseEmitente;



    public void gravarEmitente(final String razao, final String cnpj, final String logradouro, final String bairro, final String numero, final String cidade, final String uf, final String lat, final String log, final String data, final String hora){

        mDatabaseEmitente = FirebaseDatabase.getInstance().getReference("Emitente");
        mDatabaseEmitente.orderByChild("cnpj").equalTo(cnpj).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Emitente emitente = dataSnapshot.getValue(Emitente.class);


                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    keyEmitente = snapshot.getKey();

                }
                if (dataSnapshot.exists()) {

                    Map<String, Object> emitenteUpdates = new HashMap<>();
                    emitenteUpdates.put(keyEmitente + "/bairro", bairro);
                    emitenteUpdates.put(keyEmitente + "/cidade", cidade);
                    emitenteUpdates.put(keyEmitente + "/cnpj", cnpj);
                    emitenteUpdates.put(keyEmitente + "/logradouro", logradouro);
                    emitenteUpdates.put(keyEmitente + "/numero", numero);
                    emitenteUpdates.put(keyEmitente + "/razao", razao);
                    emitenteUpdates.put(keyEmitente + "/uf", uf);
                    emitenteUpdates.put(keyEmitente + "/lat", lat);
                    emitenteUpdates.put(keyEmitente + "/log", log);
                    emitenteUpdates.put(keyEmitente + "/data", data);
                    emitenteUpdates.put(keyEmitente + "/hora", hora);

                    mDatabaseEmitente.updateChildren(emitenteUpdates);


                } else {
                    emitente = new Emitente(razao, cnpj, logradouro, bairro, numero, cidade, uf, lat, log, data, hora);
                    mDatabaseEmitente.push().setValue(emitente);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
