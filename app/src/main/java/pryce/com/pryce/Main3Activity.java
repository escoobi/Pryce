package pryce.com.pryce;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;

public class Main3Activity extends AppCompatActivity {
    Button btnProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btnProduto = (Button) findViewById(R.id.buttonProduto);
        btnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MTaskProduto().execute();

            }
        });
    }

    public  class MTaskProduto extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... voids) {
            URL url = null;
            try {
                url = new URL(MainActivity.qrcode);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            obterProdutos prod = new obterProdutos();
            prod.carregaProdutos(url);
           /* for (int x = 0; x <= Produtos.qtdProd; x++) {
                if (Produtos.descricaoSelect != null && Produtos.valorSelect != null && Produtos.codigoSelect != null) {
                    gravarProdutos gravarProdut = new gravarProdutos();
                    gravarProdut.gravarProdutos(Produtos.descricaoSelect, Produtos.valorSelect, Produtos.codigoSelect);


                }
            }
*/

            return true;
        }

    }

}
