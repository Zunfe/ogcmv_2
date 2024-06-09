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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //Declaracion de componentes del layout
    EditText password_login, email_login;
    Button register, loginButton;
    TextView forgotPassword;
    //Elementos de uso para la logica del inicio de sesion
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    //Validacion de los datos en los EditText
    String correo, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        register = findViewById(R.id.register);

        //Implementacion de Firebase BD
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        // Evento que valida los datos del usuario al Iniciar sesion
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });

        /*forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Pantalla_Carga.class));
            }
        });*/

        // Evento que envia al usuario al Formulario de Registro
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
    }

    private void ValidarDatos() {

        correo = email_login.getText().toString();
        contrasena = password_login.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo Invalido!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this,"Ingrese su contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            LoginUsuario();
        }
    }

    private void LoginUsuario() {
        progressDialog.setMessage("Iniciando sesion...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, Menu.class));
                            Toast.makeText(Login.this, "Bienvenido(a): "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Verifique si el correo y contraseña son los correctos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}