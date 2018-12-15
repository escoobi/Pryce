package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;




public class gravarProdutos {

    public String keyProduto = null;
    private DatabaseReference mDatabaseProtudo;
    public String linha = null;
    public static int qtditens = 0;


    public void gravarProdutos(final  String descrProdut, final String valorProdut, final String codigoProdut) {
        mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("Emitente/" + Emitente.keyEmitente);
        mDatabaseProtudo.orderByChild("descricao").equalTo(Produtos.descricaoSelect).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotProduto) {

                Produtos produtos = dataSnapshotProduto.getValue(Produtos.class);




                for (DataSnapshot snapshott : dataSnapshotProduto.getChildren()) {
                    keyProduto = snapshott.getKey();

                }


                if (dataSnapshotProduto.exists()) {

                    Map<String, Object> produtosUpdates = new HashMap<>();
                    produtosUpdates.put(keyProduto + "/descricao", Produtos.descricaoSelect);
                    produtosUpdates.put(keyProduto + "/valor", Produtos.valorSelect);
                    produtosUpdates.put(keyProduto + "/codigo", Produtos.codigoSelect);
                    mDatabaseProtudo.updateChildren(produtosUpdates);


                } else {

                    produtos = new Produtos(Produtos.descricaoSelect, Produtos.valorSelect, Produtos.codigoSelect);
                    mDatabaseProtudo.push().setValue(produtos);

                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
