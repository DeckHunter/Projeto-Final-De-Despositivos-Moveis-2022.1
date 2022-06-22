package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;

import com.example.deckdeveloper.mundodoconfeiteiro.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AbrirLogin();
            }
        },3000);
    }

    private void AbrirLogin(){
        Intent i = new Intent(MainActivity.this, Login_Activity.class);
        startActivity(i);
        finish();
    }

}
