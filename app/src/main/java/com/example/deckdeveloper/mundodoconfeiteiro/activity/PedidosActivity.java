package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterPedido;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.listener.RecyclerItemClickListener;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView pedidos;
    private AdapterPedido adapterPedido;
    private List<Pedido> ListaPedidos = new ArrayList<>();
    private AlertDialog dialog;

    private DatabaseReference firebaseRef;
    private String IDConfeitaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //Inicializar
        Inicializar();
        firebaseRef = ConfiguracaoFireBase.getFireBase();
        IDConfeitaria = UserFireBase.getIdUser();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurar recyclerView
        pedidos.setLayoutManager(new LinearLayoutManager(this));
        pedidos.setHasFixedSize(true);
        adapterPedido = new AdapterPedido(ListaPedidos);
        pedidos.setAdapter(adapterPedido);
        
        RecuperarPedidos();

        //Adicionando Evento de Click no recyclerView
        pedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        pedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Pedido pedido = ListaPedidos.get(position);
                                pedido.setStatus("finalizado");
                                pedido.AtulizarStatus();
                                RecuperarPedidos();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        })
                );
    }

    private void RecuperarPedidos() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando Dados")
                .setCancelable(true)
                .build();
        dialog.show();

        DatabaseReference pedidosRef = firebaseRef
                .child("pedidos")
                .child(IDConfeitaria);
        Query pedidoPesquisa = pedidosRef.orderByChild("status")
                .equalTo("Confirmado");
        pedidoPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListaPedidos.clear();
                if(snapshot.getValue() != null){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Pedido pedido = ds.getValue(Pedido.class);
                        ListaPedidos.add(pedido);
                    }
                    adapterPedido.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Inicializar() {
        pedidos = findViewById(R.id.recyclerPedido);
    }
}
