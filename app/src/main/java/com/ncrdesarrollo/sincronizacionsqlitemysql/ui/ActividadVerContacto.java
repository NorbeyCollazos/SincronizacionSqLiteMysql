package com.ncrdesarrollo.sincronizacionsqlitemysql.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ncrdesarrollo.sincronizacionsqlitemysql.R;
import com.ncrdesarrollo.sincronizacionsqlitemysql.provider.Contrato.Contactos;
import com.ncrdesarrollo.sincronizacionsqlitemysql.utilidades.UConsultas;
import com.ncrdesarrollo.sincronizacionsqlitemysql.utilidades.UTiempo;

public class ActividadVerContacto extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Referencias UI
    private TextView campoPrimerNombre;
    private TextView campoPrimerApellido;
    private TextView campoTelefono;
    private TextView campoCorreo;
    String uri;


    // Clave del uri del contacto como extra
    public static final String URI_CONTACTO = "extra.uriContacto";

    private Uri uriContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_ver_contacto);



        // Encontrar Referencias UI
        campoPrimerNombre =  findViewById(R.id.campo_primer_nombre);
        campoPrimerApellido =  findViewById(R.id.campo_primer_apellido);
        campoTelefono =  findViewById(R.id.campo_telefono);
        campoCorreo =  findViewById(R.id.campo_correo);

        // Determinar si es detalle
        uri = getIntent().getStringExtra(URI_CONTACTO);
        uriContacto = Uri.parse(uri);
        getSupportLoaderManager().restartLoader(1, null, this);

    }



    private void poblarViews(Cursor data) {
        if (!data.moveToNext()) {
            return;
        }

        // Asignar valores a UI
        campoPrimerNombre.setText(UConsultas.obtenerString(data, Contactos.PRIMER_NOMBRE));
        campoPrimerApellido.setText(UConsultas.obtenerString(data, Contactos.PRIMER_APELLIDO));
        campoTelefono.setText(UConsultas.obtenerString(data, Contactos.TELEFONO));
        campoCorreo.setText(UConsultas.obtenerString(data, Contactos.CORREO));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uriContacto, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        poblarViews(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void pasar_administrar(View view) {
        Intent intent = new Intent(this, ActividadInsercionContacto.class);
            intent.putExtra(ActividadInsercionContacto.URI_CONTACTO, uri);
        startActivity(intent);
        finish();
    }


}
