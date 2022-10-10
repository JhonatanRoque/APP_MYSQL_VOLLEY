package com.fjar.app_mysql.ui.categorias;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.fjar.app_mysql.R;
import com.fjar.app_mysql.categoria_CRUD;

public class eliminarcategoria {
    private Dialog myDialog;
    private AlertDialog.Builder dialogo;
    private boolean validaInput = false;
    private String codigo;


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
        Spinner spn = (Spinner) myDialog.findViewById(R.id.spinnerCategoriasDelete);
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(cont, android.R.layout.simple_spinner_item, CRUD.obtenerCategoriaSpinner(cont));
        spn.setAdapter(adaptador);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adaptador.getItem(position);
                Toast.makeText(view.getContext(), "El item es" + item, Toast.LENGTH_SHORT).show();
                if(position != 0 ){
                   //cat.setIdCategoria(CRUD.obtenerCategorias(cont).get(position -1).getIdCategoria());
                   cat.setIdCategoria(Integer.parseInt(spn.getSelectedItem().toString()));
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
                Toast.makeText(v.getContext(), "El item del spinner es: " + spn.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
                String cod = "";
                DtoCategoria cat = new DtoCategoria();
                if((et_cod.getText().toString().length() != 0) ){
                    cod = et_cod.getText().toString();
                    cat.setIdCategoria(Integer.parseInt(cod));
                    validaInput = true;

                }else if(cat.getIdCategoria() != 0) {
                    validaInput = true;
                }else {
                    et_cod.setError("Campo obligatorio");
                    Toast.makeText(cont, "Eliga una categoria o ingrese el codigo", Toast.LENGTH_SHORT).show();
                }

                if(validaInput){
                    CRUD.eliminarcategoria(v.getContext(), cat.getIdCategoria());
                }else{
                    Toast.makeText(context, "No ha especificado la categoria a eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
