package com.example.deckdeveloper.mundodoconfeiteiro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Item_Pedido;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Pedido;

import java.util.ArrayList;
import java.util.List;

public class AdapterPedido extends RecyclerView.Adapter<AdapterPedido.MyViewHolder> {

    private List<Pedido> pedidos;

    public AdapterPedido(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedidos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Pedido pedido = pedidos.get(i);
        holder.nome.setText( pedido.getNome() );
        holder.endereco.setText( "Endereço: "+pedido.getEndereco() );
        holder.observacao.setText( "Obs: "+ pedido.getObservacao() );

        List<Item_Pedido> itens = new ArrayList<>();
        itens = pedido.getItens();
        String descricaoItens = "";

        int numeroItem = 1;
        Double total = 0.0;

        for( Item_Pedido itemPedido : itens ){

            int qtde = itemPedido.getQuantidade();
            Double preco = itemPedido.getPreco();
            total += (qtde * preco);

            String nome = itemPedido.getNomeProduto();
            descricaoItens += numeroItem + ") " + nome + " / (" + qtde + " x R$ " + preco + ") \n";
            numeroItem++;
        }
        descricaoItens += "Total: R$ " + total;
        holder.itens.setText(descricaoItens);

        int caracteristicasDoPedido = pedido.getMetodoPagamento();
        String pagamento = "";
        if(caracteristicasDoPedido == 0){
            pagamento = "Metodo De Recebimento";
        }
        if(caracteristicasDoPedido == 1){
            pagamento = "Entrega a Domicílio e Pagar Com Dinheiro";
        }
        if(caracteristicasDoPedido == 2){
            pagamento = "Retirar no Estabelecimento";
        }
        holder.pgto.setText( "Detalhes: " + pagamento );

    }

    @Override
    public int getItemCount() {

        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView endereco;
        TextView pgto;
        TextView observacao;
        TextView itens;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textPedidoNome);
            endereco = itemView.findViewById(R.id.textPedidoEndereco);
            pgto = itemView.findViewById(R.id.textPedidoPgto);
            observacao = itemView.findViewById(R.id.textPedidoObs);
            itens = itemView.findViewById(R.id.textPedidoItens);
        }
    }

}
