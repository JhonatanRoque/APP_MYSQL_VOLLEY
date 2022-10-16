package com.fjar.app_mysql.ui.productos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fjar.app_mysql.MySingleton;
import com.fjar.app_mysql.R;
import com.fjar.app_mysql.ui.categorias.DtoCategoria;
import com.google.android.material.textfield.TextInputLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Productos extends Fragment {
    private TextInputLayout ti_id, ti_nombre_prod, ti_descripcion, ti_stock,
            ti_precio, ti_unidadmedida;
    private EditText et_id, et_nombre_prod, et_descripcion, et_stock,
            et_precio, et_unidadmedida;
    private Spinner sp_estadoProductos, sp_fk_categoria;
    private TextView tv_fechahora;
    private Button btnSave, btnNew;
    ProgressDialog progressDialog;
    ArrayList<String> lista = null;
    ArrayList<DtoCategoria> listaCategorias;

    //Va a representar la información que se va a mostrar en el combo
//Arreglos para efectuar pruebas de carga de opciones en spinner.
    String elementos[] = {"Uno", "Dos", "Tres", "Cuatro", "Cinco"};
    final String[] elementos1 =new String[]{
            "Seleccione",
            "1",
            "2",
            "3",
            "4",
            "5"
    };
    String idcategoria = "";
    String nombrecategoria = "";
    int conta = 0;
    String datoStatusProduct = "";
    //Instancia DTO
    DtoProductos dto = new DtoProductos();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_productos, container,
                false);
        ti_id = view.findViewById(R.id.ti_id);ti_nombre_prod = view.findViewById(R.id.ti_nombre_prod);
        ti_descripcion = view.findViewById(R.id.ti_descripcion);
        et_id = view.findViewById(R.id.et_id);
        et_nombre_prod = view.findViewById(R.id.et_nombre_prod);
        et_descripcion = view.findViewById(R.id.et_descripcion);
        et_stock = view.findViewById(R.id.et_stock);
        et_precio = view.findViewById(R.id.et_precio);
        et_unidadmedida = view.findViewById(R.id.et_unidadmedida);
        sp_estadoProductos = view.findViewById(R.id.sp_estadoProductos);
        sp_fk_categoria = view.findViewById(R.id.sp_fk_categoria);
        tv_fechahora = view.findViewById(R.id.tv_fechahora);
        tv_fechahora.setText(timedate());
        btnSave = view.findViewById(R.id.btnSave);
        btnNew = view.findViewById(R.id.btnNew);
        sp_estadoProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           if (sp_estadoProductos.getSelectedItemPosition() > 0) {
               datoStatusProduct = sp_estadoProductos.getSelectedItem().toString();
           } else {
               datoStatusProduct = "";
           }
//Toast.makeText(getContext(), ""+datoStatusProduct, Toast.LENGTH_SHORT).show();
       }
       @Override
       public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//Llamo al método para que muestre los datos de la busqueda al carga la actividad.
                        fk_categorias(getContext());
//ArrayAdapter<String> adaptador = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, obtenerListaCategorias());
//ArrayAdapter<String> adaptador =new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, elementos);
//sp_fk_categoria.setAdapter(adaptador);
//Evento del spinner creado para extraer la información seleccionadaen cada item/opción.
                sp_fk_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(conta>=1 && sp_fk_categoria.getSelectedItemPosition()>0){
                            String item_spinner = sp_fk_categoria.getSelectedItem().toString();
//Hago una busqueda en la cadena seleccionada en el spinner para separar el idcategoria y el nombre de la categoria
//Esto es necesario, debido a que lo que debe enviarse a guardar a la base de datos es únicamente el idcategoria.
                            String s[] = item_spinner.split("~");
//Dato ID CATEGORIA
                            idcategoria = s[0].trim();
//Con trim elimino espacios al inicio y final de la cadena para enviar limpio el ID CATEGORIA.
//Dato NOMBRE DE LA CATEGORIA
                            nombrecategoria = s[1];
                            Toast toast = Toast.makeText(getContext(), "Id cat: " + idcategoria + "\n" + "Nombre Categoria: "+nombrecategoria, Toast.LENGTH_SHORT);
                                                                              toast.setGravity(Gravity.CENTER, 0, 0);
                                                                              toast.show();
                        }else{
                            idcategoria = "";
                            nombrecategoria = "";
                        }
                        conta++;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*int id = Integer.parseInt(et_id.getText().toString());
String nombre = et_nombre_prod.getText().toString();
String descripcion = et_descripcion.getText().toString();
double stock =
Double.parseDouble(et_stock.getText().toString());
double precio =
Double.parseDouble(et_precio.getText().toString());
String unidad = et_unidadmedida.getText().toString();*/
                String id = et_id.getText().toString();
                String nombre = et_nombre_prod.getText().toString();
                String descripcion = et_descripcion.getText().toString();
                String stock = et_stock.getText().toString();
                String precio = et_precio.getText().toString();
                String unidad = et_unidadmedida.getText().toString();
                if(id.length() == 0){et_id.setError("Campo obligatorio.");
                }else if(nombre.length() == 0){
                    et_nombre_prod.setError("Campo obligatorio.");
                }else if(descripcion.length() == 0){
                    et_descripcion.setError("Campo obligatorio.");
                }else if(stock.length() == 0){
                    et_stock.setError("Campo obligatorio.");
                }else if(precio.length() == 0){
                    et_precio.setError("Campo obligatorio.");
                }else if(unidad.length() == 0){
                    et_unidadmedida.setError("Campo obligatorio.");
                }else if(sp_estadoProductos.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Debe seleccionar el estado del producto.", Toast.LENGTH_SHORT).show();
                }else if(sp_fk_categoria.getSelectedItemPosition() > 0){
//Toast.makeText(getContext(), "Good...", Toast.LENGTH_SHORT).show();
                    save_productos(getContext(), id, nombre, descripcion, stock, precio, unidad, datoStatusProduct, idcategoria);
                }else{
                    Toast.makeText(getContext(), "Debe seleccionar la categoria.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_product();
            }
        });
        return view;
    }


    private String timedate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String fecha = sdf.format(cal.getTime());
        return fecha;
    }
    //public ArrayList<dto_categorias> fk_categorias(final Context context){
    public void fk_categorias(final Context context){
        listaCategorias = new ArrayList<DtoCategoria>();
        lista = new ArrayList<String>();
        lista.add("Seleccione Categoria");
        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerCategorias.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    int totalEncontrados = array.length();
//Toast.makeText(context, "Total:"+totalEncontrados, Toast.LENGTH_SHORT).show();
                    DtoCategoria obj_categorias = null;
//dto_categorias obj_categorias = new dto_categorias();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject categoriasObject = array.getJSONObject(i);
                        int id_categoria =
                                categoriasObject.getInt("idCategoria");
                        String nombre_categoria =
                                categoriasObject.getString("nombreCategoria");
                        int estado_categoria =
                                Integer.parseInt(categoriasObject.getString("estadoCategoria"));
                        obj_categorias = new DtoCategoria(id_categoria, nombre_categoria, estado_categoria);

                        listaCategorias.add(obj_categorias);
                                lista.add(listaCategorias.get(i).getIdCategoria()+" ~ "+listaCategorias.get(i).getNombreCategoria());
                                ArrayAdapter<String> adaptador =new ArrayAdapter<String> (getContext(),android.R.layout.simple_spinner_item,
                                lista);
//Cargo los datos en el Spinner
                        sp_fk_categoria.setAdapter(adaptador);
                        //Muestro datos en LogCat para verificar larespuesta obtenida desde el servidor.
                                Log.i("Id Categoria",
                                        String.valueOf(obj_categorias.getIdCategoria()));
                        Log.i("Nombre Categoria",
                                obj_categorias.getNombreCategoria());
                        Log.i("Estado Categoria",
                                String.valueOf(obj_categorias.getEstadoCategoria()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Compruebe su acceso a Internet.", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }



    private void save_productos(final Context context, final String id_prod,
                                final String nom_prod, final String des_prod,
                                final String stock, final String precio, final
                                String uni_medida, final String estado_prod,
                                final String categoria){
        String url = "https://franciscowebtw.000webhostapp.com/service2020/guardarProductos.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Registro almacenado en MySQL.", Toast.LENGTH_SHORT).show();
                    }else if(estado.equals("2")){
                        Toast.makeText(context, ""+mensaje,
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se puedo guardar. \n" +
                        "Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError
            {
//En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_prod", id_prod);
                map.put("nom_prod", nom_prod);
                map.put("des_prod", des_prod);
                map.put("stock", stock);
                map.put("precio", precio);
                map.put("uni_medida", uni_medida);
                map.put("estado_prod", estado_prod);
                map.put("categoria", categoria);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void new_product() {
        et_id.setText(null);
        et_nombre_prod.setText(null);
        et_descripcion.setText(null);
        et_stock.setText(null);
        et_precio.setText(null);
        et_unidadmedida.setText(null);
        sp_estadoProductos.setSelection(0);
        sp_fk_categoria.setSelection(0);
    }//No utilizo este método en nada por el momento
    public ArrayList<String> obtenerListaCategorias() {
//ArrayList<String> lista = new ArrayList<String>();
        lista = new ArrayList<String>();
        lista.add("Seleccione Categoria");
        for(int i=0;i<=listaCategorias.size();i++){
            lista.add(listaCategorias.get(i).getIdCategoria()+" ~ "+listaCategorias.get(i).getNombreCategoria());
        }
        return lista;
    }
        //Método para eliminar productos
        public void eliminarProducto(final Context context, final int id_cod) {
            String url = "https://franciscowebtw.000webhostapp.com/service2020/eliminarProductos.php";
            //ListView lvProd = new ListView(context);
            //obtenerProdCategoria(context, id_categoria, lvProd);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_delete);
            builder.setTitle("Warning");
            builder.setMessage("¿Esta seguro de borrar el producto? \n Código:" +
                    id_cod );
            //builder.setView(lvProd);
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
                            Toast.makeText(context, "No se pudo Eliminar. \n" + "Intentelo más tarde.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //En este método se colocan o se setean los valores a recibir por el fichero *.php
                            Map<String, String> map = new HashMap<>();
                            map.put("Content-Type", "application/json; charset=utf-8");
                            map.put("Accept", "application/json");
                            map.put("codigo", String.valueOf(id_cod));
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
    public void obtenerProductoSpinner (final Context context, Spinner spinn) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<String> producto = new ArrayList<>();
        //ArrayAdapter<String> categoriasList;
        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerProductos.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray requestJSON = new JSONArray(response.toString());
                    Log.e("tamaño de json", String.valueOf(requestJSON.length()));
                    producto.add("Seleccione un producto");
                    for (int i = 0; i < requestJSON.length(); i ++){
                        JSONObject requestobjectJSON = requestJSON.getJSONObject(i);
                        String idCategoria = requestobjectJSON.getString("id");
                        String nombreCategoria = requestobjectJSON.getString("nombreProducto");

                        producto.add(idCategoria + " - " + nombreCategoria);
                        Log.e("consola", producto.get(i).toString() );
                    }
                    Log.e("Tamaño de categoria", "" + producto.toArray().length);


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, producto);
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
    //Método para obtener todos los productos
    public void obtenerProductosLista (final Context context, ListView lv) {
        //Creamos un array en el cual guardaremos cada una de los datos que vendran de nuestra API
        ArrayList<String> Producto = new ArrayList<String>();

        String url = "https://franciscowebtw.000webhostapp.com/service2020/obtenerProductos.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray peticionJSON = new JSONArray(response.toString());

                    for (int i = 0; i <= peticionJSON.length(); i ++){
                        JSONObject requestJSON = peticionJSON.getJSONObject(i);
                        DtoProductos prod = new DtoProductos();
                        String id = requestJSON.getString("id");
                        String nombreProd = requestJSON.getString("nombreProducto");
                        String DescProd = requestJSON.getString("descripcion");
                        String stock = requestJSON.getString("stock");
                        String estado = requestJSON.getString("estado");
                        //Añadimos los datos a nuestro objeto categoria
                        prod.setIdProducto(Integer.parseInt(id));
                        prod.setNombreProducto(nombreProd);
                        prod.setDescProducto(DescProd);
                        prod.setStock(Float.parseFloat(stock));
                        prod.setEstadoProducto(Integer.parseInt(estado));
                        Producto.add(prod.getIdProducto() + " - " + prod.getNombreProducto() + " - " + prod.getDescProducto() + " - " + prod.getStock() + " - " + prod.getEstadoProducto());

                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Producto);
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

}