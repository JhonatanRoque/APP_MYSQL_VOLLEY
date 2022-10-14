package com.fjar.app_mysql.ui.categorias;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fjar.app_mysql.R;
import com.fjar.app_mysql.categoria_CRUD;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriasList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriasList extends Fragment {

    private ListView lvCategorias;
    private categoria_CRUD CRUD = new categoria_CRUD();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoriasList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriasList.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriasList newInstance(String param1, String param2) {
        CategoriasList fragment = new CategoriasList();
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
        View root = inflater.inflate(R.layout.fragment_categorias_list, container, false);
        lvCategorias = (ListView) root.findViewById(R.id.listarCategorias);
        CRUD.obtenerCategoriasLista(root.getContext(), lvCategorias);
        return root;
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //Llamar a las opciones de menu de este fragment
        MenuItem EliminarItem = menu.findItem(R.id.eliminarCategoria);
        MenuItem VerItem = menu.findItem(R.id.listarCategoria);
        EliminarItem.setVisible(true);
        VerItem.setVisible(true);
    }

}