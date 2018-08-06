package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static pryce.com.pryce.gravarEmitente.keyEmitente;

public class gravarProdutos {

    public String keyProduto = null;
    private DatabaseReference mDatabaseEmitente;

    public void gravarProdutos(final String descricao, final String valor, final String codigo, final String data, final String hora, final String cnpj){


        mDatabaseEmitente = FirebaseDatabase.getInstance().getReference("Emitente/"+keyEmitente);
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
