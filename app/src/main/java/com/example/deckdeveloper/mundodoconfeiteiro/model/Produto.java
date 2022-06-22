package com.example.deckdeveloper.mundodoconfeiteiro.model;

import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

public class Produto {
    private String IdUsuario;
    private String IdProduto;
    private String URLImage;
    private String Preco;
    private String Calorias;
    private String Descricao;
    private String Nome;

    public String getIdProduto() {
        return IdProduto;
    }

    public void setIdProduto(String idProduto) {
        IdProduto = idProduto;
    }

    public Produto() {
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference produtoRef = firebaseRef
                .child("Produtos");
        setIdProduto(produtoRef.push().getKey());
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getURLImage() {
        return URLImage;
    }

    public void setURLImage(String URLImage) {
        this.URLImage = URLImage;
    }

    public String getPreco() {
        return Preco;
    }

    public void setPreco(String preco) {
        Preco = preco;
    }

    public String getCalorias() {
        return Calorias;
    }

    public void setCalorias(String calorias) {
        Calorias = calorias;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference produtoRef = firebaseRef
                .child("Produtos")
                .child(getIdUsuario())
                .child(getIdProduto());
        produtoRef.setValue(this);

    }

    public void remover() {

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference produtoRef = firebaseRef
                .child("Produtos")
                .child(getIdUsuario())
                .child(getIdProduto());
        produtoRef.removeValue();

    }
}

