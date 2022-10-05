package com.fjar.app_mysql;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class test_volley {
    public  void baseRequest (final Context context){
        String url = "https://servicestechnology.com.sv/ws/json1.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", "1");
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void recibirJSON (final Context context){
        String url = "https://franciscowebtw.000webhostapp.com/service2020/json1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject respuestaJSON = new JSONObject(response.toString());
                    String dato1 = respuestaJSON.getString("id");
                    String dato2 = respuestaJSON.getString("nombre");

                    Toast.makeText(context, "Datos recibidos \n" + "Nombre: " + dato2 + "\n" + "ID: " + dato1, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Respuesta: " + response.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR.Verifique su conexión", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
