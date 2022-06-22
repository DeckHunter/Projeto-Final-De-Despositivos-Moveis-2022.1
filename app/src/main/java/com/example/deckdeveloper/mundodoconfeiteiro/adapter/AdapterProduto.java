package com.example.deckdeveloper.mundodoconfeiteiro.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;

    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Produto produto = produtos.get(i);
        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());
        holder.preco.setText("R$ " + produto.getPreco());
        holder.calorias.setText( produto.getCalorias()+ " Kcal");

        //Pegar Imagem
        String imagem = produto.getURLImage();
        Picasso.get().load(imagem).into(holder.imagem_Produto);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView descricao;
        TextView preco;
        TextView calorias;
        ImageView imagem_Produto;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeRefeicao);
            descricao = itemView.findViewById(R.id.textDescricaoRefeicao);
            preco = itemView.findViewById(R.id.textPreco);
            calorias = itemView.findViewById(R.id.textCaloria_da_refeicao);
            imagem_Produto = itemView.findViewById(R.id.imagem_refeicao);
        }
    }
}
