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




    public void gravarEmitente(final String fantasia, final String razao, final String cnpj, final String logradouro, final String bairro, final String numero, final String lat, final String log){

        mDatabaseEmitente = FirebaseDatabase.getInstance().getReference();
        mDatabaseEmitente.orderByChild("cnpj").equalTo(cnpj).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Emitente emitente = dataSnapshot.getValue(Emitente.class);




                    if (dataSnapshot.exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            keyEmitente = snapshot.getKey();

                        }

                        Map<String, Object> emitenteUpdates = new HashMap<>();
                        emitenteUpdates.put(keyEmitente + "/bairro", bairro);
                        emitenteUpdates.put(keyEmitente + "/cnpj", cnpj);
                        emitenteUpdates.put(keyEmitente + "/logradouro", logradouro);
                        emitenteUpdates.put(keyEmitente + "/numero", numero);
                        emitenteUpdates.put(keyEmitente + "/fantasia", fantasia);
                        emitenteUpdates.put(keyEmitente + "/razzao", razao);
                        emitenteUpdates.put(keyEmitente + "/lat", lat);
                        emitenteUpdates.put(keyEmitente + "/log", log);


                        mDatabaseEmitente.updateChildren(emitenteUpdates);



                    } else {
                        emitente = new Emitente(fantasia, razao, cnpj, logradouro, bairro, numero, lat, log);
                        mDatabaseEmitente.push().setValue(emitente);
                        //*********************************************************************************

                        //*********************************************************************************




                   }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
