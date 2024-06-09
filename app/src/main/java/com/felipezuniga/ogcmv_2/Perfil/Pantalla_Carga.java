package com.felipezuniga.ogcmv_2.Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.felipezuniga.ogcmv_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pantalla_Carga extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();

        int Tiempo = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*startActivity(new Intent(Pantalla_Carga.this, Login.class));
                finish();*/
                VerificarUsario();
            }
        }, Tiempo);

    }

    private  void VerificarUsario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            startActivity(new Intent(Pantalla_Carga.this, Menu.class ));
            finish();
        }else{
            startActivity(new Intent(Pantalla_Carga.this, Menu.class));
            finish();
        }
    }
}