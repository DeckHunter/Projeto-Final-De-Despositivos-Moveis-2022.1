package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterProduto;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.listener.RecyclerItemClickListener;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConfeitariaActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();

    private String IDUser;

    private FirebaseAuth auth;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeitaria);

        //Inicializar Componentes
        Inicializar();
        auth = ConfiguracaoFireBase.getAuth();
        firebaseRef = ConfiguracaoFireBase.getFireBase();
        IDUser = UserFireBase.getIdUser();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confeitaria - Empresa");
        setSupportActionBar(toolbar);

        //Configurar recyclerView
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos,this);
        recyclerProdutos.setAdapter(adapterProduto);

        //Recuperar Produtos
        RecuperarProdutos();

        //Adicionar Evento De Click No RecyclerView
        recyclerProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerProdutos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Produto produtoSelecionado = produtos.get(position);
                                produtoSelecionado.remover();

                                Toast.makeText(
                                        ConfeitariaActivity.this,
                                        "Produto Removido Com Sucesso",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }));

    }

    private void RecuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef
                .child("Produtos")
                .child(IDUser);
        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confeitaria, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.Sair :
                DeslogarUsuario();
                break;
            case R.id.menuConfiguracoes :
                AbriConfiguracoes();
                break;
            case R.id.menuNovoProduto :
                AbrirNovoProduto();
                break;
            case R.id.menuPedidos :
                AbrirPedidos();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void AbrirPedidos() {
        startActivity(new Intent(ConfeitariaActivity.this, PedidosActivity.class));
    }

    private void AbriConfiguracoes() {
        startActivity(new Intent(ConfeitariaActivity.this, ConfiguracoesConfeitaria.class));
    }

    private void AbrirNovoProduto() {
        startActivity(new Intent(ConfeitariaActivity.this, NovoProdutoConfeitaria.class));
    }

    private void DeslogarUsuario() {
        try {
            auth.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Inicializar() {
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
    }
}
