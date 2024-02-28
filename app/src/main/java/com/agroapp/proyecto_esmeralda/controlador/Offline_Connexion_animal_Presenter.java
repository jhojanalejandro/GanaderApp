package com.agroapp.proyecto_esmeralda.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.agroapp.proyecto_esmeralda.activities.BaseSQL;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.interfaces.Offlinne_Connexion_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;
import com.agroapp.proyecto_esmeralda.modelos.Vacunacion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Emfermedad_Model;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Servicio_Model;

import java.util.ArrayList;

public class Offline_Connexion_animal_Presenter implements Offlinne_Connexion_Interface {

    private static final String BASE_DATOS = "prueba01sqlite";

    private static final String TABLA_MEDIDA = "medida", TABLA_PESAJE = "pesaje", TABLA_VACUNACION = "vacunacion", TABLA_ENFERMEDAD = "enfermedad", TABLA_PALPACION = "palpacion", TABLA_SERVICIO = "calor", TABLA_SECADO = "secado", TABLA_PARTO = "parto";

    Context contextt_;
    private int r = 0;

    private static SQLiteDatabase database;
    private static BaseSQL dbHelper;
    Perfil_Animal_Interface perfil_animal_presenter;

    public Offline_Connexion_animal_Presenter(Context contextt_) {
        this.contextt_ = contextt_;
    }

    public Offline_Connexion_animal_Presenter(Context contextt_, Perfil_Animal_Interface perfil_animal_presenter, Share_References_interface share_references_presenter) {
        this.contextt_ = contextt_;
        this.perfil_animal_presenter = perfil_animal_presenter;
        this.share_references_presenter = share_references_presenter;
    }

    Share_References_interface share_references_presenter = new Share_References_presenter(contextt_);

    @Override
    public void openW_data_type_offline() /*throws SQLException*/ {
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void openR_data_type_offline() /*throws SQLException*/ {
        database = dbHelper.getReadableDatabase();
    }

    @Override
    public void close_data_type_offline() {
        database.close();
    }

    @Override
    public ArrayList<Palpacion_Model> show_data_palpation_animal(Context context) {
        ArrayList<Palpacion_Model> list_palpa = new ArrayList<>();
        list_palpa = new ArrayList<>();
        Cursor cursor = database.query(TABLA_PALPACION, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String palp_fecha_pre = cursor.getString(cursor.getColumnIndex("palp_fecha_pre"));
                String palp_fecha = cursor.getString(cursor.getColumnIndex("palp_fecha"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                int palp_cant_droga = cursor.getInt(cursor.getColumnIndex("palp_cant_droga"));
                String palp_tratamiento = cursor.getString(cursor.getColumnIndex("palp_tratamiento"));
                String palp_veterinario = cursor.getString(cursor.getColumnIndex("palp_veterinario"));
                String palp_result = cursor.getString(cursor.getColumnIndex("palp_result"));
                String palp_droga_aplicada = cursor.getString(cursor.getColumnIndex("palp_droga_aplicada"));
                String palp_observaciones = cursor.getString(cursor.getColumnIndex("palp_observaciones"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));

                Palpacion_Model palpacion_model = new Palpacion_Model();
                palpacion_model.setObservaciones(palp_observaciones);
                palpacion_model.setTratamiento(palp_tratamiento);
                palpacion_model.setPalp_veterinario(palp_veterinario);
                palpacion_model.setCantidad_droga(palp_cant_droga);
                palpacion_model.setPalp_result(palp_result);
                palpacion_model.setTipo_registro("palpacion");
                palpacion_model.setFecha_registro(palp_fecha);
                palpacion_model.setPalp_fecha_pre(palp_fecha_pre);
                palpacion_model.setNombre_droga(palp_droga_aplicada);

                r = perfil_animal_presenter.data_type_anmls_register(palp_droga_aplicada, palp_cant_droga, id_propietario, nombre_finca, id_animal, true, palpacion_model);

                if (r > 0) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_PALPACION);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }

                }
                list_palpa.add(palpacion_model);
            } while (cursor.moveToNext());
        }
        return list_palpa;
    }

    @Override
    public ArrayList<Parto_Model> show_data_part_animal(Context context) {

        ArrayList<Parto_Model> part_list = new ArrayList<>();
        part_list = new ArrayList<>();
        Cursor cursor = database.query(TABLA_PARTO, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String result_real = cursor.getString(cursor.getColumnIndex("part_result_real"));
                String raza_animal = cursor.getString(cursor.getColumnIndex("part_father_name"));
                String part_date = cursor.getString(cursor.getColumnIndex("part_date"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                int parto_cant_droga = cursor.getInt(cursor.getColumnIndex("part_cant_drog"));
                String numero_hijo = cursor.getString(cursor.getColumnIndex("part_number_breeding"));
                int parto_numero_partos = cursor.getInt(cursor.getColumnIndex("part_number"));
                String n_employed = cursor.getString(cursor.getColumnIndex("part_user_name"));
                String n_padre = cursor.getString(cursor.getColumnIndex("part_father_name"));
                String parto_tratamiento = cursor.getString(cursor.getColumnIndex("part_treatment"));
                String parto_result = cursor.getString(cursor.getColumnIndex("part_result"));
                String parto_droga_aplicada = cursor.getString(cursor.getColumnIndex("part_drog_applied"));
                String parto_observaciones = cursor.getString(cursor.getColumnIndex("part_observations"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("farm_name"));

                Parto_Model parto_model = new Parto_Model();
                parto_model.setObservaciones(parto_observaciones);
                parto_model.setTratamiento(parto_tratamiento);
                parto_model.setPart_raza_cria(raza_animal);
                parto_model.setPart_father_name(n_padre);
                parto_model.setCantidad_droga(parto_cant_droga);
                parto_model.setPart_result_real(result_real);
                parto_model.setPart_result(parto_result);
                parto_model.setPart_number(parto_numero_partos);
                parto_model.setPart_number_breeding(numero_hijo);

                parto_model.setPart_raza_cria(raza_animal);
                parto_model.setTipo_registro("parto");
                parto_model.setFecha_registro(part_date);
                parto_model.setNombre_droga(parto_droga_aplicada);
                r = perfil_animal_presenter.data_type_anmls_register(parto_droga_aplicada, parto_cant_droga, id_propietario, nombre_finca, id_animal, false, parto_model);

                if (r > 0) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_PARTO);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }

                }
                part_list.add(parto_model);
            } while (cursor.moveToNext());
        }
        return part_list;
    }

    @Override
    public ArrayList<Servicio_Model> show_data_hot_animal(Context context) {

        ArrayList<Servicio_Model> list_calor = new ArrayList<>();
        list_calor = new ArrayList<>();
        Cursor cursor = database.query(TABLA_SERVICIO, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String calor_fecha = cursor.getString(cursor.getColumnIndex("calor_fecha"));
                int calor_cant_droga = cursor.getInt(cursor.getColumnIndex("calor_cant_droga"));
                String calor_tratamiento = cursor.getString(cursor.getColumnIndex("calor_trat"));
                String nombre_toro = cursor.getString(cursor.getColumnIndex("calor_n_toro"));
                String calor_droga_aplicada = cursor.getString(cursor.getColumnIndex("calor_droga_aplicada"));
                String calor_observaciones = cursor.getString(cursor.getColumnIndex("calor_obs"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));

                Servicio_Model servicio_model = new Servicio_Model();

                servicio_model.setObservaciones(calor_observaciones);
                servicio_model.setTratamiento(calor_tratamiento);
                servicio_model.setCantidad_droga(calor_cant_droga);
                servicio_model.setCalor_n_toro(nombre_toro);
                servicio_model.setTipo_registro("calor");
                servicio_model.setFecha_registro(calor_fecha);
                servicio_model.setNombre_droga(calor_droga_aplicada);

                r = perfil_animal_presenter.data_type_anmls_register(calor_droga_aplicada, calor_cant_droga, id_propietario, nombre_finca, id_animal, false, servicio_model);

                if (r > 0) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_SERVICIO);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }

                }
            } while (cursor.moveToNext());
        }
        return list_calor;
    }

    @Override
    public ArrayList<Gastos_Insumos> show_data_dry_animal(Context context) {

        ArrayList<Gastos_Insumos> list_secado;
        list_secado = new ArrayList<>();
        Cursor cursor = database.query(TABLA_SECADO, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                String secado_fecha = cursor.getString(cursor.getColumnIndex("secado_fecha"));
                int secado_cant_droga = cursor.getInt(cursor.getColumnIndex("secado_cant_droga"));
                String secado_tratamiento = cursor.getString(cursor.getColumnIndex("secado_trat"));
                String secado_droga_aplicada = cursor.getString(cursor.getColumnIndex("secado_droga_aplicada"));
                String secado_observaciones = cursor.getString(cursor.getColumnIndex("secado_obs"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));

                Gastos_Insumos secado_model = new Gastos_Insumos();
                secado_model.setObservaciones(secado_observaciones);
                secado_model.setTratamiento(secado_tratamiento);
                secado_model.setCantidad_droga(secado_cant_droga);
                secado_model.setTipo_registro("calor");
                secado_model.setFecha_registro(secado_fecha);
                secado_model.setNombre_droga(secado_droga_aplicada);

                r = perfil_animal_presenter.data_type_anmls_register(secado_droga_aplicada, secado_cant_droga, id_propietario, nombre_finca, id_animal, false, secado_model);

                if (r > 0) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_SECADO);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }

                }
                list_secado.add(secado_model);
            } while (cursor.moveToNext());
        }
        return list_secado;
    }

    @Override
    public int delet_data_type_animal(int id, String where, String table_name) {
        String[] args = new String[]{"" + id};
        return database.delete(table_name, where, args);

    }

    @Override
    public ArrayList<Vacunacion_Model> show_data_vaccination_animal(Context context) {

        ArrayList<Vacunacion_Model> list_vacunacion = new ArrayList<>();
        list_vacunacion = new ArrayList<>();
        Cursor cursor = database.query(TABLA_VACUNACION, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String vcn_fecha = cursor.getString(cursor.getColumnIndex("vcn_fecha"));
                int vcn_cant_droga = cursor.getInt(cursor.getColumnIndex("vcn_cant_droga"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                String vcn_vet = cursor.getString(cursor.getColumnIndex("vcn_n_vet"));
                String vcn_tratamiento = cursor.getString(cursor.getColumnIndex("vcn_trat"));
                String vcn_event = cursor.getString(cursor.getColumnIndex("vcn_evento"));
                String elecccion_tipo = cursor.getString(cursor.getColumnIndex("vcn_tipo_eleccion"));
                String vcn_droga_aplicada = cursor.getString(cursor.getColumnIndex("vcn_droga_aplicada"));
                String vcn_observaciones = cursor.getString(cursor.getColumnIndex("vcn_obs"));
                String vcn_n_e = cursor.getString(cursor.getColumnIndex("vcn_user_name"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));

                Vacunacion_Model ficha_vcn = new Vacunacion_Model();
                ficha_vcn.setObservaciones(vcn_observaciones);
                ficha_vcn.setTratamiento(vcn_tratamiento);
                ficha_vcn.setFecha_registro(vcn_fecha);
                ficha_vcn.setTipo_registro("vacunacion");
                ficha_vcn.setCantidad_droga(vcn_cant_droga);
                ficha_vcn.setNombre_droga(vcn_droga_aplicada);
                ficha_vcn.setVcn_evento(vcn_event);
                ficha_vcn.setVcn_veterinario(vcn_vet);
                ficha_vcn.setVcn_tipo_eleccion(elecccion_tipo);

                r = perfil_animal_presenter.data_type_anmls_register(vcn_droga_aplicada, vcn_cant_droga, id_propietario, nombre_finca, id_animal, false, ficha_vcn);

                if (r > 0) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_VACUNACION);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }

                }
                list_vacunacion.add(ficha_vcn);
            } while (cursor.moveToNext());
        }
        return list_vacunacion;
    }

    @Override
    public void update_data_measur_animal(Palpacion_Model per) {

    }

    @Override
    public ArrayList<Emfermedad_Model> show_data_deseas_animal(Context context) {

        ArrayList<Emfermedad_Model> list_enferm = new ArrayList<>();
        list_enferm = new ArrayList<>();
        Cursor cursor = database.query(TABLA_ENFERMEDAD, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));

                String efmd_fecha = cursor.getString(cursor.getColumnIndex("efmd_fecha"));
                int efmd_cant_droga = cursor.getInt(cursor.getColumnIndex("efmd_cant_droga"));
                String efmd_vet = cursor.getString(cursor.getColumnIndex("efmd_n_vet"));
                String efmd_n_empdo = cursor.getString(cursor.getColumnIndex("efmd_n_empleado"));
                String efmd_tratamiento = cursor.getString(cursor.getColumnIndex("efmd_trat"));
                String efmd_event = cursor.getString(cursor.getColumnIndex("efmd_evento"));
                String efmd_droga_aplicada = cursor.getString(cursor.getColumnIndex("efmd_droga_aplicada"));
                String efmd_observaciones = cursor.getString(cursor.getColumnIndex("efmd_obs"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));

                Emfermedad_Model ficha_efmd = new Emfermedad_Model();
                ficha_efmd.setFecha_registro(efmd_fecha);
                ficha_efmd.setEfmd_evento(efmd_event);
                ficha_efmd.setTratamiento(efmd_tratamiento);
                ficha_efmd.setObservaciones(efmd_observaciones);
                ficha_efmd.setTipo_registro("enfermedad");
                ficha_efmd.setCantidad_droga(efmd_cant_droga);
                ficha_efmd.setNombre_droga(efmd_droga_aplicada);
                ficha_efmd.setEfmd_veterinario(efmd_vet);
                r = perfil_animal_presenter.data_type_anmls_register(efmd_droga_aplicada, efmd_cant_droga, id_propietario, nombre_finca, id_animal, true, ficha_efmd);

                if (r > 0) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_ENFERMEDAD);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }

                }
                list_enferm.add(ficha_efmd);
            } while (cursor.moveToNext());
        }
        return list_enferm;
    }

    @Override
    public ArrayList<Medida_Leche_Model> show_data_measur_animal(Context context) {

        ArrayList<Medida_Leche_Model> list_produccion = new ArrayList<>();
        list_produccion = new ArrayList<>();
        Cursor cursor = database.query(TABLA_MEDIDA, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                String medida_fecha = cursor.getString(cursor.getColumnIndex("medida_fecha"));
                String medida_n_e = cursor.getString(cursor.getColumnIndex("medida_n_e"));
                Double medida_result_am = Double.valueOf(cursor.getString(cursor.getColumnIndex("medida_result_am")));
                Double medida_result_pm = Double.valueOf(cursor.getString(cursor.getColumnIndex("medida_result_pm")));
                String medida_observaciones = cursor.getString(cursor.getColumnIndex("medida_observaciones"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));

                int[] date = share_references_presenter.parse_date(medida_fecha);
                int dia = date[0];
                int mes = date[1];
                int ano = date[2];

                int registro = perfil_animal_presenter.data_measur_anmls_register(dia, mes, ano, medida_fecha, medida_observaciones, medida_result_pm, medida_result_am, false);
                Medida_Leche_Model produccion_model = new Medida_Leche_Model();
                produccion_model.setMedida_anml_fecha(medida_fecha);
                produccion_model.setMedida_tipo(id_animal);
                if (registro > 0 && Probar_connexion.Prueba(context)) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_MEDIDA);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }
                }
                list_produccion.add(produccion_model);
            } while (cursor.moveToNext());
        }
        return list_produccion;
    }

    @Override
    public ArrayList<Pesaje_Animal_Model> show_data_weighing_animal(Context context) {

        ArrayList<Pesaje_Animal_Model> list_produccion = new ArrayList<>();
        list_produccion = new ArrayList<>();
        Cursor cursor = database.query(TABLA_PESAJE, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String id_animal = cursor.getString(cursor.getColumnIndex("id_animal"));
                String id_propietario = cursor.getString(cursor.getColumnIndex("id_propietario"));
                String pesaje_fecha = cursor.getString(cursor.getColumnIndex("pesaje_fecha"));
                Double pesaje_result = Double.parseDouble(cursor.getString(cursor.getColumnIndex("pesaje_result")));
                String pesaje_observaciones = cursor.getString(cursor.getColumnIndex("pesaje_observaciones"));
                String nombre_finca = cursor.getString(cursor.getColumnIndex("nombre_finca"));
                String eleccion_tipo = cursor.getString(cursor.getColumnIndex("tipo"));

                int[] date = perfil_animal_presenter.parse_date(pesaje_fecha);
                int day = date[0];
                int month = date[1];
                int year = date[2];

                int registro = perfil_animal_presenter.data_weighing_anmls_register(pesaje_result, pesaje_observaciones, pesaje_fecha, day, month, year, id_propietario, nombre_finca, id_animal, false, eleccion_tipo);
                Pesaje_Animal_Model produccion_model = new Pesaje_Animal_Model();
                produccion_model.setPesaje_fecha_ingreso(pesaje_fecha);
                produccion_model.setPesaje_tipo(id_animal);
                if (registro > 0 && Probar_connexion.Prueba(context)) {
                    int delete = delet_data_type_animal(id, "id = ?", TABLA_PESAJE);
                    if (delete > 1) {
                        Toast.makeText(context, "Registo Exitoso", Toast.LENGTH_SHORT).show();
                    }
                }
                list_produccion.add(produccion_model);
            } while (cursor.moveToNext());
        }
        return list_produccion;
    }

    @Override
    public int data_type_anmls_register_offline(Context context, int anml_r_cant_dias, Double anml_r_mw, Double anml_r_resul_am, Double kilos, String anmls_r_date_noti, String anmls_r_observations, String anmls_r_treatment, String anmls_r_vet_name, String anmls_r_farm_name, int anmls_r_cant_drog, String anmls_r_result, String anmls_r_type, String anmls_r_event, String anmls_r_date, String anmls_r_number_breeding, int anmls_r_number_part, String anmls_r_father_name, String anmls_result_real, String anmls_r_drog_applied, String anmls_r_type_choice, String anmls_r_bull_name, String id_animal, String id_propietario) {
        try {

            ContentValues cursorcliente = new ContentValues();
            switch (anmls_r_type) {
                case "enfermedad":
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("efmd_fecha", anmls_r_date); //put trae lo que digite el usuario  al string identificacion
                    cursorcliente.put("efmd_n_vet", anmls_r_vet_name);
                    cursorcliente.put("efmd_droga_aplicada", anmls_r_drog_applied);
                    cursorcliente.put("efmd_evento", anmls_r_event);
                    cursorcliente.put("efmd_cant_droga", anmls_r_cant_drog);
                    cursorcliente.put("efmd_obs", anmls_r_observations);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                    break;
                case "parto":
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("part_date", anmls_r_date);
                    cursorcliente.put("part_result", anmls_r_result);
                    cursorcliente.put("part_drog", anmls_r_drog_applied);
                    cursorcliente.put("part_cant_drog", anmls_r_cant_drog);
                    cursorcliente.put("part_result_real", anmls_result_real);
                    cursorcliente.put("part_result", anmls_r_result);
                    cursorcliente.put("part_observations", anmls_r_observations);
                    cursorcliente.put("part_number", anmls_r_number_part);
                    cursorcliente.put("part_number_breeding", anmls_r_number_breeding);
                    cursorcliente.put("part_father_name", anmls_r_farm_name);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("id_animal", anmls_r_treatment);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                    break;
                case "palpacion":
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("palp_fecha", anmls_r_date); //put trae lo que digite el usuario  al string identificacion
                    cursorcliente.put("palp_fecha_pre", anmls_r_date_noti);
                    cursorcliente.put("palp_result", anmls_r_result);
                    cursorcliente.put("palp_droga_aplicada", anmls_r_drog_applied);
                    cursorcliente.put("palp_cant_droga", anmls_r_cant_drog);
                    cursorcliente.put("palp_veterinario", anmls_r_vet_name);
                    cursorcliente.put("palp_observaciones", anmls_r_observations);
                    cursorcliente.put("palp_tratamiento", anmls_r_treatment);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                    break;
                case "vacunacion":
                    cursorcliente.put("vcn_fecha", anmls_r_date);
                    cursorcliente.put("vcn_n_vet", anmls_r_vet_name);
                    cursorcliente.put("vcn_droga_aplicada", anmls_r_drog_applied);
                    cursorcliente.put("vcn_evento", anmls_r_event);
                    cursorcliente.put("vcn_cant_droga", anmls_r_cant_drog);
                    cursorcliente.put("vcn_obs", anmls_r_observations);
                    cursorcliente.put("vcn_tipo_eleccion", anmls_r_type_choice);
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                case "medida":
                    cursorcliente.put("medida_fecha", anmls_r_date);
                    cursorcliente.put("medida_leche_am", anml_r_resul_am);
                    cursorcliente.put("medida_cont_dias", anmls_r_cant_drog);
                    cursorcliente.put("medida_observaciones", anmls_r_observations);
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                    break;
                case "pesaje":
                    cursorcliente.put("pesaje_fecha", anmls_r_date);
                    cursorcliente.put("pesaje_peso", kilos);
                    cursorcliente.put("pesaje_observaciones", anmls_r_observations);
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                    break;
                case "secado":
                    cursorcliente.put("secado_fecha", anmls_r_date);
                    cursorcliente.put("secado_droga_aplicada", anmls_r_drog_applied);
                    cursorcliente.put("secado_cant_droga", anmls_r_cant_drog);
                    cursorcliente.put("secado_obs", anmls_r_observations);
                    cursorcliente.put("id_animal", id_animal);
                    cursorcliente.put("id_propietario", id_propietario);
                    cursorcliente.put("nombre_finca", anmls_r_farm_name);
                    break;
            }
            database.insert(anmls_r_type, null, cursorcliente); //le estamos diciendo que agregue los datos de la tabla 1
            //a la tabla fisica.

            r = (int) database.insert(anmls_r_type, null, cursorcliente);

        } catch (Exception ex) {
            Toast.makeText(context, "Error Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            r = -1;

            return r;

        }

        return r;
    }

    @Override
    public int data_anmls_register_offline(Context context, Animal_Model animal_model, String id_propietario) {
        try {
            ContentValues cursorcliente = new ContentValues();
            cursorcliente.put("id_propietario", id_propietario);
            cursorcliente.put("anml_fecha_nacimiento", animal_model.getAnml_fecha_nacimiento()); //put trae lo que digite el usuario  al string identificacion
            cursorcliente.put("anml_tipo", animal_model.getAnml_tipo());
            cursorcliente.put("anml_chapeta", animal_model.getAnml_chapeta());
            cursorcliente.put("anml_genero", animal_model.getAnml_genero());
            cursorcliente.put("anml_etapa_tipo", animal_model.getAnml_etapa_tipo());
            cursorcliente.put("anml_prod_kilos", animal_model.getAnml_prod_kilos());
            cursorcliente.put("anml_padre", animal_model.getAnml_padre());
            cursorcliente.put("anml_madre", animal_model.getAnml_madre());
            cursorcliente.put("anml_procedencia", animal_model.getAnml_procedencia());
            cursorcliente.put("anml_g_vida", animal_model.getAnml_g_vida());
            cursorcliente.put("anml_precio", animal_model.getAnml_precio());
            cursorcliente.put("anml_imagen", animal_model.getAnml_imagen());
            cursorcliente.put("anml_fecha_ingreso", animal_model.getAnml_fecha_ingreso());
            cursorcliente.put("anml_raza", animal_model.getAnml_raza());
            cursorcliente.put("anml_observaciones", animal_model.getAnml_observaciones());
            r = (int) database.insert("animal", null, cursorcliente);

        } catch (Exception ex) {
            Toast.makeText(context, "Error Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            r = -1;

            return r;

        }

        return r;
    }

}
