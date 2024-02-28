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
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Produccion_Register_View_p_animal_dialog;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Adapter_produccion_individual_carne extends BaseAdapter {
    private ArrayList<Pesaje_Animal_Model> list_produccion;
    private Pesaje_Animal_Model produccion_model;
    String fecha = "",tipo ;
    TextView tv_item_fecha;
    private final Context context;
    LayoutInflater inflater;
    private static final String BASE_DATOS = "prueba01sqlite";
    private static final String TABLA_PESAJE = "pesaje";
    private static final int VERSION = 1;

    private static SQLiteDatabase database;
    private static BaseSQL dbHelper;


    public Adapter_produccion_individual_carne(ArrayList<Pesaje_Animal_Model> list_produccion, Context context) {
        this.list_produccion = list_produccion;
        this.context = context;
    }

    public Adapter_produccion_individual_carne(Context _context)
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

    public int insertpesaje(Pesaje_Animal_Model pesaje_connexion, String nombre_finca)
    {
        try {

            ContentValues cursorcliente =new ContentValues(); //esta instruccion es la que que permite crear la tabla cursor
            cursorcliente.put("pesaje_fecha",pesaje_connexion.getPesaje_fecha_ingreso()); //put trae lo que digite el usuario  al string identificacion
            cursorcliente.put("pesaje_result",pesaje_connexion.getPesaje_result());
            cursorcliente.put("pesaje_observaciones",pesaje_connexion.getPesaje_observacoines());
            cursorcliente.put("id_animal",pesaje_connexion.getPesaje_tipo());
            cursorcliente.put("nombre_finca", nombre_finca);
            //pasar los datos de la tabla cursor a la tabla fisica
            database.insert(TABLA_PESAJE, null,cursorcliente); //le estamos diciendo que agregue los datos de la tabla 1
            //a la tabla fisica.

            return (int) database.insert(TABLA_PESAJE, null, cursorcliente);

        }catch(Exception ex) {
            Toast.makeText(context, "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    public int deleteM(int id, String where)
    {
        String[] args = new String[]{ "" +  id};
        return database.delete(TABLA_PESAJE, where, args) ;

    }


    public ArrayList<Pesaje_Animal_Model> GuardarRegistrospesaje()
    {
        list_produccion = new ArrayList<>();
        Cursor cursor = database.query(TABLA_PESAJE, null, null, null, null, null, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(  cursor.getString(  cursor.getColumnIndex( "id"  ) ));
                String id_animal = cursor.getString(  cursor.getColumnIndex(   "id_animal") );
                String pesaje_fecha = cursor.getString(  cursor.getColumnIndex( "pesaje_fecha" ) );
                String pesaje_n_e = cursor.getString(  cursor.getColumnIndex( "pesaje_n_e" ) );
                int pesaje_result = cursor.getInt(  cursor.getColumnIndex(   "pesaje_result_am") );
                int pesaje_result_pm = cursor.getInt(  cursor.getColumnIndex(   "pesaje_result_pm") );
                String pesaje_observaciones = cursor.getString(  cursor.getColumnIndex(   "pesaje_observaciones")   );
                String nombre_finca = cursor.getString(  cursor.getColumnIndex(   "nombre_finca") );

                Produccion_Register_View_p_animal_dialog registro_datos_pesaje_dialog = new Produccion_Register_View_p_animal_dialog();
                produccion_model = new Pesaje_Animal_Model();
                produccion_model.setPesaje_fecha_ingreso(fecha);
                produccion_model.setPesaje_tipo(id_animal);

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
            String where="medida_anml_fecha = ?";

            String idtext= String.valueOf(medida_leche.getMedida_anml_fecha());
            int rtsl = database.update(TABLA_PESAJE,updatedValues, where, new String[]{idtext});
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
            itemview = inflater.inflate(R.layout.item_produccion_individual_carne, viewGroup, false);
        }
        tv_item_fecha = itemview.findViewById(R.id.tv_item_produccion_pesaje_animal);
        produccion_model = list_produccion.get(i);
        fecha = produccion_model.getPesaje_fecha_ingreso();
        tipo = produccion_model.getPesaje_tipo();

        if (fecha  != null & tipo != null){
            tv_item_fecha.setText(fecha);
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
