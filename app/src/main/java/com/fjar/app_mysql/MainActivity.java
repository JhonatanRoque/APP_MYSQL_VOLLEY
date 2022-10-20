package com.fjar.app_mysql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.fjar.app_mysql.ui.categorias.CategoriasList;
import com.fjar.app_mysql.ui.categorias.eliminarcategoria;
import com.fjar.app_mysql.ui.productos.eliminarProducto;
import com.fjar.app_mysql.ui.session.DtoUsuario;
import com.fjar.app_mysql.ui.session.InitSession;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.fjar.app_mysql.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private DtoUsuario usuario;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_delete)
                    .setTitle("Warning")
                    .setMessage("¡Esta a punto de abondanar la APP!")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            finishAffinity();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = new DtoUsuario();
        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String estado = sp.getString("estado", "");

        if(estado.equals("logON")){
            if(sp.contains("id")){
                String id = sp.getString("id", "");
                String nick = sp.getString("nickName", "");
                usuario.setId(Integer.parseInt(id));
                if(usuario.getId() > 0){
                    Toast.makeText(MainActivity.this, "Su id este: " + usuario.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "su usuario es: " + nick, Toast.LENGTH_SHORT).show();
                }
            }
            if(sp.contains("tipo")){
                String tipo = sp.getString("tipo", "");
                usuario.setTipo(Integer.parseInt(tipo));
                Toast.makeText(this, "Su tipo de usuario es: " + usuario.getTipo(), Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(this, "Su estado es: " + estado, Toast.LENGTH_SHORT).show();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(usuario.getTipo() > 1){
            navigationView.getMenu().findItem(R.id.nav_categoria).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_ModCategoria).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_producto).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_ModProducto).setVisible(false);
        }

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Action();
                            }
                        }).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categoria, R.id.nav_producto, R.id.nav_ListCategoria, R.id.nav_ModCategoria, R.id.nav_productoList, R.id.nav_ModProducto, R.id.nav_User, R.id.nav_Cerrar_session, R.id.nav_UserList)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.eliminarCategoria){
            eliminarcategoria eliminar = new eliminarcategoria();
            eliminar.eliminar(this);
        }else  if(id == R.id.eliminarProducto){
            eliminarProducto eliminar = new eliminarProducto();
            eliminar.eliminar(this);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void Action (){
        Toast.makeText(this, "Al presionar el boton", Toast.LENGTH_SHORT).show();
    }


}