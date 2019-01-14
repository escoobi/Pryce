package pryce.com.pryce;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class gravarProdutos {

    public String keyProduto = null;
    private DatabaseReference mDatabaseProtudo;




    public void gravarProdutos(final String valorProdut) {

        mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("Emitente/" + Emitente.keyEmitente +"/" + Produtos.codigoSelect);


        try {
                mDatabaseProtudo.orderByChild("descricao").equalTo(Produtos.descricaoSelect).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshotProduto) {

                    try {
                        Produtos produtos = dataSnapshotProduto.getValue(Produtos.class);

                        for (DataSnapshot snapshott : dataSnapshotProduto.getChildren()) {
                            keyProduto = snapshott.getKey();

                        }


                        if (dataSnapshotProduto.exists()) {

                            Map<String, Object> produtosUpdates = new HashMap<>();
                            produtosUpdates.put(keyProduto + "/valor", Produtos.valorSelect);
                            mDatabaseProtudo.updateChildren(produtosUpdates);


                        }
                        else {
                            produtos = new Produtos(Produtos.valorSelect);
                            mDatabaseProtudo.setValue(produtos);
                        }
                    } catch (Exception e) {
                        String teste = e.toString();
                        teste = teste + "sdfsdf";

                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    String err = databaseError.toString();
                    err = err + "Mais ESSA============";
                }
            });
                String toba =null;
                toba = "buceta" + "chana";
                wait(119000);

        }
        catch (Exception ex){
            String error = null;
            error = ex.toString();
        }
    }
}
