package com.example.deckdeveloper.mundodoconfeiteiro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deckdeveloper.mundodoconfeiteiro.R;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.ConfiguracaoFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.helper.UserFireBase;
import com.example.deckdeveloper.mundodoconfeiteiro.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConfiguracoesUsuario extends AppCompatActivity implements LocationListener {

    private EditText nome_Usuario;
    private EditText endereco_Usuario;
    private String IdUsuario;
    private DatabaseReference firebaseRef;

    private Button btm_Localizacao;
    private final int GPS_REQUEST = 100;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Inicializar
        Inicializar();
        IdUsuario = UserFireBase.getIdUser();
        firebaseRef = ConfiguracaoFireBase.getFireBase();

        //Configuração Da ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações Usuario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //Recuperar Dados Usuario
        RecuperarDadosUsuario();

        btm_Localizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ConfiguracoesUsuario.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_REQUEST);
                }else{
                    getLocation();
                }
            }
        });
    }

    private void getLocation() {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void RecuperarDadosUsuario() {

        DatabaseReference usuarioRef = firebaseRef
                .child("Usuarios")
                .child(IdUsuario);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    nome_Usuario.setText(usuario.getNome());
                    endereco_Usuario.setText(usuario.getEndereco());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Inicializar() {
        nome_Usuario = findViewById(R.id.editUserNome);
        endereco_Usuario = findViewById(R.id.editUserEndereco);
        btm_Localizacao = findViewById(R.id.btn_Localizacao);
    }

    public void ValidarDadosUsuario(View view){
        //Validar Se Os Campos Foram Preenchidos
        String nome = nome_Usuario.getText().toString();
        String endereco = endereco_Usuario.getText().toString();

        if(!nome.isEmpty()){
            if(!endereco.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(IdUsuario);
                usuario.setNome(nome);
                usuario.setEndereco(endereco);
                usuario.Salvar();

                ExibirMensagem("Dados Atualizados Com Sucesso");
                finish();

            }else{
                ExibirMensagem("Digite Seu Endereço");
            }
        }else{
            ExibirMensagem("Digite Seu Nome");
        }
    }

    private void ExibirMensagem(String Texto){
        Toast.makeText(
                ConfiguracoesUsuario.this,
                Texto,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> lista_enderecos =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if(lista_enderecos != null && lista_enderecos.size() > 0){
                Address address = lista_enderecos.get(0);
                endereco_Usuario.setText(address.getAddressLine(0));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
