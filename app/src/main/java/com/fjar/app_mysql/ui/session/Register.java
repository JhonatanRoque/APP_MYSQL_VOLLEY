package com.fjar.app_mysql.ui.session;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fjar.app_mysql.R;

public class Register extends AppCompatActivity {
    private EditText nombre, apellido, usuario, correo, clave, respuesta;
    private Spinner tipoUsuario, estado, pregunta;
    private Button btnRegistrar, btnLimpiar;
    private UsuarioCRUD CRUD = new UsuarioCRUD();
    private DtoUsuario dtoUsuario = new DtoUsuario();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nombre = (EditText) findViewById(R.id.et_nombres);
        apellido = (EditText) findViewById(R.id.et_apellidos);
        usuario = (EditText) findViewById(R.id.et_usuario);
        correo = (EditText) findViewById(R.id.et_correo);
        clave = (EditText) findViewById(R.id.et_contrasena);
        respuesta = (EditText) findViewById(R.id.et_respuesta);
        tipoUsuario = (Spinner) findViewById(R.id.spn_tipoUsuario);
        estado = (Spinner) findViewById(R.id.spn_estadoRG);
        pregunta = (Spinner) findViewById(R.id.spn_pregunta);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnLimpiar = (Button) findViewById(R.id.btnNew);

        //Obtener datos de los spinners
        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position > 0){
                    String item = String.valueOf(tipoUsuario.getSelectedItem());
                    String s[] = item.split("-");
                    dtoUsuario.setTipo(Integer.parseInt(s[0].trim()));
                    Toast.makeText(Register.this, "Tipo usuario: " + dtoUsuario.getTipo(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = String.valueOf(estado.getSelectedItem());
                if(position > 0){
                    dtoUsuario.setEstado(Integer.parseInt(item));
                    Toast.makeText(Register.this, "Estado: " + dtoUsuario.getEstado(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = String.valueOf(pregunta.getSelectedItem());
                if(position > 0){
                    dtoUsuario.setPregunta(item);
                    Toast.makeText(Register.this, "Estado: " + dtoUsuario.getPregunta(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pasar datos de los edit text a variables String
                dtoUsuario.setNombre(nombre.getText().toString());
                dtoUsuario.setApellido(apellido.getText().toString());
                dtoUsuario.setUsuario(usuario.getText().toString());
                dtoUsuario.setCorreo(correo.getText().toString());
                dtoUsuario.setClave(clave.getText().toString());
                dtoUsuario.setRespuesta(respuesta.getText().toString());
                if(dtoUsuario.getNombre().length() == 0){
                    nombre.setError("Campo obligatorio.");
                }else if(dtoUsuario.getApellido().length() == 0){
                    apellido.setError("Campo obligatorio.");
                }else if(dtoUsuario.getUsuario().length() == 0){
                    usuario.setError("Campo obligatorio.");
                }else if(dtoUsuario.getCorreo().length() == 0){
                    correo.setError("Campo obligatorio.");
                }else if(dtoUsuario.getClave().length() == 0){
                    clave.setError("Campo obligatorio.");
                }else if(dtoUsuario.getRespuesta().length() == 0){
                    respuesta.setError("Campo obligatorio.");
                }else if(tipoUsuario.getSelectedItemPosition() == 0){
                    Toast.makeText(Register.this, "Debe seleccionar el tipo de usuario.", Toast.LENGTH_SHORT).show();
                }else if(estado.getSelectedItemPosition() == 0){
                    Toast.makeText(Register.this, "Debe seleccionar el estado para su cuenta", Toast.LENGTH_SHORT).show();
                }else if(pregunta.getSelectedItemPosition() == 0){
                    Toast.makeText(Register.this, "Debe seleccionar una pregunta de seguridad.", Toast.LENGTH_SHORT).show();
                }else{
                    CRUD.registrarUsuario(Register.this, dtoUsuario);
                }
            }
        });
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre.setText("");
                apellido.setText("");
                usuario.setText("");
                correo.setText("");
                clave.setText("");
                respuesta.setText("");
                tipoUsuario.setSelection(0);
                estado.setSelection(0);
                pregunta.setSelection(0);
            }
        });

    }
}