package com.fjar.app_mysql.ui.session;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fjar.app_mysql.MainActivity;
import com.fjar.app_mysql.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UsuarioCRUD  extends AppCompatActivity{
    //Método para registrar usuarios
    public void registrarUsuario(final Context context, DtoUsuario usuario) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/registrarUsuario.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Registro almacenado en MySQL.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo registrar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("nombres", usuario.getNombre());
                map.put("apellidos", usuario.getApellido());
                map.put("correo", usuario.getCorreo());
                map.put("usuario", usuario.getUsuario());
                map.put("contrasena", usuario.getClave());
                map.put("tipo", String.valueOf(usuario.getTipo()));
                map.put("estado", String.valueOf(usuario.getEstado()));
                map.put("pregunta", usuario.getPregunta());
                map.put("respuesta", usuario.getRespuesta());
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //Método par inicar sesion
    public void IniciarSesionUsu(final Context context, DtoUsuario usuario, Switch mantener) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/iniciarSesion.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    if(requestJSON.has("mensaje") == false){
                        String id = requestJSON.getString("id");
                        String usuario = requestJSON.getString("usuario");

                        if(id.length() > 0){
                            Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                            SharedPreferences spUsuario = context.getSharedPreferences("usuario", context.MODE_PRIVATE);
                            String estado = "logON";
                            SharedPreferences.Editor editor = spUsuario.edit();
                            editor.putString("estado", estado);
                            //Si se establecio la opcion de mantener iniciada sesion
                            if(mantener.isChecked()){

                                editor.putString("id", id);
                                editor.putString("nickName", usuario);

                            }
                            editor.commit();

                        }else {
                            Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        String mensaje = requestJSON.getString("mensaje");
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo iniciar session. \n" +"Intentelo más tarde." + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                if(usuario.getUsuario() != null){
                    map.put("Content-Type", "application/json; charset=utf-8");
                    map.put("Accept", "application/json");
                    map.put("usuario", usuario.getUsuario());
                    map.put("correo", "");
                    map.put("contrasena", usuario.getClave());
                }else if(usuario.getCorreo() != ""){
                    map.put("Content-Type", "application/json; charset=utf-8");
                    map.put("Accept", "application/json");
                    map.put("usuario", "");
                    map.put("correo", usuario.getCorreo());
                    map.put("contrasena", usuario.getClave());
                }

                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //Método par obtener pregunta de seguridad
    public void getPregunta(final Context context, DtoUsuario usuario, EditText preguntaEspacio) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerPregunta.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String pregunta = requestJSON.getString("pregunta");
                    preguntaEspacio.setText(pregunta);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo encontrar una pregunta. \n" +"Intentelo más tarde." + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("correo", usuario.getCorreo());
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //Método par verificar respuesta y recuperar usuario
    public void getUsuario(final Context context, DtoUsuario usuario, TextView usuarioEspacio) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/verificarRespuesta.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String usuario = requestJSON.getString("usuario");
                    usuarioEspacio.setText(usuario);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo encontrar el usuario. \n" +"Intentelo más tarde." + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("correo", usuario.getCorreo());
                map.put("pregunta", usuario.getPregunta());
                map.put("respuesta", usuario.getRespuesta());
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //Método par verificar respuesta y recuperar usuario
    public void getContrasena(final Context context, DtoUsuario usuario, TextView claveEspacio) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/recuperarContrasena.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String contrasena = requestJSON.getString("contrasena");
                    claveEspacio.setText(contrasena);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo encontrar la contrasena de la cuenta. \n" +"Intentelo más tarde." + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("correo", usuario.getCorreo());
                map.put("pregunta", usuario.getPregunta());
                map.put("respuesta", usuario.getRespuesta());
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //Método para obtener datos indivudual de configuración de cuenta
    public void obtenerDatosSettings(final Context context, DtoUsuario usuario, View vista) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/recuperarContrasena.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String nombre = requestJSON.getString("nombre");
                    String apellido = requestJSON.getString("apellido");
                    String correo = requestJSON.getString("correo");
                    String usuario = requestJSON.getString("usuario");
                    String contrasena = requestJSON.getString("clave");
                    String estado = requestJSON.getString("estado");
                    String pregunta = requestJSON.getString("pregunta");
                    String respuesta = requestJSON.getString("respuesta");
                    String fecha = requestJSON.getString("fecha_registro");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo encontrar la contrasena de la cuenta. \n" +"Intentelo más tarde." + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("usuario", usuario.getUsuario());
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
