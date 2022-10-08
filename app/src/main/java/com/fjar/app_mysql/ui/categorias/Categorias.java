package com.fjar.app_mysql.ui.categorias;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Categorias extends Fragment {

    private EditText id_categoria, nom_categoria;
    private Spinner spinner_estado;
    private Button btnGuardar;

    private String estado = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Categorias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Categorias.
     */
    // TODO: Rename and change types and number of parameters
    public static Categorias newInstance(String param1, String param2) {
        Categorias fragment = new Categorias();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);
        id_categoria = (EditText) root.findViewById(R.id.et_idcategoria);
        nom_categoria = (EditText) root.findViewById(R.id.et_namecategoria);
        spinner_estado = (Spinner) root.findViewById(R.id.sp_estado);
        btnGuardar = (Button) root.findViewById(R.id.btnSave);

        spinner_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_estado.getSelectedItemPosition() > 0){
                    estado = spinner_estado.getSelectedItem().toString();
                }else {
                    estado = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_categoria.getText().toString();
                String nombre = nom_categoria.getText().toString();
                String estadoCategoria = estado;

                String dato = "";

                if(id_categoria.length() == 0){
                    id_categoria.setError("Campo obligatorio");
                }else if(nom_categoria.length() == 0){
                    nom_categoria.setError("Campo obligatorio");
                }else if(estado.length() == 0){
                    Toast.makeText(getContext(), "Debe seleccionar una opción para el estado", Toast.LENGTH_SHORT).show();
                }else{
                    guardarcategoria(getContext(), Integer.parseInt(id), nombre, Integer.parseInt(estadoCategoria));
                }
            }
        });

        return root;
    }

    private void guardarcategoria(final Context context, final int id_categoria, final String nom_categoria, final int estado_categoria) {
        String url = "https://franciscowebtw.000webhostapp.com/service2020/guardarCategoria.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    Toast.makeText(context, "estado " + estado + mensaje, Toast.LENGTH_SHORT).show();
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
                map.put("nom_categoria", nom_categoria);
                map.put("estado_categoria", String.valueOf(estado_categoria));
                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}