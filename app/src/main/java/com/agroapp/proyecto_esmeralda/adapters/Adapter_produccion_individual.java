package com.agroapp.proyecto_esmeralda.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.BaseSQL;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Adapter_produccion_individual extends BaseAdapter {
    private ArrayList<Medida_Leche_Model> list_produccion;
    private Medida_Leche_Model produccion_model;
    private static final String BASE_DATOS = "prueba01sqlite";
    private static final String TABLA_MEDIDDA = "medida";
    private static final int VERSION = 1;

    private static SQLiteDatabase database;
    private final Context context;
    private static BaseSQL dbHelper;
    LayoutInflater inflater;
    String fecha = "",tipo = "";
    TextView tv_item_fecha,tv_item_ind_tipo;
    public Adapter_produccion_individual(ArrayList<Medida_Leche_Model> list_produccion, Context context) {
        this.list_produccion = list_produccion;
        this.context = context;
    }

    public Adapter_produccion_individual(Context _context)
    {
        context = _context;
        dbHelper = new BaseSQL(context, BASE_DATOS, null, VERSION);
    }


    public void openWProd() /*throws SQLException*/
    {
        database = dbHelper.getWritableDatabase();
    }

    public void openRProd() /*throws SQLException*/
    {
        database = dbHelper.getReadableDatabase();
    }

    public void closeProd()
    {
        database.close();
    }

    public int insertmedida(Medida_Leche_Model medida_connexion, String nombre_finca)
    {
        try {

            ContentValues cursorcliente =new ContentValues(); //esta instruccion es la que que permite crear la tabla cursor
                cursorcliente.put("medida_fecha",medida_connexion.getMedida_anml_fecha()); //put trae lo que digite el usuario  al string identificacion
                cursorcliente.put("medida_leche_am",medida_connexion.getMedida_leche_am());
                cursorcliente.put("medida_leche_pm",medida_connexion.getMedida_leche_pm());
                cursorcliente.put("medida_cont_dias",medida_connexion.getMedida_count_dias());
                cursorcliente.put("medida_observaciones",medida_connexion.getMedida_observacoines());
                cursorcliente.put("medida_litros_mes",medida_connexion.getMedida_litros_medidos_mes());
                cursorcliente.put("id_animal",medida_connexion.getMedida_tipo());
                cursorcliente.put("nombre_finca", nombre_finca);
            //pasar los datos de la tabla cursor a la tabla fisica
            database.insert(TABLA_MEDIDDA, null,cursorcliente); //le estamos diciendo que agregue los datos de la tabla 1
            //a la tabla fisica.
            return (int) database.insert(TABLA_MEDIDDA, null, cursorcliente);

        }catch(Exception ex) {
            Toast.makeText(context, "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    public int deleteM(int id, String where)
    {
        String[] args = new String[]{ "" +  id};
        return database.delete(TABLA_MEDIDDA, where, args) ;

    }


    public ArrayList<Medida_Leche_Model> GuardarRegistrosmedida()
    {
        list_produccion = new ArrayList<>();
        Cursor cursor = database.query(TABLA_MEDIDDA, null, null, null, null, null, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(  cursor.getString(  cursor.getColumnIndex( "id"  ) ));
                String id_animal = cursor.getString(  cursor.getColumnIndex("id_animal") );
                String medida_fecha = cursor.getString(  cursor.getColumnIndex( "medida_fecha" ) );
                String medida_n_e = cursor.getString(  cursor.getColumnIndex("medida_n_e" ) );
                int medida_result_am = cursor.getInt(  cursor.getColumnIndex("medida_result_am") );
                int medida_result_pm = cursor.getInt(  cursor.getColumnIndex("medida_result_pm") );
                String medida_observaciones = cursor.getString(  cursor.getColumnIndex(   "medida_observaciones")   );
                String nombre_finca = cursor.getString(  cursor.getColumnIndex(   "nombre_finca") );


                list_produccion.add(produccion_model);
            }while (cursor.moveToNext());
        }
        return list_produccion;
    }

    public int updateM(Medida_Leche_Model medida_leche)
    {
        try {
            //  create object of ContentValues
            ContentValues updatedValues = new ContentValues();
            // Assign values for each Column.

            updatedValues.put("medida_result_pm", medida_leche.getMedida_leche_pm());
            String where="meidida_fecha = ?";

            String idtext= String.valueOf(medida_leche.getMedida_anml_fecha());
            int rtsl = database.update(TABLA_MEDIDDA,updatedValues, where, new String[]{idtext});
            return  rtsl;
            
        }catch (Exception e){
            Toast.makeText(context, "Error " +  e.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }

    }




    @Override
    public int getCount() {
        return list_produccion.size();
    }

    @Override
    public Object getItem(int i) {
        return list_produccion.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemview = view;
        if (view == null) {
             inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemview = inflater.inflate(R.layout.item_produccion_individual, viewGroup, false);
        }
        produccion_model = list_produccion.get(i);
        fecha = produccion_model.getMedida_anml_fecha();
        tipo = produccion_model.getMedida_tipo();
        if (fecha!= null && tipo != null){
            tv_item_fecha.setText(fecha);
            tv_item_ind_tipo.setText(tipo);
        }

        return itemview;
    }
    private int[] parsea_Fecha(String fechaEntera) {
        int day = 0, month = 0, year = 0;
        try {
            //transforma la cadena en un tipo date
            Date miFecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaEntera);

            //creo un calendario
            Calendar calendario = Calendar.getInstance();
            //establezco mi fecha
            calendario.setTime(miFecha);

            //obtener el año
            year = calendario.get(Calendar.YEAR);
            //obtener el mes (0-11 ::: enero es 0 y diciembre es 11)
            month = calendario.get(Calendar.MONTH);
            //obtener el dia del mes (1-31)
            day = calendario.get(Calendar.DAY_OF_MONTH);

            //...mas campos...

        } catch (ParseException ex) {
            //manejar excepcion
        }
        return new int[]{day, month, year};
    }



}
