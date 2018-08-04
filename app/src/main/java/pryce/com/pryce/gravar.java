package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class gravar {

    private DatabaseReference mDatabase;

    public void gravarEmitente(final String razao, final String cnpj, final String logradouro, final String bairro, final String numero, final String cidade, final String uf, final String lat, final String log){

        mDatabase = FirebaseDatabase.getInstance().getReference("Emitente");
        mDatabase.orderByChild("cnpj").equalTo(cnpj).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Emitente emitente = dataSnapshot.getValue(Emitente.class);

                String key = null;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    key = snapshot.getKey();

                }
                if (dataSnapshot.exists()) {

                    Map<String, Object> emitenteUpdates = new HashMap<>();
                    emitenteUpdates.put(key + "/bairro", bairro);
                    emitenteUpdates.put(key + "/cidade", cidade);
                    emitenteUpdates.put(key + "/cnpj", cnpj);
                    emitenteUpdates.put(key + "/logradouro", logradouro);
                    emitenteUpdates.put(key + "/numero", numero);
                    emitenteUpdates.put(key + "/razao", razao);
                    emitenteUpdates.put(key + "/uf", uf);
                    emitenteUpdates.put(key + "/lat", lat);
                    emitenteUpdates.put(key + "/log", log);

                    mDatabase.updateChildren(emitenteUpdates);


                } else {
                    emitente = new Emitente(razao, cnpj, logradouro, bairro, numero, cidade, uf, lat, log);
                    mDatabase.push().setValue(emitente);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void gravarProdutos(final String descricao, final String valor, final String codigo, final String data, final String hora){

        mDatabase = FirebaseDatabase.getInstance().getReference("Produto");
        mDatabase.orderByChild("descricao").equalTo(descricao).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Produtos produtos = dataSnapshot.getValue(Produtos.class);


                String key = null;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    key = snapshot.getKey();

                }
                if (dataSnapshot.exists()) {

                    Map<String, Object> produtosUpdates = new HashMap<>();
                    produtosUpdates.put(key + "/descricao", descricao);
                    produtosUpdates.put(key + "/valor", valor);
                    produtosUpdates.put(key + "/codigo", codigo);
                    produtosUpdates.put(key + "/data", data);
                    produtosUpdates.put(key + "/hora", hora);
                    mDatabase.updateChildren(produtosUpdates);


                } else {
                    produtos = new Produtos(descricao, valor, codigo, data, hora);
                    mDatabase.push().setValue(produtos);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
