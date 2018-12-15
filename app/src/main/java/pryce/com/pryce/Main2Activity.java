package pryce.com.pryce;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
    Button btnEmitente;
    private DatabaseReference mDatabaseEmitente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Emitente.keyEmitente = null;
        btnEmitente = (Button) findViewById(R.id.buttonEmitente);
        btnEmitente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(intent);
                new MTaskObterKeyEmitente().execute();

            }
        });


    }

    public void carregarTxt(String texto) {
        TextView text = (TextView) findViewById(R.id.textViewEmitente);
        text.setText(texto);

    }

    public class MTaskObterKeyEmitente extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {

            while (Emitente.keyEmitente == null) {
                mDatabaseEmitente = FirebaseDatabase.getInstance().getReference("Emitente");
                mDatabaseEmitente.orderByChild("cnpj").equalTo(Emitente.cnpjSelect).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Emitente emitente = dataSnapshot.getValue(Emitente.class);
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Emitente.keyEmitente = snapshot.getKey();
                                break;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            carregarTxt(Produtos.descricaoSelect);



        }
    }
}
