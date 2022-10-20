package com.fjar.app_mysql.ui.session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fjar.app_mysql.R;

public class RecuperarUsuario extends AppCompatActivity {
    private EditText correo, pregunta, respuesta;
    private Button btngetPregunta, btnCheckRespuesta;
    private TextView resultado, usuario;
    private UsuarioCRUD CRUD = new UsuarioCRUD();
    private DtoUsuario usuarioDto = new DtoUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_usuario);
        correo = (EditText) findViewById(R.id.et_correo);
        pregunta = (EditText) findViewById(R.id.et_preguntaSeguridad);
        respuesta = (EditText) findViewById(R.id.et_respuestaSeguridad);
        btngetPregunta = (Button) findViewById(R.id.btnBuscarCorreo); //Al presionar este boton debe de cargar la pregunta relacionada al correo introducido
        btnCheckRespuesta = (Button) findViewById(R.id.btnRecuperarUsu); //Al presionar este boton debe verificar si la respuesta es corecta y deolver el usuario
        resultado = (TextView) findViewById(R.id.resultado);
        usuario = (TextView) findViewById(R.id.tvUsuario);

        btngetPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correoStr = correo.getText().toString();
                if(correoStr.length() == 0){
                    correo.setError("Introduzca un correo");
                }else {
                    usuarioDto.setCorreo(correoStr);
                    CRUD.getPregunta(RecuperarUsuario.this, usuarioDto, pregunta);
                }
            }
        });
        btnCheckRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String preguntatmp = pregunta.getText().toString();
                String respuestatmp = respuesta.getText().toString();
                if(usuarioDto.getCorreo().length() == 0){
                    correo.setError("Introduzca un correo");
                }else if(preguntatmp.length() == 0){
                    pregunta.setError("¡Debe buscar su pregunta antes!");
                }else if (preguntatmp.length() == 0){
                    respuesta.setError("Introduzca la respuesta a su pregunta.");
                }else {
                    usuarioDto.setPregunta(preguntatmp);
                    usuarioDto.setRespuesta(respuestatmp);
                    CRUD.getUsuario(RecuperarUsuario.this, usuarioDto, usuario);
                }
            }
        });
    }
}