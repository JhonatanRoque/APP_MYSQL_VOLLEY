package com.fjar.app_mysql.ui.session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fjar.app_mysql.MainActivity;
import com.fjar.app_mysql.R;

public class InitSession extends AppCompatActivity {
    private Button btnRegistrar, btnIniciar, btnRecuperarUsuario, btnRecuperarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_session);

        //Instanciamos nuestros componentes
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarse);
        btnIniciar = (Button) findViewById(R.id.btnIngresar);
        btnRecuperarUsuario = (Button) findViewById(R.id.btnRecuperarUsuario);

        //Funcion de los botones
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cambiarVista = new Intent(InitSession.this, Register.class);
                startActivity(cambiarVista);
            }
        });
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vista = new Intent(InitSession.this, MainActivity.class);
                startActivity(vista);
            }
        });
        btnRecuperarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vista = new Intent(InitSession.this, RecuperarUsuario.class);
                startActivity(vista);
            }
        });
    }
}