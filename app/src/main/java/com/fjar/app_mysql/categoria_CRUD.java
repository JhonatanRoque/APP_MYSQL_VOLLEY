package com.fjar.app_mysql;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fjar.app_mysql.ui.categorias.DtoCategoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class categoria_CRUD {

    //Constructor vacio
    public categoria_CRUD(){

    }
    //Registrar categoria
    public void guardarcategoria(final Context context, final int id_categoria, final String nom_categoria, final int estado_categoria) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/guardarCategoria.php";
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
                    }else if(estado.equals("2")){
                        Toast.makeText(context, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo guardar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_categoria", String.valueOf(id_categoria));
                map.put("nom_categoria", nom_categoria);
                map.put("estado_categoria", String.valueOf(estado_categoria));
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void eliminarcategoria(final Context context, final int id_categoria) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/eliminarCategoria.php";
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
                    }else if(estado.equals("2")){
                        Toast.makeText(context, "Error"+mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo guardar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_categoria", String.valueOf(id_categoria));
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //Método para obtener todas las categorias
    public ArrayList<DtoCategoria> obtenerCategorias (final Context context) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<DtoCategoria> categorias = new ArrayList<DtoCategoria>();
        //Objeto para almacenar cada categoria

        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerCategorias.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());

                    for (int i = 0; i <= requestJSON.length(); i ++){
                        DtoCategoria cat = new DtoCategoria();
                        String idCategoria = requestJSON.getString("idCategoria");
                        String nombreCategoria = requestJSON.getString("nombreCategoria");
                        String estadoCategoria = requestJSON.getString("estadoCategoria");
                        //Añadimos los datos a nuestro objeto categoria
                        cat.setIdCategoria(Integer.parseInt(idCategoria));
                        cat.setNombreCategoria(nombreCategoria);
                        cat.setEstadoCategoria(Integer.parseInt(estadoCategoria));
                        categorias.add(cat);
                    }

                    obtenerCategoriaSpinner(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo obtener las categorias \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);


        return categorias;
    }
    //Método para llenar spinenr
    public ArrayList<String> obtenerCategoriaSpinner (final Context context) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<String> categorias = new ArrayList<>();
        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerCategorias.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray requestJSON = new JSONArray(response.toString());
                    Log.e("tamaño de json", String.valueOf(requestJSON.length()));
                    categorias.add("Seleccione una categoria");
                    for (int i = 0; i < requestJSON.length(); i ++){
                        JSONObject requestobjectJSON = requestJSON.getJSONObject(i);
                        String idCategoria = requestobjectJSON.getString("idCategoria");
                        String nombreCategoria = requestobjectJSON.getString("nombreCategoria");
                        Log.e("consola", idCategoria + " -- " + nombreCategoria);
                        categorias.add(idCategoria + " -> " + nombreCategoria);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo obtener las categorias \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);


        return categorias;
    }
}
