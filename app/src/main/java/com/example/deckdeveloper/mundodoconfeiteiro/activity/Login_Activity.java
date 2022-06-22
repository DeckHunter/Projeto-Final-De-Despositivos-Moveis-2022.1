package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private Button btmAcessar;
    private EditText campo_email;
    private EditText campo_senha;
    private Switch switch_escolha;
    private Switch switch_tipo_de_user;
    private LinearLayout Tipo_Usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        getSupportActionBar();

        autenticacao = ConfiguracaoFireBase.getAuth();

        //Inicializar Componentes
        Inicializar();

        //Verificar Usuario Logado
        VerificarUsuarioLogado();

        switch_escolha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){//Sabemos que é uma Confeitaria
                    Tipo_Usuario.setVisibility(View.VISIBLE);
                }else{//Sabemos que é um Usuario
                    Tipo_Usuario.setVisibility(View.GONE);
                }
            }
        });

        btmAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = campo_email.getText().toString();
                String senha = campo_senha.getText().toString();

                //Verificar se tem algum campo vazio
                if(!email.isEmpty()){
                    if(!senha.isEmpty()){
                        //Verificar o Estado Do Switch De Escolha
                        if(switch_escolha.isChecked()){
                            autenticacao.createUserWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Tratamento De Excecoes
                                    if(task.isSuccessful()){
                                        Toast.makeText(
                                                Login_Activity.this,
                                                "Cadastrado Com Sucesso!",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        String tipoUsuario = getTipoUser();
                                        UserFireBase.AtualizarTipoUser(tipoUsuario);

                                        AbrirTelaInicial(tipoUsuario);

                                    }else{
                                        String errorExcecao = "";
                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e){
                                            errorExcecao = "Digite Um Senha Mais Forte";
                                        }catch (FirebaseAuthInvalidCredentialsException e) {
                                            errorExcecao = "Digite Um E-mail Validido";
                                        }catch (FirebaseAuthUserCollisionException e) {
                                            errorExcecao = "Esta Conta Já Foi Cadastrada";
                                        } catch (Exception e) {
                                            errorExcecao = "Ao Cadastrar Ususario" + e.getMessage();
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(
                                                Login_Activity.this,
                                                "Erro: "+errorExcecao,
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }
                            });
                        }else{
                            autenticacao.signInWithEmailAndPassword(email,senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(
                                                Login_Activity.this,
                                                "Logado Com Sucesso",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        String tipoUser = task.getResult().getUser().getDisplayName();
                                        AbrirTelaInicial(tipoUser);
                                    }else{
                                        Toast.makeText(
                                                Login_Activity.this,
                                                "Erro Ao Fazer Login",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }
                            });
                        }
                    }else{
                        Toast.makeText(
                                Login_Activity.this,
                                "Preencha O Campo Senha!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }else{
                    Toast.makeText(
                            Login_Activity.this,
                            "Preencha O Campo E-mail!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void VerificarUsuarioLogado() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            String tipoUser = usuarioAtual.getDisplayName();
            AbrirTelaInicial(tipoUser);
        }
    }

    private void Inicializar(){
        btmAcessar = findViewById(R.id.acessar);
        campo_email = findViewById(R.id.email);
        campo_senha = findViewById(R.id.senha);
        switch_escolha = findViewById(R.id.escolha);
        switch_tipo_de_user = findViewById(R.id.tipo_de_user);
        Tipo_Usuario = findViewById(R.id.Linear_Tipo_Usuario);
    }
    private void AbrirTelaInicial(String tipoUser){
        if(tipoUser.equals("C")){
            startActivity(new Intent(getApplicationContext(),ConfeitariaActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }

    private String getTipoUser(){
        return switch_tipo_de_user.isChecked() ? "C" : "U";
    }
}
