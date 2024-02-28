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
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Mostrar_imagen;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Opciones_animal_dialog;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_macho.Perfil_animal_macho;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Perfil_Animal_view;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Recycle_Adapter_Produccion_animal extends RecyclerView.Adapter<Recycle_Adapter_Produccion_animal.Animal_viewhollder> {
    private Context contextt;
    private int layout_resur;

    Opciones_animal_dialog opciones_animal_dialog;
    ArrayList<Animal_Model> animalArrayList_animal;


    public Recycle_Adapter_Produccion_animal(Context contextt, int layout_resur, ArrayList<Animal_Model> animalArrayList_animal) {
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

        holder.nombre.setText(animal.getAnml_nombre());
        holder.numero.setText(animal.getAnml_chapeta());
        holder.id_data = animal.getAnml_salida();
        holder.id_tipo = animal.getAnml_tipo();
        holder.genero = animal.getAnml_genero();
        String url = animal.getAnml_imagen();


        if (holder.id_tipo.equals("medida")) {
            holder.medida = String.valueOf(animal.getAnml_prod_litros());
            holder.cantidad_produccion.setText(holder.medida);
        } else if (holder.id_tipo.equals("pesaje")) {
            holder.pesaje = String.valueOf(animal.getAnml_prod_kilos_ganados());
            holder.cantidad_produccion.setText(holder.pesaje);
            holder.img_prod_animal.setImageResource(R.drawable.ic_kilo);
        }

        if (!url.equals("vacio")) {
            Glide
                    .with(contextt)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.img_foto);


            holder.img_foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((AppCompatActivity) contextt).getSupportFragmentManager();
                    Mostrar_imagen mostrar_imagen = new Mostrar_imagen();
                    mostrar_imagen.show(manager, "registro insumo animales");
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (animalArrayList_animal.size() > 0) {
            animalArrayList_animal.size();
        }
        return animalArrayList_animal.size();
    }

    public class Animal_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombre, numero, cantidad_produccion;
        View view;

        ImageView img_foto, img_prod_animal;
        String id_data = "", medida, pesaje, genero, id_tipo;
        SharedPreferences preferences;
        FragmentManager manager;


        public Animal_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre = itemView.findViewById(R.id.tv_item_prod_alta_nombre);
            numero = itemView.findViewById(R.id.tv_item_prod_alta_chapeta);
            cantidad_produccion = itemView.findViewById(R.id.tv_cant_prod_alta_leche);
            img_foto = itemView.findViewById(R.id.img_produccion_animal);
            img_prod_animal = itemView.findViewById(R.id.img_prod_anml);
        }

        @Override
        public void onClick(View v) {

            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            manager = ((AppCompatActivity) contextt).getSupportFragmentManager();

            if (genero.equals("hembra")) {
                Intent intent = new Intent(contextt, Perfil_Animal_view.class);
                SharedPreferences.Editor editors = preferences.edit();
                editors.putString("id_animal", id_data);
                editors.apply();
                contextt.startActivity(intent);

            } else {
                Intent perfil_macho = new Intent(contextt, Perfil_animal_macho.class);
                SharedPreferences.Editor editors = preferences.edit();
                editors.putString("nombre_animal", nombre.getText().toString());
                editors.putString("id_animal", id_data);
                editors.apply();
                contextt.startActivity(perfil_macho);
            }

        }
    }

    public void filtrar(ArrayList<Animal_Model> filtro_animal) {
        this.animalArrayList_animal = filtro_animal;
        notifyDataSetChanged();
    }
}
