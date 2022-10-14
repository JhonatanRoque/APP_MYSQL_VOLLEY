package com.fjar.app_mysql.ui.categorias;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.fjar.app_mysql.categoria_CRUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class eliminarcategoria {
    private Dialog myDialog;
    private AlertDialog.Builder dialogo;
    private boolean validaInput = false;
    private String codigo;
    private Spinner spn;



    private SQLiteDatabase db = null;

    public void eliminar(final Context context){
        categoria_CRUD CRUD = new categoria_CRUD();
        DtoCategoria cat = new DtoCategoria();
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.ventana_eliminar_categoria);
        myDialog.setTitle("Eliminar");
        myDialog.setCancelable(false);
        Context cont = myDialog.getContext();
        final EditText et_cod = (EditText) myDialog.findViewById(R.id.et_cod);
        Button btn_eliminar = (Button) myDialog.findViewById(R.id.btn_eliminar);
        TextView tv_Close = (TextView) myDialog.findViewById(R.id.tv_close);
        tv_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        //Spinner
        spn = (Spinner) myDialog.findViewById(R.id.spinnerCategoriasDelete);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, obtenerCategoriaSpinner(myDialog.getContext()));
        //spn.setAdapter(adapter);
        CRUD.obtenerCategoriaSpinner(myDialog.getContext(), spn);
        //Toast.makeText(cont, "mensaje" + obtenerCategoriaSpinner(myDialog.getContext()), Toast.LENGTH_SHORT).show();

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) spn.getSelectedItem().toString();

                    if(position != 0) {
                        String s[] = item.split("-");
                        cat.setIdCategoria(Integer.parseInt(s[0].trim()));
                        //Toast.makeText(view.getContext(), "El item es" + cat.getIdCategoria(), Toast.LENGTH_SHORT).show();
                        Log.e("Mensaje Id categoria", "" + cat.getIdCategoria());

                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "El item del spinner es: " + spn.getSelectedItem(), Toast.LENGTH_SHORT).show();


                if((cat.getIdCategoria() != 0) || (et_cod.getText().toString().length() != 0)){
                    if((et_cod.getText().toString().length() > 0) && (cat.getIdCategoria() > 0)){
                        Toast.makeText(cont, "¡Utilice solo uno de los métodos para eliminar!", Toast.LENGTH_SHORT).show();
                        validaInput = false;
                    }else {
                        validaInput = true;
                    }
                }else {
                    Toast.makeText(cont, "Eliga una categoria o ingrese el codigo", Toast.LENGTH_SHORT).show();
                }

                if(validaInput){
                    CRUD.eliminarcategoria(v.getContext(), cat.getIdCategoria());
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
