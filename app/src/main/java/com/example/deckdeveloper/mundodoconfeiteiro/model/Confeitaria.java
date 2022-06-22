package com.example.deckdeveloper.mundodoconfeiteiro.model;

import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Confeitaria implements Serializable {
    private String IdUsuario;
    private String URLImage;
    private Double Taxa;
    private String Tempo;
    private String Categoria;
    private String Nome;
    private String Endereco;

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public Confeitaria() {

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

    public Double getTaxa() {
        return Taxa;
    }

    public void setTaxa(Double taxa) {
        Taxa = taxa;
    }

    public String getTempo() {
        return Tempo;
    }

    public void setTempo(String tempo) {
        Tempo = tempo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void Salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference confeitariaRef = firebaseRef
                .child("Confeitarias")
                .child(getIdUsuario());
        confeitariaRef.setValue(this);
    }
}
