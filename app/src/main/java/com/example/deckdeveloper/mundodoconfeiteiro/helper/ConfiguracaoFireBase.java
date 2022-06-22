package com.example.deckdeveloper.mundodoconfeiteiro.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFireBase {
    public static DatabaseReference referenciaFireBase;
    public static FirebaseAuth referenciaAuth;
    public static StorageReference refenciaStorage;

    // Retorna a referencia do FireBase
    public static DatabaseReference getFireBase(){
        if(referenciaFireBase == null){
            referenciaFireBase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFireBase;
    }
    // Retorna a referencia do Storage
    public static StorageReference getStorage(){
        if(refenciaStorage == null) {
            refenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return refenciaStorage;
    }
    // Retorna a referencia da Autenticacao
    public static FirebaseAuth getAuth(){
        if(referenciaAuth == null){
            referenciaAuth = FirebaseAuth.getInstance();
        }
        return referenciaAuth;
    }
}
