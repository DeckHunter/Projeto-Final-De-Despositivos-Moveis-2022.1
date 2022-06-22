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

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterConfeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterProduto;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.listener.RecyclerItemClickListener;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Confeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private MaterialSearchView searchView;

    private RecyclerView recyclerConfeitaria;
    private List<Confeitaria> confeitarias = new ArrayList<>();

    private DatabaseReference firebaseRef;
    private AdapterConfeitaria adapterConfeitaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Inicializar();
        firebaseRef = ConfiguracaoFireBase.getFireBase();
        auth = ConfiguracaoFireBase.getAuth();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confeitarias");
        setSupportActionBar(toolbar);

        //Configurar recyclerView
        recyclerConfeitaria.setLayoutManager(new LinearLayoutManager(this));
        recyclerConfeitaria.setHasFixedSize(true);
        adapterConfeitaria = new AdapterConfeitaria(confeitarias);
        recyclerConfeitaria.setAdapter(adapterConfeitaria);

        //Recurperar Confeitarias
        RecuperarConfeitarias();

        //Configuração do Search View
        searchView.setHint("Pesquisar Confeitarias");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarConfeitaria(newText);
                return true;
            }
        });

        //Evento De Clique
        recyclerConfeitaria.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerConfeitaria,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Confeitaria confeitariaSelecionada = confeitarias.get(position);
                                Intent i = new Intent(HomeActivity.this, CardapioActivity.class);

                                i.putExtra("confeitaria", confeitariaSelecionada);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
        }));

    }

    private void pesquisarConfeitaria(String pesquisa){
        DatabaseReference confeitariaRef = firebaseRef
                .child("Confeitarias");
        Query query = confeitariaRef.orderByChild("nome")
                .startAt(pesquisa).endAt(pesquisa + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                confeitarias.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                    confeitarias.add(ds.getValue(Confeitaria.class));
                }

                adapterConfeitaria.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void RecuperarConfeitarias() {
        DatabaseReference confeitariaRef = firebaseRef.child("Confeitarias");
        confeitariaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                confeitarias.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                    confeitarias.add(ds.getValue(Confeitaria.class));
                }

                adapterConfeitaria.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Inicializar() {
        searchView = findViewById(R.id.materialSearchView);
        recyclerConfeitaria = findViewById(R.id.recyclerConfeitarias);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        //Configurar Botao De Pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void AbriConfiguracoes() {
        startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuario.class));
    }

    private void DeslogarUsuario() {
        try {
            auth.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
