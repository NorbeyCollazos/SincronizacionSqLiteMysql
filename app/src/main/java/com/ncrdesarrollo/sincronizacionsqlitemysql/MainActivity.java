package com.ncrdesarrollo.sincronizacionsqlitemysql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ncrdesarrollo.sincronizacionsqlitemysql.ui.ActividadListaContactos;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edtUsuario, edtPassword;
    Button btnLogin;
    String usuario, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        recuperarPreferencias();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario=edtUsuario.getText().toString();
                password=edtPassword.getText().toString();
                if (!usuario.isEmpty()&&!password.isEmpty()){
                    validarUsuario("http://192.168.0.103/api.peopleapp.com/v1/validar_usuario.php");
                }else{
                    Toast.makeText(MainActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    guardarPreferencias();
                    Intent intent = new Intent(MainActivity.this, ActividadListaContactos.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Usruario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parametros= new HashMap<String, String>();
                parametros.put("correo",usuario);
                parametros.put("contrasena",password);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferencesLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario",usuario);
        editor.putString("password",password);
        editor.putBoolean("sesion",true);
        editor.commit();
    }

    private void recuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferencesLogin", Context.MODE_PRIVATE);
        edtUsuario.setText(preferences.getString("usuario",""));
        edtPassword.setText(preferences.getString("password",""));
    }

}
