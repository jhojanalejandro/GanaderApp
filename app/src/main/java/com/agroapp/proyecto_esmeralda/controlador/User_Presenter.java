package com.agroapp.proyecto_esmeralda.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Produccion_Mensual_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.adapters.User_Recycle_View;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.interfaces.User_Interface;
import com.agroapp.proyecto_esmeralda.modelos.User_Model;

import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class User_Presenter implements User_Interface {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String descrip = "descrypt";
    public static final String KEY = "1Hbfh667adfDEJ78";
    private static final String ALGORITHM = "AES";
    private RecyclerView  recyclerView;
    private long timeLeftInMilliseconds = 16000;
    User_Recycle_View user_viewhollder;
    Context context;
    private Produccion_Mensual_Recycle_Adapter produccion_viewhollder;
    private DocumentReference farm_ref;

    public User_Presenter(RecyclerView recyclerView, Context context, DocumentReference animales_refefecnce) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.farm_ref = animales_refefecnce;
    }

    public User_Presenter(Context context) {
        this.context = context;
    }

    @Override
    public void show_users(RecyclerView recyclerView, String id_propietario, String farm_name, LinearLayoutManager layoutManager) {
        ArrayList<User_Model> user_list = new ArrayList<>();
        user_viewhollder = new User_Recycle_View(context, R.layout.item_usuario, user_list);
        CollectionReference query = db.collection("usuarios");
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                User_Model modelo_usuario;
                modelo_usuario = documentSnapshot.toObject(User_Model.class);
                String ids = documentSnapshot.getId();
                String id_propietario_traido = modelo_usuario.getUser_id();
                String cedula = modelo_usuario.getUser_cedula();
                try {
                    cedula = descrypt(cedula);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (id_propietario.equals(id_propietario_traido)) {
                    modelo_usuario.setUser_tipo(ids);
                    modelo_usuario.setUser_cedula(cedula);
                    user_list.add(modelo_usuario);
                }

            }
            recyclerView.setAdapter(user_viewhollder);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void user_register(User_Model class_name, ProgressDialog progressDialog, String clave, String mail) {
        DocumentReference doref = db.collection("usuarios").document();
        doref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    Toast.makeText(context, "YA EXISTE ESTA CEDULA , VERIFIQUE SU NUMERO O COMUNIQUESE SI CREE AVER SIDO SUBPLANTADO", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    final FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(mail, clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                doref.set(class_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            ActionCodeSettings actionCodeSettings =
                                                    ActionCodeSettings.newBuilder()
                                                            // URL you want to redirect back to. The domain (www.example.com) for this
                                                            // URL must be whitelisted in the Firebase Console.
                                                            .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                                                            // This must be true
                                                            .setHandleCodeInApp(true)
                                                            .setIOSBundleId("com.agroapp.proyecto_esmeralda")
                                                            .setAndroidPackageName(
                                                                    "com.example.android",
                                                                    true, /* installIfNotAvailable */
                                                                    "12"    /* minimumVersion */)
                                                            .build();
                                            auth.sendSignInLinkToEmail(mail, actionCodeSettings)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(context, "Email sent.", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                            Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(context, "Ahora puedes  disfrutar de la app", Toast.LENGTH_SHORT).show();
                                            Intent p_admin = new Intent(context, MainActivity.class);
                                            context.startActivity(p_admin);
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Create an English-German translator:
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void check_personal(String user_name, String user_key, SharedPreferences preference, ProgressDialog progressDialog) {
        CollectionReference uno = db.collection("usuarios");
        uno.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(context, "No Existen Usuarios Registrados", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            User_Model user_model = documentSnapshot.toObject(User_Model.class);
                            String clave_encrip = user_model.getUser_clave();
                            String tipo = user_model.getUser_tipo();
                            String id_prop = documentSnapshot.getId();
                            String id_user = user_model.getUser_id();
                            int nivel_acceso = user_model.getNivel_acceso();
                            Boolean cuenta_activa = user_model.getActivacion_cuenta();
                            ArrayList<String> fincas = user_model.getFincas();
                            String gmail = user_model.getUser_gmail();
                            String alias = user_model.getUser_alias();
                            String type_user = user_model.getUser_tipo();
                            try {
                                descrip = descrypt(clave_encrip);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (user_name.equals(gmail) || user_name.equals(alias) & descrip.equals(user_key)) {
                                if (cuenta_activa) {
                                    if (tipo.equals("propietario") & id_user.equals("vacio")) {
                                        uno.document(id_prop).update("id_user", id_prop);
                                        id_user = id_prop;
                                    }
                                    sign_in(gmail, user_key, type_user, alias, id_user, id_prop, preference, progressDialog, fincas, nivel_acceso);
                                    return;
                                } else {
                                    Toast.makeText(context, "CUENTA INACTIVA: ", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "CUENTA INACTIVA VERIFIQUE CON EL EMPLEADOR LA ACTIVACION DE LA CUENTA", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            } else {
                                startTimer();
                                progressDialog.dismiss();
                            }


                        }
                    }

                }

            }
        });


    }

    @Override
    public void lista_credenciales( LinearLayoutManager layoutManager) {
        CollectionReference produccion_ref = db.collection("produccion");

        ArrayList<Produccion_Model> list_animal = new ArrayList<>();
        produccion_viewhollder = new Produccion_Mensual_Recycle_Adapter(context, R.layout.item_animal_first, list_animal);
        produccion_ref.whereEqualTo("anml_tipo", "bovino").whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Produccion_Model modelo_produccion = new Produccion_Model();
                    modelo_produccion = documentSnapshot.toObject(Produccion_Model.class);
                    String ids = documentSnapshot.getId();
                    modelo_produccion.setProd_obs(ids);
                    list_animal.add(modelo_produccion);
                }
                recyclerView.setAdapter(produccion_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 10000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;

            }

            @Override
            public void onFinish() {
                timeLeftInMilliseconds = 0;
                Toast.makeText(context, "Error: verifique la información", Toast.LENGTH_LONG).show();
            }

        }.start();
    }

    @Override
    public void detalle_personal(String id_user, TextView tv_nombre, TextView
            tv_cedula, TextView tv_telefono, EditText edt_tipo, EditText nivel_acceso) {
        DocumentReference uno = db.collection("usuarios").document(id_user);
        uno.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User_Model user_model = task.getResult().toObject(User_Model.class);
                    String nombre = user_model.getUser_nombre();
                    String tipo = user_model.getUser_tipo();
                    String finca1 = user_model.getFincas().get(0);
                    String finca2 = user_model.getFincas().get(1);
                    String finca3 = user_model.getFincas().get(2);
                    String finca4 = user_model.getFincas().get(3);
                    String nivel_acceso_ = String.valueOf(user_model.getNivel_acceso());
                    String telefono = String.valueOf(user_model.getUser_telefono());
                    String cedula = String.valueOf(user_model.getUser_cedula());
                    tv_nombre.setText(nombre);
                    tv_cedula.setText(cedula);
                    tv_telefono.setText(telefono);
                    edt_tipo.setText(tipo);
                    nivel_acceso.setText(nivel_acceso_);


                }
            }
        });

    }

    @Override
    public void actualizar_datos_personal(String id_user, int nivel, Long
            pago_mensual, ArrayList<String> fincas_array) {
        DocumentReference uno = db.collection("usuarios").document(id_user);
        uno.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User_Model user_model = task.getResult().toObject(User_Model.class);
                ArrayList<String> fincas_traidas = user_model.getFincas();
                Long pago_efectivo = user_model.getUser_pago_mensual();
                if (!fincas_traidas.get(0).equals("vacio")) {
                    fincas_array.set(0, fincas_traidas.get(0));
                } else if (!fincas_traidas.get(1).equals("vacio")) {
                    fincas_array.set(1, fincas_traidas.get(1));
                } else if (!fincas_traidas.get(2).equals("vacio")) {
                    fincas_array.set(2, fincas_traidas.get(2));
                } else if (!fincas_traidas.get(3).equals("vacio")) {
                    fincas_array.set(3, fincas_traidas.get(3));
                }
                if (pago_efectivo == 0L) {
                    pago_efectivo = pago_mensual;
                }
                uno.update("nivel_acceso", nivel, "user_pago_mensual", pago_efectivo, "fincas", fincas_array, "activacion_cuenta", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "CUENTA DE USUARIO ACTIVADA", Toast.LENGTH_SHORT).show();
                            Intent perfil_admin = new Intent(context, Perfil_admin_view.class);
                            context.startActivity(perfil_admin);
                        }
                    }
                });
            }
        });


    }

    @Override
    public void desactivar_cuenta(String id_user) {
        DocumentReference uno = db.collection("usuarios").document(id_user);
        uno.update("activacion_cuenta", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Cuenta De Usuario DESACTIVADA", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public Key generateKey() throws Exception {
        Key keys = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        return keys;
    }

    @Override
    public String descrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String ecryptedValue = new String(decryptedByteValue, "utf-8");

        return ecryptedValue;
    }

    @Override
    public String encrypt(String value) throws Exception {

        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;
    }

    @Override
    public void sign_in(String email, String password, String tipo_user, String alias, String
            id_propietario, String id_user, SharedPreferences preference, ProgressDialog
                                progressDialog, ArrayList<String> fincas, int nivel_acceso) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Intent manejo = new Intent(context, Inicio_Ganaderapp.class);
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString("type_user", tipo_user);
                        editor.putString("password", password);
                        editor.putString("id_propietario", id_propietario);
                        editor.putInt("nivel_acceso", nivel_acceso);
                        Set<String> set = new HashSet<>();
                        set.addAll(fincas);
                        editor.putStringSet("fincas", set);
                        editor.putString("id_usuario", id_user);
                        editor.putString("empleado", alias);
                        editor.apply();
                        context.startActivity(manejo);
                    } else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }


}
