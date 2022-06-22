package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterConfeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.adapter.AdapterProduto;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.listener.RecyclerItemClickListener;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Confeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Item_Pedido;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Pedido;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity{

    private static Confeitaria confeitariaSelecionada;
    private ImageView imageConfeitariaCardapio;
    private TextView nomeconfeitariaCardapio;
    private RecyclerView recyclerCardapio;

    //private Confeitaria confeitariaSelecionada;
    private Pedido pedidoRecuperado;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private List<Item_Pedido> ItensCarrinho = new ArrayList<>();
    private DatabaseReference firebaseRef;

    private String IdConfeitaria;
    private String IdUsuarioLogado;
    private Usuario usuario;

    private AlertDialog dialog;

    private int QtdItensCarrinho;
    private Double totalCarrinho;

    private TextView textCarrinhoQtd;
    private TextView textCarrinhoTotal;

    private int caracteristicasDoPedido;

    private Button btn_localizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        //Inicializar Componentes
        Inicializar();
        firebaseRef = ConfiguracaoFireBase.getFireBase();
        IdUsuarioLogado = UserFireBase.getIdUser();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardapio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar Empresa Selecionada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            confeitariaSelecionada = (Confeitaria) bundle.getSerializable("confeitaria");
            nomeconfeitariaCardapio.setText(confeitariaSelecionada.getNome());

            IdConfeitaria = confeitariaSelecionada.getIdUsuario();

            String url = confeitariaSelecionada.getURLImage();
            Picasso.get().load(url).into(imageConfeitariaCardapio);

        }

        //Configurar recyclerView
        recyclerCardapio.setLayoutManager(new LinearLayoutManager(this));
        recyclerCardapio.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos,this);
        recyclerCardapio.setAdapter(adapterProduto);

        //Evento De Clique
        recyclerCardapio.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerCardapio,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                ConfirmarQuantidade(position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }));

        //Recuperar Produtos Para Empresa
        RecuperarProdutos();
        RecuperarDadosUsuarios();

        btn_localizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CardapioActivity.this, MapaActivity.class);
                startActivity(i);
            }
        });
    }
    public static String GetLocalizacao(){
        return confeitariaSelecionada.getEndereco();
}
    private void ConfirmarQuantidade(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Digite A Quantidade");

        EditText editQtd = new EditText(this);
        editQtd.setText("1");

        builder.setView(editQtd);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String quantidade = editQtd.getText().toString();

                Produto produtoSelecionado = produtos.get(position);

                Item_Pedido item_pedido = new Item_Pedido();
                item_pedido.setIdProduto(produtoSelecionado.getIdProduto());
                item_pedido.setNomeProduto(produtoSelecionado.getNome());
                item_pedido.setQuantidade(Integer.parseInt(quantidade));
                item_pedido.setPreco(Double.parseDouble(produtoSelecionado.getPreco()));
                ItensCarrinho.add(item_pedido);

                if(pedidoRecuperado == null){
                    pedidoRecuperado = new Pedido(IdUsuarioLogado,IdConfeitaria);
                }

                pedidoRecuperado.setNome(usuario.getNome());
                pedidoRecuperado.setEndereco(usuario.getEndereco());
                pedidoRecuperado.setItens(ItensCarrinho);

                pedidoRecuperado.Salvar();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void RecuperarDadosUsuarios() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando Dados")
                .setCancelable(false)
                .build();
        dialog.show();

        DatabaseReference usuarioRef = firebaseRef
                .child("Usuarios")
                .child(IdUsuarioLogado);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    usuario = snapshot.getValue(Usuario.class);
                }
                RecuperarPedido();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RecuperarPedido() {

        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(IdConfeitaria)
                .child(IdUsuarioLogado);

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                QtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                ItensCarrinho = new ArrayList<>();

                if(snapshot.getValue() != null){
                    pedidoRecuperado = snapshot.getValue(Pedido.class);
                    ItensCarrinho = pedidoRecuperado.getItens();

                    for (Item_Pedido item_pedido : ItensCarrinho){

                        int QTD = item_pedido.getQuantidade();
                        Double preco = item_pedido.getPreco();

                        totalCarrinho += (QTD * preco);
                        QtdItensCarrinho += QTD;
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                textCarrinhoQtd.setText("Quantidade : " + String.valueOf(QtdItensCarrinho));
                textCarrinhoTotal.setText("R$ " + df.format(totalCarrinho));

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Inicializar() {
        imageConfeitariaCardapio = findViewById(R.id.imageConfeitariaCardapio);
        nomeconfeitariaCardapio = findViewById(R.id.textNomeConfeitariaCardapio);
        recyclerCardapio = findViewById(R.id.recyclerCardapio);

        textCarrinhoQtd = findViewById(R.id.textCarrinhoQtd);
        textCarrinhoTotal = findViewById(R.id.textCarrinhoPreco);

        btn_localizacao =  findViewById(R.id.localizacao_Mapa);
    }
    private void RecuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef
                .child("Produtos")
                .child(IdConfeitaria);
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
        inflater.inflate(R.menu.menu_cardapio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuPedido :
                ConfirmarPedido();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ConfirmarPedido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Metodo De Recebimento");

        CharSequence[] itens = new CharSequence[]{
            "Entrega a Domicílio e Pagar Com Cartão", "Entrega a Domicílio e Pagar Com Dinheiro","Retirar no Estabelecimento"
        };
        builder.setSingleChoiceItems(itens, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                caracteristicasDoPedido = i;
            }
        });

        EditText observacao = new EditText(this);
        observacao.setHint("Digite uma observação");
        builder.setView(observacao);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String obs = observacao.getText().toString();
                pedidoRecuperado.setMetodoPagamento(caracteristicasDoPedido);
                pedidoRecuperado.setObservacao(obs);
                pedidoRecuperado.setStatus("Confirmado");
                pedidoRecuperado.Confirmar();
                pedidoRecuperado.Remover();
                pedidoRecuperado = null;
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
