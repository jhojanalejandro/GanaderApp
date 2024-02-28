package com.agroapp.proyecto_esmeralda.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Subastas_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Subastas_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Mostrar_imagen;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Perfil_Animal_view;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_macho.Perfil_animal_macho;
import com.agroapp.proyecto_esmeralda.views.menejo_potreros.Movimiento_pto_dialog;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Opciones_animal_dialog;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Animals_Recycle_Adapter extends RecyclerView.Adapter<Animals_Recycle_Adapter.Animal_viewhollder> {
    private Context contextt;
    private int layout_resur;

    Opciones_animal_dialog opciones_animal_dialog;
    ArrayList<Animal_Model> animalArrayList_animal;

    public Animals_Recycle_Adapter(Context contextt, int layout_resur, ArrayList<Animal_Model> animalArrayList_animal) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.animalArrayList_animal = animalArrayList_animal;
    }

    @NonNull
    @Override
    public Animal_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Animal_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Animal_viewhollder holder, int position) {
        Animal_Model animal = animalArrayList_animal.get(position);
        final String url = animal.getAnml_imagen();
        holder.nombre_animal.setText(animal.getAnml_nombre().toUpperCase());
        holder.id_data = animal.getAnml_salida();
        holder.numero_id.setText(animal.getAnml_chapeta());
        holder.id_tipo = animal.getAnml_tipo();
        holder.imagen = animal.getAnml_imagen();
        holder.genero.setText(animal.getAnml_genero());
        holder.raza_animal.setText(animal.getAnml_raza());
        holder.preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);

        if (!holder.imagen.equals("vacio")) {
            Glide
                    .with(contextt)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mfoto_animal);
        }


        holder.mfoto_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) contextt).getSupportFragmentManager();
                Mostrar_imagen mostrar_imagen = new Mostrar_imagen();
                SharedPreferences.Editor editor = holder.preferences.edit();
                editor.putString("id_animal", holder.id_data);
                editor.apply();
                mostrar_imagen.show(manager, "registro insumo animales");
            }
        });

    }

    @Override
    public int getItemCount() {

        if (animalArrayList_animal.size() > 0) {
            animalArrayList_animal.size();
        }
        return animalArrayList_animal.size();
    }

    public class Animal_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombre_animal, numero_id, raza_animal, genero;

        ImageView mfoto_animal;
        String id_data = "", id_tipo, imagen;
        SharedPreferences preferences;
        FragmentManager manager;


        public Animal_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre_animal = itemView.findViewById(R.id.tv_nombre_animal_view_holder);
            numero_id = itemView.findViewById(R.id.tv_numero_animal_view_holder);
            genero = itemView.findViewById(R.id.tv_item_genero_animal);
            raza_animal = itemView.findViewById(R.id.tv_item_raza_animal);
            mfoto_animal = itemView.findViewById(R.id.imagen_animal_view_holder);

        }

        @Override
        public void onClick(View v) {

            Share_References_interface share_references_interface = new Share_References_presenter(contextt);

            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("id_animal", id_data);
            editors.putString("nombre_animal", nombre_animal.getText().toString());
            editors.putString("nombre_potrero", id_tipo);
            editors.apply();

            manager = ((AppCompatActivity) contextt).getSupportFragmentManager();

            switch (id_tipo) {

                case "bovino":
                    if (genero.getText().toString().equals("hembra")) {
                        Intent intent = new Intent(contextt, Perfil_Animal_view.class);
                        contextt.startActivity(intent);
                    } else if (genero.getText().toString().equals("macho")) {
                        Intent perfil_macho = new Intent(contextt, Perfil_animal_macho.class);
                        contextt.startActivity(perfil_macho);
                    }
                    break;
                case "opciones":
                    opciones_animal_dialog = new Opciones_animal_dialog();
                    opciones_animal_dialog.show(manager, "opciones de animal ");
                    break;
                case "subasta":
                    Snackbar deseas_agregar_este_registro = Snackbar.make(v, "DESEAS AGREGAR ESTE ANIMAL A LA LISTA DE SUBASTA", Snackbar.LENGTH_INDEFINITE);
                    deseas_agregar_este_registro.setAction("AGREGAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Share_References_interface share_references_interface = new Share_References_presenter(contextt);
                            String id_propietario = share_references_interface.id_propietario(preferences);
                            String farm_name = share_references_interface.farm_name(preferences);
                            String id_subasta = share_references_interface.farm_name(preferences);
                            Manejo_Subastas_Interface manejo_subastas_interface = new Manejo_Subastas_Presenter(contextt);
                            manejo_subastas_interface.agregar_animales_asubastar(id_propietario, id_data, farm_name, id_subasta);
                        }
                    });
                    deseas_agregar_este_registro.show();
                    break;
                case "potreros":
                    Movimiento_pto_dialog movimientos_potrero = new Movimiento_pto_dialog();
                    SharedPreferences.Editor ingreso_lote = preferences.edit();
                    ingreso_lote.putString("id_animal", id_data);
                    ingreso_lote.putString("id_tipo_ingreso", "individual");
                    ingreso_lote.apply();
                    movimientos_potrero.show(manager, "Ingreso Lote Potrero ");
                    break;
            }
        }
    }

    public void filtrar(ArrayList<Animal_Model> filtro_animal) {
        this.animalArrayList_animal = filtro_animal;
        notifyDataSetChanged();
    }
}
