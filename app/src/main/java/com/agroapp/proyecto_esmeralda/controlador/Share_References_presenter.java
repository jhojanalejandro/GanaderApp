package com.agroapp.proyecto_esmeralda.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Finca_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class Share_References_presenter<T> implements Share_References_interface {
    private Context contextt_;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Animal_Model animal_model = new Animal_Model();
    private int retorno = 0;

    public Share_References_presenter(Context contextt_) {
        this.contextt_ = contextt_;
    }

    @Override
    public String id_propietario(SharedPreferences preferences) {
        String id_propietario = preferences.getString("id_propietario", null);
        if (id_propietario != null) {
            return id_propietario;
        } else {
            return null;
        }
    }
    @Override
    public int nivel_acceso(SharedPreferences preferences) {
        int  nivel_acceso = preferences.getInt("nivel_acceso", 0);
        if (nivel_acceso > 0) {
            return nivel_acceso;
        } else {
            return 0;
        }
    }

    @Override
    public CollectionReference[] referencedb_c(String id_propietario,String farm_name,String id_animal) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        CollectionReference animales_ref = db.collection("usuarios").document(id_propietario).collection("fincas").document(farm_name).collection("animales");
        final CollectionReference registro_animales_ref = animales_ref.document(id_animal).collection("registro_animal");
        CollectionReference[] referencias = {animales_ref,registro_animales_ref };
        return referencias;
    }
    @Override
    public DocumentReference[] referencedb_d(String id_propietario,String farm_name,String id_animal) {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        DocumentReference fincas_ref = db.collection("usuarios").document(id_propietario).collection("fincas").document(farm_name);
        final DocumentReference animal_ref = fincas_ref.collection("animales").document(id_animal);
        DocumentReference[] referencias = {fincas_ref,animal_ref };
        return referencias;
    }

    @Override
    public void fincas(SharedPreferences preferences, ArrayList fincas) {
        Set<String> set = preferences.getStringSet("fincas", null);
        if (set == null) {
            fincas.set(0,"vacio");
        } else {
            fincas.addAll(set);
        }

    }

    @Override
    public String id_usuario(SharedPreferences preferences) {
        String id_usuario = preferences.getString("id_usuario", null);
        if (id_usuario != null) {
            return id_usuario;
        } else {
            return null;
        }
    }


    @Override
    public String cedula(SharedPreferences preferences) {
        String cedula = preferences.getString("cedula", null);
        if (cedula != null) {
            return cedula;
        } else {
            return null;
        }
    }
    @Override
    public String tipo(SharedPreferences preferences) {
        String tipo = preferences.getString("tipo", null);
        if (tipo != null) {
            return tipo;
        } else {
            return null;
        }
    }
    @Override
    public String farm_name(SharedPreferences preferences) {
        String farm_name = preferences.getString("finca", null);
        if (farm_name != null) {
            return farm_name;
        } else {
            return null;
        }
    }
    @Override
    public ProgressDialog progressDialog_cargando(Context context){
        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("cargando...");
        progressBar.setCancelable(true);
        progressBar.show();
        return progressBar;
    }
    @Override
    public ProgressDialog progressDialog_fallo(Context context){
        ProgressDialog progressBar = new ProgressDialog(context);

        progressBar.setTitle("fallo En El Registro ...");
        progressBar.setCancelable(true);
        progressBar.show();
        progressBar.setButton("volver a intentar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.dismiss();

            }
        });
        return progressBar;
    }

    @Override
    public String id_subasta(SharedPreferences preferences) {
        String id_subasta = preferences.getString("id_subasta", null);
        if (id_subasta != null) {
            return id_subasta;
        } else {
            return null;
        }
    }
    @Override
    public void spinner_farm(Spinner spinner_farm, Context context, TextView tv_farm_name,String id_usuario) {

        Query query = db.collection("usuarios").document(id_usuario).collection("fincas");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String finca_busqueda = "";
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> fincas = new ArrayList<String>();
                    for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                        finca_busqueda = readData.get("finca_nombre").toString();
                        fincas.add(finca_busqueda);
                    }
                    if (fincas != null) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, fincas);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_farm.setAdapter(arrayAdapter);
                        spinner_farm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                Object item = parent.getItemAtPosition(pos);
                                String farm_name = (String) item;
                                tv_farm_name.setText(farm_name);
                                if (farm_name == null){
                                    Toast.makeText(context, "No Te Puedes registrar Si no Existen Fincas Registradas ", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Por Favor Registra Una Finca En El Menu Superior " , Toast.LENGTH_LONG).show();
                                }

                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(context, "No Se Han Seleccionado Fincas", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("nombre de la finca", e.getMessage());
            }
        });

    }


    @Override
    public String paddock_name(SharedPreferences preferences) {
        String paddock_name = preferences.getString("paddock_name", null);
        if (paddock_name != null) {
            return paddock_name;
        } else {
            return null;
        }
    }

    @Override
    public String user_type(SharedPreferences preferences) {
        String type_user = preferences.getString("type_user", null);
        if (type_user != null) {
            return type_user;
        } else {
            return null;
        }
    }
    @Override
    public String user_name(SharedPreferences preferences) {
         String user_name = preferences.getString("empleado", null);
        if (user_name != null) {
            return user_name;
        } else {
            return null;
        }
    }
    @Override
    public String animal_name(SharedPreferences preferences) {
        String nombre_animal = preferences.getString("nombre_animal", null);
        if (nombre_animal != null) {
            return nombre_animal;
        } else {
            return null;
        }
    }

    @Override
    public String id_animal(SharedPreferences preference) {
        String id_animal = preference.getString("id_animal", null);
        if (id_animal != null) {
            return id_animal;
        } else {
            return null;
        }
    }

    @Override
    public int[] date_picker() {
        int dia_hoy, mes_hoy, ano_hoy;
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        mes_hoy = calendarNow.get(Calendar.MONTH) + 1;
        ano_hoy = calendarNow.get(Calendar.YEAR);
        dia_hoy = calendarNow.get(Calendar.DAY_OF_MONTH) ;
        return new int[]{dia_hoy, mes_hoy, ano_hoy};
    }

    @Override
    public int[] parse_date(String fechaEntera) {
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
            month = calendario.get(Calendar.MONTH) + 1;
            //obtener el dia del mes (1-31)
            day = calendario.get(Calendar.DAY_OF_MONTH);

            //...mas campos...

        } catch (ParseException ex) {
            //manejar excepcion
        }
        return new int[]{day, month, year};    }

    @Override
    public void next_Intent(Context context, Class aClass) {
        Intent search = new Intent(context, aClass);
        context.startActivity(search);

    }

    @Override
    public void home_data_farm(String id_propietario, String farm_name, TextView tv_extension, TextView tv_ubicacion) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        fincas_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String extesion, ubicacion;
                if (documentSnapshot.exists()) {
                    Finca_model finca_model = new Finca_model();
                    finca_model= documentSnapshot.toObject(Finca_model.class);
                    extesion = String.valueOf(finca_model.getFinca_extesion());
                    ubicacion = finca_model.getFinca_ubicacion();

                    tv_ubicacion.setText(ubicacion);
                    tv_extension.setText(extesion + "" + "Hectareas");

                }

            }
        });
    }

    @Override
    public void count_cant_type_animal(String id_propietario,String farm_name, String type_animal, String etapa_tipo, TextView textView) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");

        coreff.whereEqualTo("anml_tipo",type_animal).whereEqualTo("anml_etapa_tipo",etapa_tipo).whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String cant_animals = "0";
                retorno = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()){
                        animal_model = documentSnapshot.toObject(Animal_Model.class);
                        retorno  +=1;
                        cant_animals= String.valueOf(retorno);
                    }

                }

                textView.setText(cant_animals);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return;
            }
        });

    }

    @Override
    public void count_cant_animals_adult(String id_propietario,String farm_name, String type_animal, String etapa_tipo, TextView textView) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");

        coreff.whereEqualTo("anml_tipo",type_animal).whereEqualTo("anml_genero", "hembra").whereEqualTo("anml_etapa_tipo",etapa_tipo).whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String cant_animals = "0";
                retorno = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()){
                        animal_model = documentSnapshot.toObject(Animal_Model.class);
                        retorno  +=1;
                        cant_animals= String.valueOf(retorno);
                    }

                }

                textView.setText(cant_animals);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return;
            }
        });

    }

    @Override
    public void count_cant_toros_animal(String id_propietario, String farm_name, String type_animal, String etapa_tipo, TextView textView, String genero) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");

        coreff.whereEqualTo("anml_tipo",type_animal).whereEqualTo("anml_etapa_tipo",etapa_tipo).whereEqualTo("anml_salida", "vacio").whereEqualTo("anml_genero", genero).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String cant_animals = "0";
                retorno = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()){
                        animal_model = documentSnapshot.toObject(Animal_Model.class);
                        retorno  +=1;
                        cant_animals= String.valueOf(retorno);
                    }

                }
                textView.setText(cant_animals);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return;
            }
        });

    }

    @Override
    public void count_cant_animales(String id_propietario, String farm_name, TextView textView) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");

        coreff.whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String cant_animals = "0";
                retorno = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    animal_model = documentSnapshot.toObject(Animal_Model.class);
                    retorno  +=1;
                    cant_animals = String.valueOf(retorno);
                }
                textView.setText(cant_animals);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(contextt_, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
