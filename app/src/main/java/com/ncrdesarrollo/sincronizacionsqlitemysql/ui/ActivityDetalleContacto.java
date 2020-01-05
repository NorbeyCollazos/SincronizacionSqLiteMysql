package com.ncrdesarrollo.sincronizacionsqlitemysql.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.ncrdesarrollo.sincronizacionsqlitemysql.R;
import com.ncrdesarrollo.sincronizacionsqlitemysql.provider.Contrato;
import com.ncrdesarrollo.sincronizacionsqlitemysql.utilidades.UConsultas;

import java.util.ArrayList;

public class ActivityDetalleContacto extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // Referencias UI
    private TextView campoPrimerNombre;
    private TextView campoPrimerApellido;
    private TextView campoTelefono;
    private TextView campoCorreo;
    String uri;

    CollapsingToolbarLayout collapsingToolbarLayout;



    //para el grafico
    /*private BarChart grafico_barras;


    private String[] meses = new String[]{"Enero","Febrero","Marzo"};
    private int[] datos = new int[]{15,27,33};
    private int[] colores = new int[]{Color.BLACK,Color.BLUE,Color.GREEN};*/


    // Clave del uri del contacto como extra
    public static final String URI_CONTACTO = "extra.uriContacto";

    private Uri uriContactov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Encontrar Referencias UI
        campoPrimerNombre =  findViewById(R.id.campo_primer_nombre);
        campoPrimerApellido =  findViewById(R.id.campo_primer_apellido);
        campoTelefono =  findViewById(R.id.campo_telefono);
        campoCorreo =  findViewById(R.id.campo_correo);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);



        // Determinar si es detalle
        uri = getIntent().getStringExtra(URI_CONTACTO);
        uriContactov = Uri.parse(uri);
        getSupportLoaderManager().restartLoader(1, null, this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(ActivityDetalleContacto.this, ActividadInsercionContacto.class);
                intent.putExtra(ActividadInsercionContacto.URI_CONTACTO, uri);
                startActivity(intent);
                finish();
            }
        });



        //para los grafocs
        /*grafico_barras = findViewById(R.id.grafico_barras);
        createCharts();*/


    }



    private void poblarViews(Cursor data) {
        if (!data.moveToNext()) {
            return;
        }

        // Asignar valores a UI
        campoPrimerNombre.setText(UConsultas.obtenerString(data, Contrato.Contactos.PRIMER_NOMBRE));
        campoPrimerApellido.setText(UConsultas.obtenerString(data, Contrato.Contactos.PRIMER_APELLIDO));
        campoTelefono.setText(UConsultas.obtenerString(data, Contrato.Contactos.TELEFONO));
        campoCorreo.setText(UConsultas.obtenerString(data, Contrato.Contactos.CORREO));

        collapsingToolbarLayout.setTitle(UConsultas.obtenerString(data, Contrato.Contactos.PRIMER_NOMBRE));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uriContactov, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        poblarViews(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }








    //clases para los graficos
    /*private Chart getSameChart(Chart chart,String description,int textcolor,int bakground,int animateY){
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textcolor);
        chart.setBackgroundColor(bakground);
        chart.animateY(animateY);
        legend(chart);
        return chart;
    }

    //para personalizar la leyenda
    private void legend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry>entries=new ArrayList<>();
        for (int i =0;i<meses.length;i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor=colores[i];
            entry.label=meses[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry>getBarEntries(){
        ArrayList<BarEntry>entries=new ArrayList<>();
        for (int i =0;i<datos.length;i++)
            entries.add(new BarEntry(i,datos[i]));
        return entries;
    }
    private ArrayList<PieEntry>getPieEntries(){
        ArrayList<PieEntry>entries=new ArrayList<>();
        for (int i =0;i<datos.length;i++)
            entries.add(new PieEntry(i,datos[i]));
        return entries;
    }

    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(meses));
        axis.setEnabled(true);//para desactivar ls titulos de las barras

    }
    private void axisLeft(YAxis axis){
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }
    public void createCharts(){
        grafico_barras= (BarChart) getSameChart(grafico_barras,"Descripción",Color.BLACK,Color.WHITE,2000);
        grafico_barras.setDrawGridBackground(true);
        grafico_barras.setDrawBarShadow(true);
        grafico_barras.setData(getBarData());
        grafico_barras.invalidate();
        grafico_barras.getLegend().setEnabled(false);//para mostrar los titulos sin leyenda

        axisX(grafico_barras.getXAxis());
        axisLeft(grafico_barras.getAxisLeft());
        axisRight(grafico_barras.getAxisRight());

    }
    private DataSet getData(DataSet dataSet){
        dataSet.setColors(colores);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }
    private BarData getBarData(){
        BarDataSet barDataSet=(BarDataSet)getData(new BarDataSet(getBarEntries(),""));

        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }
    private PieData getPieData(){
        PieDataSet pieDataSet=(PieDataSet)getData(new PieDataSet(getPieEntries(),""));

        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueFormatter(new PercentFormatter());
        return new PieData(pieDataSet);
    }*/


}
