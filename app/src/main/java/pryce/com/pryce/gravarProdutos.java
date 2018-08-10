package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static pryce.com.pryce.obterProdutos.cnpj;
import static pryce.com.pryce.obterNfc.hora;
import static pryce.com.pryce.obterNfc.data;
import static pryce.com.pryce.obterProdutos.cod;
import static pryce.com.pryce.obterProdutos.descr;
import static pryce.com.pryce.obterProdutos.val;


public class gravarProdutos {

    public String keyProduto = null;
    public String keyEmitente = null;
    private DatabaseReference mDatabaseProtudo;
    private DatabaseReference mDatabaseEmitente;



    public String obterKeyEmitente (final String cnpj){

        mDatabaseEmitente = FirebaseDatabase.getInstance().getReference("Emitente");
        mDatabaseEmitente.orderByChild("cnpj").equalTo(cnpj).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Emitente emitente = dataSnapshot.getValue(Emitente.class);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    keyEmitente = snapshot.getKey();

                }

                gravarProdutos(descr, val, cod, data, hora, cnpj);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return keyEmitente;
    }

    public void gravarProdutos(final String descricao, final String valor, final String codigo, final String data, final String hora, final String cnpj){



            mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("Emitente/" + obterKeyEmitente(cnpj));
            mDatabaseProtudo.orderByChild("descricao").equalTo(descricao).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Produtos produtos = dataSnapshot.getValue(Produtos.class);


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        keyProduto = snapshot.getKey();

                    }


                    if (dataSnapshot.exists()) {

                        Map<String, Object> produtosUpdates = new HashMap<>();
                        produtosUpdates.put(keyProduto + "/descricao", descricao);
                        produtosUpdates.put(keyProduto + "/valor", valor);
                        produtosUpdates.put(keyProduto + "/codigo", codigo);
                        produtosUpdates.put(keyProduto + "/data", data);
                        produtosUpdates.put(keyProduto + "/hora", hora);
                        mDatabaseProtudo.updateChildren(produtosUpdates);


                    } else {

                        produtos = new Produtos(descricao, valor, codigo, data, hora);
                        mDatabaseProtudo.push().setValue(produtos);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


}
