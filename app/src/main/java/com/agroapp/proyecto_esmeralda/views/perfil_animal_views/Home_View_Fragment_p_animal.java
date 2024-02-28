package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Home_View_Fragment_p_animal extends Fragment {

    private View view;
    SharedPreferences preferences;

    Animal_Model animales_model;
    ImageView foto_animal;
    String nombre_animal,nombre_madre,nombre_padre,fecha_nacimiento,chapeta,genero,raza,ruta_foto,peso,precio,etapa_animal,estado_animal;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    Uri imageUri;
    private ProgressDialog progressBar;

    private int PICK_FOTO = 100;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv_animal_nanme, tv_chapeta, tv_etapa,tv_date_birt, tv_raza, tv_genero, tv_estado_animal,tv_padre, tv_madre, tv_peso, tv_precio;
    Button btn_subir_foto;
    String farm_name, id_animal, id_propietario;
    Share_References_interface share_references_interface;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inicio_p_animal, container, false);
        iniciar_v();
        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        if (farm_name != null && id_animal != null) {
            traer_detalles_animal();
        }

        btn_subir_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        return view;
    }

    private void traer_detalles_animal(){

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document(id_animal);
        dos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    animales_model = task.getResult().toObject(Animal_Model.class);
                    nombre_animal = animales_model.getAnml_nombre();
                    if (!id_animal.equals("")){
                        chapeta = animales_model.getAnml_chapeta();
                        genero = animales_model.getAnml_genero();
                        raza = animales_model.getAnml_raza();
                        estado_animal = animales_model.getAnml_salida();
                        precio = String.valueOf(animales_model.getAnml_precio());
                        peso = String.valueOf(animales_model.getAnml_prod_kilos());
                        nombre_madre = animales_model.getAnml_madre();
                        nombre_padre = animales_model.getAnml_padre();
                        fecha_nacimiento = animales_model.getAnml_fecha_nacimiento();

                        etapa_animal = animales_model.getAnml_etapa_tipo();
                    }

                }

                if (estado_animal.equals("vacio")){
                    tv_estado_animal.setText("ACTIVO");
                }else {
                    tv_estado_animal.setText(estado_animal.toUpperCase());
                }
                tv_madre.setText(nombre_madre.toUpperCase());
                tv_padre.setText(nombre_padre.toUpperCase());
                tv_peso.setText(peso);
                tv_precio.setText(precio);
                tv_chapeta.setText(chapeta);
                tv_animal_nanme.setText(nombre_animal.toUpperCase());

                tv_etapa.setText(etapa_animal.toUpperCase());
                tv_genero.setText(genero.toUpperCase());
                tv_raza.setText(raza.toUpperCase());
                tv_date_birt.setText(fecha_nacimiento);
                ruta_foto = animales_model.getAnml_imagen();
                //datos foto
                if (!ruta_foto.equals("vacio")){
                    Glide
                            .with(view.getContext())
                            .load(ruta_foto)
                            .apply(RequestOptions.circleCropTransform())
                            .into(foto_animal);
                }


            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_FOTO);
    }

    private void iniciar_v() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("subiendo");
        storageReference = FirebaseStorage.getInstance().getReference();
        btn_subir_foto = view.findViewById(R.id.btn_subir_foto_animal);
        foto_animal = view.findViewById(R.id.img_foto);
        tv_precio = view.findViewById(R.id.tv_d_animal_precio);
        tv_peso = view.findViewById(R.id.tv_d_animal_peso);
        tv_raza = view.findViewById(R.id.tv_d_animal_i_raza);
        tv_padre = view.findViewById(R.id.tv_d_animal_n_padre);
        tv_madre = view.findViewById(R.id.tv_d_animal_n_madre);

        tv_etapa = view.findViewById(R.id.tv_d_animal_i_etapa_tipo);
        tv_chapeta = view.findViewById(R.id.tv_d_animal_i_chapeta);
        tv_estado_animal = view.findViewById(R.id.tv_d_animal_estado_animal);

        tv_genero = view.findViewById(R.id.tv_d_animal_genero);
        tv_animal_nanme = view.findViewById(R.id.tv_d_animal_i_nombre);
        tv_date_birt = view.findViewById(R.id.tv_d_animal_i_f_nacimiento);

        share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", MODE_PRIVATE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_FOTO) {
            Snackbar.make(view, "deseas agregar este registro", Snackbar.LENGTH_INDEFINITE)
                    .setAction("agregar foto", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressBar = new ProgressDialog(getContext());
                            progressBar.setTitle("Subiendo Foto...");
                            progressBar.setCancelable(false);
                            progressBar.show();
                            DocumentReference[] animal_array = share_references_interface.referencedb_d(id_propietario, farm_name, id_animal);
                            DocumentReference animal_ref = animal_array[1];
                            String img = data.getData().toString();
                            Perfil_Animal_Interface perfil_animal_interface = new Perfil_animal_Presenter(getContext(),animal_ref);
                            perfil_animal_interface.update_photo(img, progressDialog);
                        }

                    }).show();
            imageUri = data.getData();
            if (imageUri != null) {
                foto_animal.setImageURI(imageUri);
            }
        }


    }

    private void guardar_foto() {
    }
    
    /*private  void guardar_foto(){

        final StorageReference foto_ref =  storageReference.child("imagenes").child(farm_name).child(nombre_animal).child(imageUri.getLastPathSegment());
        foto_ref.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                //Preguntamos si el task de subir la imagen fue exitoso, si lo es retornamos la url de descarga del archivo subido , sino lanzamos una exception.
                if (!task.isSuccessful()) {
                    throw task.getException();
                }else if (task.isSuccessful()){
                    Toast.makeText(view.getContext(), "subida extosa", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }

                return foto_ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //Luego si se utiliza addOnCompleteListener para obtener ese enlace
                if (task.isSuccessful()) {
                    //Obtenemos el uri del enlace
                    final Uri uri_foto = task.getResult();
                    DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
                    DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
                    final DocumentReference coreff = fincas_ref.collection("animales").document(id_animal);
                    coreff.update("anml_imagen",uri_foto.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(view.getContext(), "registro  exitoso", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "error" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }   */


}