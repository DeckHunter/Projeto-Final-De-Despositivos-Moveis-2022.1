package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Confeitaria;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.Manifest.*;
import static android.Manifest.permission.*;

public class NovoProdutoConfeitaria extends AppCompatActivity {

    private EditText nome_produto, descricao_produto, calorias_produto, preço_produto;
    private ImageView imagem_produto;

    private ImageView camera;

    private static final int SELECAO_GALERIA = 200;
    private static final int SELECAO_CAMERA = 100;

    private String IDUser;
    private String URL_Img_Selecionada = "";

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_confeitaria);

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializar Compenentes
        Inicializar();

        IDUser = UserFireBase.getIdUser();
        storageReference = ConfiguracaoFireBase.getStorage();

        imagem_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {
                switch (requestCode){
                    case SELECAO_GALERIA :
                        Uri local_imagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                local_imagem);
                        break;
                    case SELECAO_CAMERA :
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if(imagem != null){
                    imagem_produto.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosDaImagem =  baos.toByteArray();

                    StorageReference imgRef = storageReference
                            .child("imagens")
                            .child("Produtos")
                            .child(IDUser + "jpeg");
                    UploadTask uploadTask = imgRef.putBytes(dadosDaImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(
                                    NovoProdutoConfeitaria.this,
                                    "Erro Ao Fazer Upload Da Imagem",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri uri = task.getResult();
                                    URL_Img_Selecionada = uri.toString();
                                }
                            });

                            Toast.makeText(
                                    NovoProdutoConfeitaria.this,
                                    "Sucesso Ao Fazer Upload Da Imagem",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void ValidarDadosProduto(View view){

        //Validar Se Os Campos Foram Preenchidos
        String nome = nome_produto.getText().toString();
        String descricao = descricao_produto.getText().toString();
        String calorias = calorias_produto.getText().toString();
        String preco = preço_produto.getText().toString();
        String img_produto = URL_Img_Selecionada;

        if(!nome.isEmpty()){
            if(!descricao.isEmpty()){
                if(!preco.isEmpty()){
                    if(!calorias.isEmpty()){
                        if(!img_produto.isEmpty()) {
                            Produto produto = new Produto();

                            produto.setIdUsuario(IDUser);
                            produto.setNome(nome);
                            produto.setCalorias(calorias);
                            produto.setPreco(preco);
                            produto.setDescricao(descricao);
                            produto.setURLImage(URL_Img_Selecionada);

                            produto.salvar();
                            finish();
                            ExibirMensagem("Produto Adicionado Com Sucesso");
                        }else{
                            ExibirMensagem("Adicione Uma Imagen");
                        }
                    }else{
                        ExibirMensagem("Digite As Calorias Do Produto");
                    }
                }else{
                    ExibirMensagem("Digite Uma Preço Pro Produto");
                }
            }else{
                ExibirMensagem("Digite Uma Descrição Pro Produto");
            }
        }else{
            ExibirMensagem("Digite Um Nome Pro Produto");
        }
    }

    private void ExibirMensagem(String Texto){
        Toast.makeText(
                NovoProdutoConfeitaria.this,
                Texto,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void Inicializar(){
        nome_produto = findViewById(R.id.nome_produto);
        preço_produto = findViewById(R.id.preco_produto);
        calorias_produto = findViewById(R.id.calorias_produto);
        descricao_produto = findViewById(R.id.descricao_produto);
        imagem_produto = findViewById(R.id.imageProduto);
        camera = findViewById(R.id.abrir_Camera);
    }
}
