package com.ncrdesarrollo.sincronizacionsqlitemysql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.ncrdesarrollo.sincronizacionsqlitemysql.ui.ActividadListaContactos;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("preferencesLogin", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion", false);
                if (sesion){
                    Intent intent = new Intent(SplashScreen.this, ActividadListaContactos.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}
