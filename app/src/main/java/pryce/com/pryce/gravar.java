package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class gravar {

    public String keyEmitente = null;
    public String keyProduto = null;
    private DatabaseReference mDatabaseEmitente;



    public void gravarEmitente(final String razao, final String cnpj, final String logradouro, final String bairro, final String numero, final String cidade, final String uf, final String lat, final String log){

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

                    mDatabaseEmitente.updateChildren(emitenteUpdates);


                } else {
                    emitente = new Emitente(razao, cnpj, logradouro, bairro, numero, cidade, uf, lat, log);
                    mDatabaseEmitente.push().setValue(emitente);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void gravarProdutos(final String descricao, final String valor, final String codigo, final String data, final String hora, final String cnpj, final String key){


        mDatabaseEmitente = FirebaseDatabase.getInstance().getReference("Emitente/"+key);
        mDatabaseEmitente.orderByChild("descricao").equalTo(descricao).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Produtos produtos = dataSnapshot.getValue(Produtos.class);



                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    keyProduto = snapshot.getKey();

                }
                if (dataSnapshot.exists()) {

                    Map<String, Object> produtosUpdates = new HashMap<>();
                    produtosUpdates.put(keyProduto + "/descricao", descricao);
                    produtosUpdates.put(keyProduto + "/valor", valor);
                    produtosUpdates.put(keyProduto + "/codigo", codigo);
                    produtosUpdates.put(keyProduto + "/data", data);
                    produtosUpdates.put(keyProduto + "/hora", hora);
                    mDatabaseEmitente.updateChildren(produtosUpdates);


                } else {
                    produtos = new Produtos(descricao, valor, codigo, data, hora);
                    mDatabaseEmitente.push().setValue(produtos);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
