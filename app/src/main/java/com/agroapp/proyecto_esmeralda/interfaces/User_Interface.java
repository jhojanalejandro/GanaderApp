package com.agroapp.proyecto_esmeralda.interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.modelos.User_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Key;
import java.util.ArrayList;

public interface User_Interface {
    void actualizar_datos_personal( String id_user, int nivel, Long pago_mensual, ArrayList<String> fincas);
    void detalle_personal( String id_user, TextView tv_nombre, TextView cedula, TextView tv_telefono, EditText tv_tipo, EditText edt_nivel_acceso);
    void user_register( User_Model class_name, ProgressDialog progressDialog, String clave, String gmail);
    void  check_personal( String user_name, String user_key, SharedPreferences preference, ProgressDialog progressDialog);
    String encrypt(String value) throws Exception;
    void lista_credenciales( LinearLayoutManager layoutManager);

    void sign_in(String email, String password, String tipo_user, String alias, String id_rpopietario, String id_user, SharedPreferences preference,ProgressDialog progressDialog,ArrayList<String> fincas, int nivel_acceso);
    Key generateKey() throws Exception;
    void show_users( RecyclerView recyclerView, String id_propietario, String farm_name, LinearLayoutManager linearLayoutManager);
    void desactivar_cuenta(String id_user);
    String descrypt(String value) throws Exception;



}
