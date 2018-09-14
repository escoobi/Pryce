package pryce.com.pryce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    public BufferedReader br;

    public void gravarProdutos(String urls, String chaveEmitente){
        Produtos produtos = new Produtos();
        URL url = null;
        try {
            url = new URL(urls);


            if(urls.length() == 124) {
                StringBuilder numeros = new StringBuilder();


                br = new BufferedReader(new InputStreamReader(url.openStream()));



                String minhaLinha;
                while ((minhaLinha = br.readLine()) != null) {
                    numeros.append(minhaLinha).append("\n");

                }

                Scanner scanProdutos = new Scanner(numeros.toString());
                int numLinha = 0;
                int produtoLinha = 157;
                int minhaLinhaValor = 159;
                int qtdTotal = produtoLinha + (6 * qtditens);

                while (scanProdutos.hasNextLine()) {
                    linha = scanProdutos.nextLine();


                    // Pegar qtd itens
                    if (linha.contains("Qtd. total de itens:")) {
                        linha = linha.substring(59, linha.indexOf("</span>"));
                        qtditens = Integer.parseInt(linha);
                    }
                    for (int x = 0; x <= qtditens; x++) {
                        if (qtdTotal != numLinha) {
                            // Obter itens
                            if (numLinha == produtoLinha) {

                                produtos.descricao = linha.substring(38, linha.indexOf("</span>"));
                                produtos.codigo = linha.substring(linha.indexOf(":") + 1, linha.length());
                                produtos.codigo = produtos.codigo.substring(0, produtos.codigo.indexOf(")"));
                                produtos.codigo = produtos.codigo.trim();
                                produtoLinha = produtoLinha + 6;
                                try {
                                    Thread.sleep(4000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mDatabaseProtudo = FirebaseDatabase.getInstance().getReference("Emitente/" + chaveEmitente);
                                mDatabaseProtudo.orderByChild("descricao").equalTo(produtos.descricao).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Produtos produtos = dataSnapshot.getValue(Produtos.class);

                                        if (dataSnapshot.exists()) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                keyProduto = snapshot.getKey();

                                            }

                                            Map<String, Object> produtosUpdates = new HashMap<>();
                                            produtosUpdates.put(keyProduto + "/descricao", Produtos.descricaoSelect);
                                            produtosUpdates.put(keyProduto + "/valor", Produtos.valorSelect);
                                            produtosUpdates.put(keyProduto + "/codigo", Produtos.codigoSelect);
                                            produtosUpdates.put(keyProduto + "/data", Produtos.dataSelect);
                                            produtosUpdates.put(keyProduto + "/hora", Produtos.horaSelect);
                                            mDatabaseProtudo.updateChildren(produtosUpdates);


                                        } else {

                                            produtos = new Produtos(Produtos.descricaoSelect, Produtos.valorSelect, Produtos.codigoSelect, Produtos.dataSelect, Produtos.horaSelect);
                                            mDatabaseProtudo.push().setValue(produtos);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            // Obter valor itens
                         /*   if (numLinha == minhaLinhaValor) {

                                produtos.valor = linha.substring(linha.indexOf("Vl. Unit.:</strong>&nbsp;") + 25, linha.indexOf("</span></td>"));
                                produtos.valor = produtos.valor.replace(",", ".");
                                Produtos.valorSelect = produtos.valor;
                                minhaLinhaValor = minhaLinhaValor + 6;

                            }*/

                        }

                    }


                    numLinha++;
                }

                scanProdutos.close();
                br.close();
            }
            else{
             //   alert("Erro ao ler o QRCode.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }






        }


}
