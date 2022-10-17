package com.fjar.app_mysql.ui.productos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import androidx.fragment.app.Fragment;

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


public class ModificarProductos extends Fragment {
    private TextInputLayout ti_id, ti_nombre_prod, ti_descripcion, ti_stock,
            ti_precio, ti_unidadmedida;
    private Productos CRUD = new Productos();
    private EditText et_id, et_nombre_prod, et_descripcion, et_stock,
            et_precio, et_unidadmedida;
    private Spinner sp_estadoProductos, sp_fk_categoria, spnIDPRod;
    private TextView tv_fechahora;
    private Button btnModificar, btnNew;
    ProgressDialog progressDialog;
    ArrayList<String> lista = null;
    ArrayList<DtoCategoria> listaCategorias;
    String id;

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
        View view = inflater.inflate(R.layout.fragment_modificar_productos, container,
                false);

        ti_nombre_prod = view.findViewById(R.id.ti_nombre_prod);
        ti_descripcion = view.findViewById(R.id.ti_descripcion);
        et_nombre_prod = view.findViewById(R.id.et_nombre_prod);
        et_descripcion = view.findViewById(R.id.et_descripcion);
        et_stock = view.findViewById(R.id.et_stock);
        et_precio = view.findViewById(R.id.et_precio);
        et_unidadmedida = view.findViewById(R.id.et_unidadmedida);
        sp_estadoProductos = view.findViewById(R.id.sp_estadoProductos);
        sp_fk_categoria = view.findViewById(R.id.sp_fk_categoria);
        tv_fechahora = view.findViewById(R.id.tv_fechahora);
        tv_fechahora.setText(timedate());
        btnModificar = view.findViewById(R.id.btnSave);
        btnNew = view.findViewById(R.id.btnNew);
        spnIDPRod = (Spinner) view.findViewById(R.id.spnIdProducto);
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

        //Rellenar el spinner
        CRUD.fk_categorias(view.getContext(), sp_fk_categoria);
        CRUD.obtenerProductoSpinner(view.getContext(), spnIDPRod);

        spnIDPRod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int position, long l) {
                String Item = spnIDPRod.getSelectedItem().toString();

                if(position > 0){
                    String s[] = Item.split("-");
                    id = s[0].trim();
                    Toast.makeText(getContext(), "ID de producto" + id, Toast.LENGTH_SHORT).show();
                    CRUD.obtenerProductoIndividual(getContext(), Integer.parseInt(id), view);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_fk_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = sp_fk_categoria.getSelectedItem().toString();
                if(position > 0){
                    String s[] = item.split("-");
                    idcategoria = s[0].trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Boton eliminar
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = et_nombre_prod.getText().toString();
                String descripcion = et_descripcion.getText().toString();
                String stock = et_stock.getText().toString();
                String precio = et_precio.getText().toString();
                String unidad = et_unidadmedida.getText().toString();
                if(spnIDPRod.getSelectedItemPosition() > 0)
                {
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
                        CRUD.update_productos(getContext(), id, nombre, descripcion, stock, precio, unidad, datoStatusProduct, idcategoria);

                    }else{
                        Toast.makeText(getContext(), "Debe seleccionar la categoria.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Debe escoger un producto para continuar", Toast.LENGTH_SHORT).show();
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

    private void new_product() {
        spnIDPRod.setSelection(0);
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


}