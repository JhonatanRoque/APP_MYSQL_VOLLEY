package com.fjar.app_mysql.ui.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fjar.app_mysql.MainActivity;
import com.fjar.app_mysql.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SessionSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SessionSettings extends Fragment {
    private UsuarioCRUD CRUD = new UsuarioCRUD();
    private Button btnActualizar, btnCerrarSession, btnEliminarCuenta;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SessionSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SessionSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static SessionSettings newInstance(String param1, String param2) {
        SessionSettings fragment = new SessionSettings();
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
        DtoUsuario usuario = new DtoUsuario();
        SharedPreferences sp = getContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String estado = sp.getString("estado", "");
        if(estado.equals("logON")){
            if(sp.contains("nickName")){
                String usuarioSP = sp.getString("nickName", "");
                usuario.setUsuario(usuarioSP);
                if(usuario.getId() > 0){
                    Toast.makeText(getContext(), "Su usuario es: " + usuario.getUsuario(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_session_settings, container, false);
        btnActualizar = (Button) root.findViewById(R.id.btnActualizar);
        btnCerrarSession = (Button) root.findViewById(R.id.btnCerrarSession);
        btnEliminarCuenta = (Button) root.findViewById(R.id.btnEliminar);

        btnCerrarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getContext().getSharedPreferences("usuario", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Intent inicio = new Intent(getContext(), InitSession.class);
                startActivity(inicio);
            }
        });

        CRUD.obtenerDatosSettings(getContext(), usuario, root);
        return root;
    }
}