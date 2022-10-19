package com.fjar.app_mysql.ui.session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fjar.app_mysql.MainActivity;
import com.fjar.app_mysql.R;

public class InitSession extends AppCompatActivity {
    private Button btnRegistrar, btnIniciar, btnRecuperarUsuario, btnRecuperarContrasena;
    private EditText usuario_correo, contrasena;
    private Switch holdSession;
    private DtoUsuario usuario = new DtoUsuario();
    private UsuarioCRUD CRUD = new UsuarioCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_session);usuario = new DtoUsuario();
        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String estado = sp.getString("estado", "");
        if(estado.equals("logON")){
            if(sp.contains("id")){
                String id = sp.getString("id", "");
                usuario.setId(Integer.parseInt(id));
                if(usuario.getId() > 0){
                    Toast.makeText(InitSession.this, "Su id es: " + usuario.getId(), Toast.LENGTH_SHORT).show();
                    Intent nueva = new Intent(InitSession.this, MainActivity.class);
                    startActivity(nueva);
                }
            }
        }

        //Instanciamos nuestros componentes
        usuario_correo = (EditText) findViewById(R.id.et_usuarioIS);
        contrasena = (EditText) findViewById(R.id.et_contrasenaIS);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarse);
        btnIniciar = (Button) findViewById(R.id.btnIngresar);
        btnRecuperarUsuario = (Button) findViewById(R.id.btnRecuperarUsuario);
        btnRecuperarContrasena = (Button) findViewById(R.id.btnRecuperarContrasena);
        holdSession = (Switch) findViewById(R.id.mantenerSession);



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
                String usu = usuario_correo.getText().toString();
                String contra = contrasena.getText().toString();

                if(usu.length() == 0){
                    usuario_correo.setError("Campo obligatorio");
                }else if(contra.length() == 0){
                    contrasena.setError("Campo obligatorio");
                }else {
                    if(usu.contains("@")){
                        usuario.setCorreo(usu);
                    }else{
                        usuario.setUsuario(usu);
                    }
                    usuario.setClave(contra);

                    CRUD.IniciarSesionUsu(InitSession.this, usuario, holdSession);
                    DtoUsuario usua = new DtoUsuario();
                    SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                    String estado = sp.getString("estado", "");
                    if(estado.equals("logON")){
                        if(sp.contains("id")){
                            String id = sp.getString("id", "");
                            usua.setId(Integer.parseInt(id));
                            if(usua.getId() > 0){
                                Toast.makeText(InitSession.this, "Su id es: " + usua.getId(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        Intent nueva = new Intent(InitSession.this, MainActivity.class);
                        startActivity(nueva);
                    }

                }
            }
        });
        btnRecuperarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vista = new Intent(InitSession.this, RecuperarUsuario.class);
                startActivity(vista);
            }
        });
        btnRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vista = new Intent(InitSession.this, RecuperarContrasena.class);
                startActivity(vista);
            }
        });
    }
}