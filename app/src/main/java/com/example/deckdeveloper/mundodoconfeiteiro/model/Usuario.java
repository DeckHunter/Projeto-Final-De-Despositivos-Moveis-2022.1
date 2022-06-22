package com.example.deckdeveloper.mundodoconfeiteiro.model;

import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {
    private String nome;
    private String IdUsuario;
    private String Endereco;

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public void Salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFireBase();
        DatabaseReference UsuarioRef = firebaseRef
                .child("Usuarios")
                .child(getIdUsuario());
        UsuarioRef.setValue(this);
    }
}
