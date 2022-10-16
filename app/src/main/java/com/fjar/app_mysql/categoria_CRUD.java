package com.fjar.app_mysql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fjar.app_mysql.ui.categorias.DtoCategoria;
import com.fjar.app_mysql.ui.productos.DtoProductos;

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
        ListView lvProd = new ListView(context);
        obtenerProdCategoria(context, id_categoria, lvProd);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_delete);
        builder.setTitle("Warning");
        builder.setMessage("¿Esta seguro de borrar el registro? \n Código:" +
                id_categoria + "\n Productos asociados: \n" );
        builder.setView(lvProd);
        builder.setCancelable(false);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject requestJSON = new JSONObject(response.toString());
                            String estado = requestJSON.getString("estado");
                            String mensaje = requestJSON.getString("mensaje");
                            if (estado.equals("1")) {
                                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context, "Registro almacenado en MySQL.", Toast.LENGTH_SHORT).show();
                            } else if (estado.equals("2")) {
                                Toast.makeText(context, "Error" + mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "No se pudo guardar. \n" + "Intentelo más tarde.", Toast.LENGTH_SHORT).show();
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
                MySingleton.getInstance(context).addToRequestQueue(request);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();;

    }

    //Método para obtener todas las categorias
    public void obtenerCategoriasLista (final Context context, ListView lv) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<String> categorias = new ArrayList<String>();
        //Objeto para almacenar cada categoria

        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerCategorias.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray peticionJSON = new JSONArray(response.toString());

                    for (int i = 0; i <= peticionJSON.length(); i ++){
                        JSONObject requestJSON = peticionJSON.getJSONObject(i);
                        DtoCategoria cat = new DtoCategoria();
                        String idCategoria = requestJSON.getString("idCategoria");
                        String nombreCategoria = requestJSON.getString("nombreCategoria");
                        String estadoCategoria = requestJSON.getString("estadoCategoria");
                        //Añadimos los datos a nuestro objeto categoria
                        cat.setIdCategoria(Integer.parseInt(idCategoria));
                        cat.setNombreCategoria(nombreCategoria);
                        cat.setEstadoCategoria(Integer.parseInt(estadoCategoria));
                        categorias.add(cat.getIdCategoria() + " - " + cat.getNombreCategoria() + " - " + cat.getEstadoCategoria());

                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, categorias);
                        lv.setAdapter(adaptador);
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

    }

    public void obtenerProdCategoria (final Context context, int idCat, ListView lv) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<String> productos = new ArrayList<String>();
        //Objeto para almacenar cada categoria

        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerProdDeCategoria.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray peticionJSON = new JSONArray(response.toString());
                    productos.add("ID - Producto - Estado");
                    Log.e("Tamaño del array ", String.valueOf(peticionJSON.length()));
                    Toast.makeText(context, "Tamaño de array " + String.valueOf(peticionJSON.length()), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < peticionJSON.length(); i ++){
                        JSONObject requestJSON = peticionJSON.getJSONObject(i);
                        DtoProductos prod = new DtoProductos();
                        String idProducto = requestJSON.getString("idProducto");
                        String nombreProducto = requestJSON.getString("nombreProducto");
                        String Stock = requestJSON.getString("Stock");
                        String precio = requestJSON.getString("precio");
                        String estado = requestJSON.getString("estado");
                        //Añadimos los datos a nuestro objeto producto
                        prod.setIdProducto(Integer.parseInt(idProducto));
                        prod.setNombreProducto(nombreProducto);
                        prod.setStock(Float.parseFloat(Stock));
                        prod.setPrecio(Float.parseFloat(precio));
                        prod.setEstadoProducto(Integer.parseInt(estado));
                        productos.add(prod.getIdProducto() + " - " + prod.getNombreProducto() + " - " + prod.getEstadoProducto());
                        Toast.makeText(context, "Productos encontrados " + productos.get(i), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Productos encontrados " + productos.get(i+1), Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, productos);
                        lv.setAdapter(adaptador);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo obtener los productos. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_categoria", String.valueOf(idCat));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
    //Método para llenar spinenr

    public void obtenerCategoriaSpinner (final Context context, Spinner spinn) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<String> categoria = new ArrayList<>();
        //ArrayAdapter<String> categoriasList;
        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerCategorias.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray requestJSON = new JSONArray(response.toString());
                    Log.e("tamaño de json", String.valueOf(requestJSON.length()));
                    categoria.add("Seleccione una categoria");
                    for (int i = 0; i < requestJSON.length(); i ++){
                        JSONObject requestobjectJSON = requestJSON.getJSONObject(i);
                        String idCategoria = requestobjectJSON.getString("idCategoria");
                        String nombreCategoria = requestobjectJSON.getString("nombreCategoria");

                        categoria.add(idCategoria + " - " + nombreCategoria);
                        Log.e("consola", categoria.get(i).toString() );
                    }
                    Log.e("Tamaño de categoria", "" + categoria.toArray().length);


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoria);
                    spinn.setAdapter(adapter);

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
    }



    public void modificarcategoria(final Context context, DtoCategoria categoria) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/modificarCategoria.php";
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
                Toast.makeText(context, "No se pudo Modificar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_categoria", String.valueOf(categoria.getIdCategoria()));
                map.put("nom_categoria", categoria.getNombreCategoria());
                map.put("estado_categoria", String.valueOf(categoria.getEstadoCategoria()));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
