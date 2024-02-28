package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Offlinne_Connexion_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class Animal_Register_View_Fragment extends AppCompatDialogFragment implements View.OnClickListener {

    private EditText edt_nombre, edt_madre, edt_padre, edt_fecha_nacimiento, edt_chapeta, edt_anml_peso, edt_anml_precio;
    private ImageButton ibtn_registro_animal_nacimiento_calendario;
    private View view_;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences preferences;
    private String n_empleado, farm_name, tipo_vaca = "vaca", imagen_animal;
    private Button btn_registrar_animal, btn_cancelar_r;
    private ImageView btn_subir_foto;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private Pesaje_Animal_Model ficha_pesaje_carne;
    private int PICK_FOTO = 100;
    private Uri imageUri;
    Manejo_Animal_Interface manejo_animal_interface;
    Share_References_interface share_references_interface;
    private ProgressDialog progressBar;
    Produccion_Model ficha_produccion;

    private Spinner tipos_spinner, spinner_genero, spinner_lotes, spinner_procedencia, spinner_etapa, spinner_raza, spinner_raza_equinos;
    private String[] tipo, array_genero, array_procedencia, array_raza_bovinos, array_etapa, array_raza_equinos, array_lotes;
    private ArrayAdapter comboAdapter, genero_adapter, procedencia_adapter, lote_adapter, raza_adapter, raza_equino_adapter, etapa_adapter;
    private String nombre, chapetas, id_propietario, fecha, genero_eleccion, eleccion_procedencia, eleccion_etapa, eleccion_raza, eleccion_tipo, eleccion_lote, equino = "equino", bovino = "bovino";
    private int dia_hoy, ano_hoy, mes_hoy;
    Double peso,sumado,cant_ins_gasto,precio;
    private DatePickerDialog datePickerDialog;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view_ = inflater.inflate(R.layout.registro_animal_dialog, null);

        iniciar_activity();

        int[] fecha_hoy = share_references_interface.date_picker();
        dia_hoy = fecha_hoy[0];
        mes_hoy = fecha_hoy[1];
        ano_hoy = fecha_hoy[2];
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                dismiss();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        tipo = new String[]{"bovino", "equino"};
        array_genero = new String[]{"hembra", "macho"};
        array_etapa = new String[]{"inicial", "media", "productiva"};
        array_raza_bovinos = new String[]{"Pardo-suiza", "Roja-sueca", "Holstein-Roja", "Brahman", "Erchi", "Guir", "Angus", "Simbra", "Befmaster", "guirolando", "Simental", "Bon Blanco-oreja-negra", "Bon-Negro-oreja-Blanca", "Holstein", "Jersey", "Cebu", "Normando", "Beefmaster", "Simmental", "Hereford"};
        array_raza_equinos = new String[]{"mulo", "Apalusa", "miniatura", "Schleswig", "Holstein", "Sorraia", "Tarpán", "macho", "caballo", "burrro", "mula"};
        array_procedencia = new String[]{"comprado", "nacido", "trasladado"};
        array_lotes = new String[]{"horras", "novillos01", "novillos02", "novillos03", "lote01", "lote02", "lote03", "lote04", "terneras01", "terneras02", "terneras03"};
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        manejo_animal_interface = new Manejo_Animal_Presenter(getContext(),coreff);

        spinner_tipos();
        spinner_generos();
        spinner_lote();
        spinner_etapa();
        spinner_procedencia();

        if (farm_name == null) {
            Toast.makeText(getContext(), "no se podra complletar el registro por fallos en los datos de recursos", Toast.LENGTH_LONG).show();
        }
        spinner_raza.setAdapter(raza_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_raza_bovinos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_raza.setAdapter(arrayAdapter);
        spinner_raza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_raza = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tipos_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_tipo = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "error: campo vacio", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_procedencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_procedencia = (String) item;
                if (eleccion_procedencia.equals("comprado")) {
                    view_.findViewById(R.id.ln_r_precio_anml).setVisibility(View.VISIBLE);
                } else {
                    view_.findViewById(R.id.ln_r_precio_anml).setVisibility(View.GONE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "sin eleccion", Toast.LENGTH_SHORT).show();

            }
        });
        spinner_lotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_lote = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view_).setTitle("Registrar Animal");
        btn_cancelar_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return builder.create();
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_FOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_FOTO) {
            imageUri = data.getData();
            btn_subir_foto.setImageURI(imageUri);
        }
    }


    private void spinner_tipos() {
        tipos_spinner.setAdapter(comboAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipo);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipos_spinner.setAdapter(arrayAdapter);
    }

    private void spinner_generos() {
        spinner_genero.setAdapter(genero_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_genero);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_genero.setAdapter(arrayAdapter);
        spinner_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                genero_eleccion = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinner_procedencia() {
        spinner_procedencia.setAdapter(procedencia_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_procedencia);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_procedencia.setAdapter(arrayAdapter);

    }

    private void spinner_etapa() {
        spinner_etapa.setAdapter(etapa_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_etapa);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_etapa.setAdapter(arrayAdapter);
        spinner_etapa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_etapa = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinner_lote() {
        spinner_lotes.setAdapter(lote_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_lotes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lotes.setAdapter(arrayAdapter);

    }


    private void spinner_raza_equnos() {
        spinner_raza_equinos.setAdapter(raza_equino_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_raza_equinos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_raza_equinos.setAdapter(arrayAdapter);
        spinner_raza_equinos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_raza = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void iniciar_activity() {
        progressDialog = new ProgressDialog(getContext());

        storageReference = FirebaseStorage.getInstance().getReference();

        btn_subir_foto = view_.findViewById(R.id.img_r_upload_foto_animal);
        progressBar = new ProgressDialog(getContext());
        edt_nombre = view_.findViewById(R.id.edt_anml_nombre);
        edt_chapeta = view_.findViewById(R.id.edt_anml_chapeta);
        edt_anml_peso = view_.findViewById(R.id.edt_anml_peso);
        edt_anml_precio = view_.findViewById(R.id.edt_anml_precio);
        edt_fecha_nacimiento = view_.findViewById(R.id.edt_anml_f_nacim_animal);
        edt_padre = view_.findViewById(R.id.edt_anml_padre_n);
        tipos_spinner = view_.findViewById(R.id.spinner_tipo_animal);
        edt_madre = view_.findViewById(R.id.edt_anml_madre_n);
        spinner_raza_equinos = view_.findViewById(R.id.spinner_equino_raza);
        spinner_raza = view_.findViewById(R.id.spinner_registro_razas);
        spinner_genero = view_.findViewById(R.id.spinner_genero_animal);
        spinner_lotes = view_.findViewById(R.id.spinner_registro_lotes);
        spinner_procedencia = view_.findViewById(R.id.spinner_procedencia);
        spinner_etapa = view_.findViewById(R.id.spinner_etapa_animal);

        share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        btn_registrar_animal = view_.findViewById(R.id.btn_animal_r);

        btn_cancelar_r = view_.findViewById(R.id.btn_r_animal_cancelar);
        ibtn_registro_animal_nacimiento_calendario = view_.findViewById(R.id.ibtn_anml_f_nacim);
        btn_registrar_animal.setOnClickListener(this);
        btn_subir_foto.setOnClickListener(this);
        ibtn_registro_animal_nacimiento_calendario.setOnClickListener(this);

    }

    public String datepikers_nacimiento() {
        Calendar calendar = Calendar.getInstance();
        dia_hoy = calendar.get(calendar.DAY_OF_MONTH);
        mes_hoy = calendar.get(Calendar.MONTH) + 1;
        ano_hoy = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                edt_fecha_nacimiento.setText(dia + "/" + mes + "/" + ano);
            }
        }, ano_hoy, mes_hoy, dia_hoy);
        datePickerDialog.show();
        return String.valueOf(edt_fecha_nacimiento);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_animal_r:

                Snackbar deseas_agregar_este_registro = Snackbar.make(v, "deseas agregar este registro", Snackbar.LENGTH_LONG);
                deseas_agregar_este_registro.setAction("agregar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        progressBar.setTitle("cargando...");
                        progressBar.setCancelable(false);
                        progressBar.setIcon(R.drawable.ic_three_anmls_small);
                        progressBar.show();
                        if (edt_anml_precio.getText().toString().length() == 0) {
                            precio = 0.0;
                        } else {
                            if (eleccion_procedencia.equals("comprado")) {
                                precio = Double.parseDouble(edt_anml_precio.getText().toString());
                                consultar_produccion(precio);
                            }
                        }
                        if (edt_anml_peso.getText().toString().length() == 0) {
                            peso = 0.0;
                        } else {
                            peso = Double.parseDouble(edt_anml_peso.getText().toString());

                        }

                        String padre_animal = edt_padre.getText().toString();
                        String fecha_nacimiento = edt_fecha_nacimiento.getText().toString();
                        String madre_animal = edt_madre.getText().toString();
                        nombre = edt_nombre.getText().toString();
                        chapetas = edt_chapeta.getText().toString();
                        if (imageUri != null){
                            imagen_animal = imageUri.toString();
                        }else{
                            imagen_animal = "vacio";
                        }
                        if (fecha_nacimiento.length() == 0){
                            fecha_nacimiento = "VACIO";
                        }
                        if (chapetas == null || nombre == null) {
                            Toast.makeText(view_.getContext(), "Es necesario agregar Un Nombre Y un Numero de identificacion ", Toast.LENGTH_SHORT).show();

                        } else {
                            String meses = String.valueOf(mes_hoy);
                            String anios = String.valueOf(ano_hoy);
                            String dias = String.valueOf(dia_hoy);
                            fecha = dias + "/" + meses + "/" + anios;
                            Animal_Model animal_model = new Animal_Model(nombre, fecha, chapetas, eleccion_procedencia, farm_name, genero_eleccion, eleccion_tipo, eleccion_etapa, 0.0, peso, 0.0, "vacio", padre_animal, "vacio", madre_animal, "vacio", "vacio", eleccion_lote, 0.0,precio, fecha_nacimiento, imagen_animal, eleccion_raza, "obs");
                            if (Probar_connexion.Prueba(getContext())) {
                                manejo_animal_interface.animal_register( animal_model, peso, n_empleado,progressBar);
                            } else {
                                Offlinne_Connexion_Interface offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());
                                int r = offlinne_connexion_interface.data_anmls_register_offline(getContext(), animal_model, id_propietario);
                                if (r > 0 ){
                                    Toast.makeText(getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                    progressBar.dismiss();
                                }else {
                                    progressBar.setTitle("fallo En El Registro ...");
                                    progressBar.setCancelable(true);
                                    progressBar.show();
                                    progressBar.setButton("volver a intentar", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressBar.dismiss();

                                        }
                                    });                            }
                            }
                        }

                    }
                });
                deseas_agregar_este_registro.show();
                break;
            case R.id.ibtn_anml_f_nacim:
                if (eleccion_tipo.equals(equino)) {
                    view_.findViewById(R.id.spinner_equino_raza).setVisibility(View.VISIBLE);
                    view_.findViewById(R.id.spinner_registro_razas).setVisibility(View.GONE);
                    spinner_raza_equnos();
                } else {
                    view_.findViewById(R.id.spinner_equino_raza).setVisibility(View.GONE);
                    view_.findViewById(R.id.spinner_registro_razas).setVisibility(View.VISIBLE);
                }
                if (eleccion_procedencia.equals("nacido")) {
                    view_.findViewById(R.id.linearLayout_lote).setVisibility(View.GONE);
                }
                datepikers_nacimiento();
                break;
            case R.id.img_r_upload_foto_animal:
                openGallery();
                break;

        }
    }

    public void consultar_produccion(Double precio) {
        String meses = String.valueOf(mes_hoy);
        String anios = String.valueOf(ano_hoy);
        String dias = String.valueOf(dia_hoy);
        fecha = dias + "/" + meses + "/" + anios;
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    ficha_produccion = new Produccion_Model(precio, 0.0, 0.0, 0, 0, 0, 0,0.0 , fecha, 0.0, 0,false,false);
                    coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                        }
                    });
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                            fecha = ficha_produccion.getProd_fecha();
                            String id = documentSnapshot.getId();
                            int[] fecha_par = parsea_Fecha(fecha);
                            int mes_tra = fecha_par[1];
                            int anio_tra = fecha_par[2];
                            if (mes_tra == mes_hoy & anio_tra == ano_hoy) {
                                cant_ins_gasto = ficha_produccion.getProd_gasto_mensual();
                                if (cant_ins_gasto != 0) {
                                    sumado = cant_ins_gasto + precio;
                                    coreff.document(id).update("prod_gasto_mensual", sumado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(view_.getContext(), "Refgistro Exitoso", Toast.LENGTH_SHORT).show();
                                            dismiss();

                                        }
                                    });
                                    return;
                                }

                            } else {
                                ficha_produccion = new Produccion_Model(precio, 0.0,0.0, 0, 0, 0, 0, 0.0,  fecha,0.0, 0,false,false);
                                coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view_.getContext(), "Refgistro Exitoso", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                });
                                return;
                            }

                        }

                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view_.getContext(), "Error Al Realizar la Accion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int[] parsea_Fecha(String fechaEntera) {
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
        return new int[]{day, month, year};
    }

}


