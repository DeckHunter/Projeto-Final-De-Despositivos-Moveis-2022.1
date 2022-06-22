package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.deckdeveloper.mundodoconfeiteiro.R;

public class MapaActivity extends AppCompatActivity {

    private String endereco;

    private Button btn_AbrirMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        endereco = CardapioActivity.GetLocalizacao();
        btn_AbrirMapa = findViewById(R.id.abrirMapa);

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Localização");
        setSupportActionBar(toolbar);

        btn_AbrirMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mapa(endereco);
            }
        });

    }
    private void Mapa(String e){
        WebView wv = findViewById(R.id.webv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + e);
    }
}
