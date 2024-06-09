package com.felipezuniga.ogcmv_2.Perfil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.felipezuniga.ogcmv_2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {


    EditText name,email,password;
    Button registerButton;
    TextView subtitle;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    //
    String nombre = "", correo = " ", contrasena = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Registrar");
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);


        name = findViewById(R.id.name);
        email = findViewById( R.id.email);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        subtitle = findViewById(R.id.subtitle);

        //Implementacion de Firebase BD
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        //Evento para Registrar usuario
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });

        subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cuando el usuario indique tengo una cuenta
                startActivity(new Intent(Registro.this, Login.class));
            }
        });
    }
private void ValidarDatos(){
        nombre = name.getText().toString();
        correo = email.getText().toString();
        contrasena = password.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this,"Ingrese nombre", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Ingrese correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(contrasena)){
            Toast.makeText(this,"Ingrese nombre", Toast.LENGTH_SHORT).show();
    }
    else {
        CrearCuenta();
        }
    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta");
        progressDialog.show();

        // Crear usuario en Firebase BD
        firebaseAuth.createUserWithEmailAndPassword(correo,contrasena)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        GuardarInformacion();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando su informacion");
        progressDialog.dismiss();

        //Obtener datos de usuario actual
        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("nombre", nombre);
        Datos.put("correo", correo);
        Datos.put("password", contrasena);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ogcm");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Cuenta creada con exito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this, Menu.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}