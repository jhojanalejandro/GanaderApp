package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.MODE_PRIVATE;

public class Mostrar_imagen extends AppCompatDialogFragment {

    private View view_;
    ImageView img_foto;
    FirebaseStorage firebaseStorage;
    Animal_Model modelo_animal;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    StorageReference storageReference;
    String farm_name,id_animal, id_propietario;
    TextView tv_nombre;
    private String ruta_foto,nombre_animal;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view_  = inflater.inflate(R.layout.mostrar_imagen_dialog,null);
        iniciar_activity();
        Share_References_presenter  share_references_presenter =new Share_References_presenter(getContext());
        id_animal = share_references_presenter.id_animal(preferences);
        farm_name = share_references_presenter.farm_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        if ( id_animal == null) {
            Toast.makeText(getContext(), "no se podra completar ña accion por fallos en los datos de recursos", Toast.LENGTH_LONG).show();
            dismiss();
        }else {
            traer_imagen();
        }

        builder.setView(view_).setTitle("Imagen")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });


        return  builder.create();
    }
    private void traer_imagen(){

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document(id_animal);
        dos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    modelo_animal = task.getResult().toObject(Animal_Model.class);
                    nombre_animal = modelo_animal.getAnml_nombre();
                    ruta_foto = modelo_animal.getAnml_imagen();

                }
                tv_nombre.setText(nombre_animal);
                //datos foto
                if (ruta_foto != null){
                    Glide
                            .with(view_.getContext())
                            .load(ruta_foto)
                            .into(img_foto);
                }


            }
        });
    }

    private   void  iniciar_activity(){
        preferences =  view_.getContext().getSharedPreferences("preferences", MODE_PRIVATE);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        img_foto = view_.findViewById(R.id.img_view_foto);
        tv_nombre = view_.findViewById(R.id.tv_view_imagen);

    }


}
