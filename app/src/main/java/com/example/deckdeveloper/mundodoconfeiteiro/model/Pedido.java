package com.example.deckdeveloper.mundodoconfeiteiro.model;

import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

public class Pedido {

    private String IdUsusario;
    private String IdConfeitaria;
    private String IdPedido;
    private String endereco;
    private String nome;
    private List<Item_Pedido> itens;
    private Double total;
    private String status = "pendente";
    private int metodoPagamento;
    private String observacao;

    public Pedido() {
    }
    public Pedido(String IdUser, String idConfeitaria) {
        setIdUsusario(IdUser);
        setIdConfeitaria(idConfeitaria);

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(IdConfeitaria)
                .child(IdUser);
        setIdPedido(pedidoRef.push().getKey());
    }
    public String getIdUsusario() {
        return IdUsusario;
    }

    public void setIdUsusario(String idUsusario) {
        IdUsusario = idUsusario;
    }

    public String getIdConfeitaria() {
        return IdConfeitaria;
    }

    public void setIdConfeitaria(String idConfeitaria) {
        IdConfeitaria = idConfeitaria;
    }

    public String getIdPedido() {
        return IdPedido;
    }

    public void setIdPedido(String idRequisicao) {
        IdPedido = idRequisicao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Item_Pedido> getItens() {
        return itens;
    }

    public void setItens(List<Item_Pedido> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void Salvar() {

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(getIdConfeitaria())
                .child(getIdUsusario());
        pedidoRef.setValue(this);
    }

    public void Confirmar() {
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getIdConfeitaria())
                .child(getIdPedido());
        pedidoRef.setValue(this);
    }

    public void Remover() {

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(getIdConfeitaria())
                .child(getIdUsusario());
        pedidoRef.removeValue();
    }

    public void AtulizarStatus() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getIdConfeitaria())
                .child(getIdPedido());
        pedidoRef.updateChildren(status);
    }
}
