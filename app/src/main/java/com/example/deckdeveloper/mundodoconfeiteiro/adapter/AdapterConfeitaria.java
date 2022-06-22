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
import com.example.deckdeveloper.mundodoconfeiteiro.model.Confeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterConfeitaria extends RecyclerView.Adapter<AdapterConfeitaria.MyViewHolder> {

    private List<Confeitaria> confeitaria;

    public AdapterConfeitaria(List<Confeitaria> confeitaria) {
        this.confeitaria = confeitaria;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_confeitaria, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Confeitaria confeitaria = this.confeitaria.get(i);
        holder.nomeEmpresa.setText(confeitaria.getNome());
        holder.categoria.setText(confeitaria.getCategoria() + " - ");
        holder.tempo.setText(confeitaria.getTempo() + " Min");
        holder.entrega.setText("R$ " + confeitaria.getTaxa().toString());

        //Carregar imagem
        String urlImagem = confeitaria.getURLImage();
        Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );

    }

    @Override
    public int getItemCount() {
        return confeitaria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEmpresa;
        TextView nomeEmpresa;
        TextView categoria;
        TextView tempo;
        TextView entrega;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textNomeEmpresa);
            categoria = itemView.findViewById(R.id.textCategoriaEmpresa);
            tempo = itemView.findViewById(R.id.textTempoEmpresa);
            entrega = itemView.findViewById(R.id.textEntregaEmpresa);
            imagemEmpresa = itemView.findViewById(R.id.imageEmpresa);
        }
    }
}
