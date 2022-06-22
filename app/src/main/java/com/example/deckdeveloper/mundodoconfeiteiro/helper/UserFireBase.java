package com.example.deckdeveloper.mundodoconfeiteiro.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserFireBase {
    public static String getIdUser(){
        FirebaseAuth autenticacao = ConfiguracaoFireBase.getAuth();
        return autenticacao.getCurrentUser().getUid();
    }
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFireBase.getAuth();
        return usuario.getCurrentUser();
    }
    public static boolean AtualizarTipoUser(String tipo){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build();
            user.updateProfile(profile);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
