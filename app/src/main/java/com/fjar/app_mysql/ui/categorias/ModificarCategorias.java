package com.fjar.app_mysql.ui.categorias;

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

import com.fjar.app_mysql.R;
import com.fjar.app_mysql.categoria_CRUD;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificarCategorias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarCategorias extends Fragment {
    private Spinner spnEstado, spnCategoria;
    private Button btnModificar;
    private EditText edtNombre;
    private categoria_CRUD CRUD = new categoria_CRUD();
    private DtoCategoria categoria = new DtoCategoria();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModificarCategorias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModificarCategorias.
     */
    // TODO: Rename and change types and number of parameters
    public static ModificarCategorias newInstance(String param1, String param2) {
        ModificarCategorias fragment = new ModificarCategorias();
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
        View root = inflater.inflate(R.layout.fragment_modificar_categorias, container, false);
        spnCategoria = (Spinner) root.findViewById(R.id.spnCategoria);
        spnEstado = (Spinner) root.findViewById(R.id.spn_estado);
        edtNombre = (EditText) root.findViewById(R.id.edt_nombreCategoria);
        btnModificar = (Button) root.findViewById(R.id.btnUpdate);

        CRUD.obtenerCategoriaSpinner(root.getContext(), spnCategoria);
        spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) spnCategoria.getSelectedItem().toString();

                if(position != 0) {
                    String s[] = item.split("-");
                    categoria.setIdCategoria(Integer.parseInt(s[0].trim()));
                    categoria.setNombreCategoria(s[1]);

                    edtNombre.setText(categoria.getNombreCategoria());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position != 0) {
                    categoria.setEstadoCategoria(Integer.parseInt(spnEstado.getSelectedItem().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    categoria.setEstadoCategoria(1);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtNombre.length() == 0){
                    edtNombre.setError("Eliga una categoria para continuar");
                }else {
                    categoria.setNombreCategoria(edtNombre.getText().toString());
                    CRUD.modificarcategoria(root.getContext(), categoria);
                }
            }
        });

        return root;
    }
}