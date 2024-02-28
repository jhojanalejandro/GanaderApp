package com.agroapp.proyecto_esmeralda.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseSQL extends SQLiteOpenHelper {

    //lo que esta dentro de (), son los campos que va a tener la tabla
    String tabla_animal = "CREATE TABLE animal (id integer primary key autoincrement, id_animal text, id_propietario text, nombre_finca text, anml_observaciones text, anml_tipo  text, anml_fecha_nacimiento text , anml_chapeta text, anml_padre text, anml_genero text, anml_madre text, anml_procedencia text, anml_g_vida text, anml_precio integer, anml_imagen text, anml_raza text)";
    String tabla_medida = "CREATE TABLE medida (id integer primary key autoincrement, id_animal text,id_propietario text, nombre_finca text, medida_observacoines text, medida_leche_am integer,medida_leche_pm integer,produc_anml_fecha text, medida_cont_dias integer, medida_promedio_animal integer, medidaesult integer, medida_litros_mes integer, medida_n_e text )";
    String tabla_palpacion = "CREATE TABLE palpacion (id integer primary key autoincrement, id_animal text,id_propietario text , nombre_finca text , palp_fecha_pre text, palp_observaciones text,palp_tratamiento text, palp_veterinario text, palp_cant_droga integer, palp_result text, palp_fecha text, palp_droga_aplicada text )";
    String tabla_parto = "CREATE TABLE parto (id integer primary key autoincrement, id_animal text,id_propietario text, nombre_finca text, parto_fecha text, parto_numero_cria text,parto_numero integer, parto_observaciones  text,parto_droga  text, parto_cant_droga integer, parto_n_padre text, parto_result text, parto_result_real text )";
    String tabla_enfermedad = "CREATE TABLE enfermedad (id integer primary key autoincrement, id_animal text,id_propietario text, nombre_finca text, efmd_fecha text, efmd_n_vet text,efmd_droga_aplicada integer, efmd_evento  text,efmd_cant_droga  text, efmd_obs text )";
    String tabla_vacunacion = "CREATE TABLE vacunacion (id integer primary key autoincrement, id_animal text,id_propietario text, nombre_finca text, vcn_fecha text, vcn_n_vet text,vcn_droga_aplicada integer, vcn_evento  text,vcn_cant_droga  text, vcn_obs text, vcn_tipo_eleccion text )";
    String tabla_empleado = "CREATE TABLE empleado (id integer primary key autoincrement,  perna_nombre text,perna_clave text, perna_cedula text ,perna_tipo text,perna_nomina text, perna_finca text, perna_telefono integer )";
    String tabla_finca = "CREATE TABLE finca (id integer primary key autoincrement,  finca_nombre text,finca_extesion  integer, finca_ubicacion  text, finca_promedio_anual text, finca_tipos_produccion text, finca_tipos_produccion2 text, finca_tipos_produccion3 text, finca_tipos_produccion4 text,finca_precio_produccion text, finca_precio_produccion2 text )";
    String tabla_insumo_finca = "CREATE TABLE insumo_finca (id integer primary key autoincrement, id_animal text,id_propietario text, nombre_finca text,  ins_finca_nombre text,ins_finca_tipo text, ins_finca_n_e  text,ins_fincaestante integer,ins_finca_cantidad integer, ins_finca_precio integer, ins_finca_precio_unitario integer, ins_finca_observaciones text  )";
    String tabla_herramientas = "CREATE TABLE herramoentas (id integer primary key autoincrement, id_animal text, id_propietario text, nombre_finca text , herra_cant integer, herra_nombre text, herra_precio integer, herra_observaciones text )";
    String tabla_pesaje = "CREATE TABLE pesaje (id integer primary key autoincrement, id_animal text, id_propietario text, nombre_finca text , pesaje_observacoines text, pesaje_peso  integer, produc_anml_fecha text, pesaje_n_e text )";
    String tabla_potrero = "CREATE TABLE potrero (id integer primary key autoincrement, id_propietario text, nombre_finca text, pto_nombre text, pto_extension text, pto_n_finca  text, pto_lote text, pto_tipo_pasto text, pto_estado text, fecha_salida text, pto_cant_anml_pto text, pto_observaciones text )";
    String tabla_insumo_animal = "CREATE TABLE insumo_animal (id integer primary key autoincrement,id_animal text, id_propietario text, nombre_finca text, ins_animal_tipo text, ins_animal_fecha text, ins_animal_observaciones  text, ins_animal_tratamiento text,ins_animal_droga text, ins_animal_cant_droga text , ins_animaletiro text, ins_animal_n_empleado text)";
    String tabla_insumo_potrero = "CREATE TABLE insumo_potrero (id integer primary key autoincrement, id_propietario text, nombre_finca text,ins_pto_nombre text, ins_pto_cant  integer, ins_pto_n_e text, ins_pto_observaciones text )";
    //CONTRUCTORES

    public BaseSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //METODOS
    @Override
    public void onCreate(SQLiteDatabase db) //onCreate, DEBE ESTAR PARA CREAR LA BASE DE DATOS (SQLiteDatabase db)
    {
        db.execSQL(tabla_medida);
        db.execSQL(tabla_animal);
        db.execSQL(tabla_empleado);
        db.execSQL(tabla_vacunacion);
        db.execSQL(tabla_enfermedad);
        db.execSQL(tabla_palpacion);
        db.execSQL(tabla_parto);
        db.execSQL(tabla_potrero);
        db.execSQL(tabla_herramientas);
        db.execSQL(tabla_pesaje);
        db.execSQL(tabla_insumo_finca);
        db.execSQL(tabla_finca);
        db.execSQL(tabla_insumo_animal);
        db.execSQL(tabla_insumo_potrero);
        //con esto estamos ejecutando lo que tiene la variale (creando la tabla)

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) //onUpgrade, PERMITE LA LA INFORMACION SE VAYA ACTUALIZANDO
    {
        //si la tabla existe, cada que se actualicela borrerla
        db.execSQL("DROP TABLE parto"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_parto);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE palpacion"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_palpacion);
        db.execSQL("DROP TABLE enfermedad"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_enfermedad);
        db.execSQL("DROP TABLE vacunacion"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_vacunacion);
        db.execSQL("DROP TABLE pesaje"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_pesaje);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE insumo_animal"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_insumo_animal);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE insumo_finca"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_insumo_finca);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE potrero"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_potrero);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE animal"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_animal);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE herramienta"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_herramientas);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE finca"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_finca);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA
        db.execSQL("DROP TABLE empleado"); // DROP TABLE,+ NOMBRE (CREATE TABLE CLIENTE ARRIBA DE LA TABLA) LA BORRA
        db.execSQL(tabla_empleado);// CON ESTA INSTRUCCION Y LA VUELVE Y LA CREA

    }


}
