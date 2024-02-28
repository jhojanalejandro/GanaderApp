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
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Mostrar_imagen;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Opciones_animal_dialog;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_macho.Perfil_animal_macho;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Perfil_Animal_view;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Lista_General_Animals_Recycle_Adapter extends RecyclerView.Adapter<Lista_General_Animals_Recycle_Adapter.Animal_viewhollder> {
    private Context contextt;
    private int layout_resur;

    Opciones_animal_dialog opciones_animal_dialog;
    ArrayList<Animal_Model> animalArrayList_animal;

    public Lista_General_Animals_Recycle_Adapter(Context contextt, int layout_resur, ArrayList<Animal_Model> animalArrayList_animal) {
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
        String estado = animal.getAnml_salida();
        if (estado.equals("vacio")) {
            holder.estado.setText("activo");
        } else {
            holder.estado.setText(estado);
        }

        holder.url = animal.getAnml_imagen();
        holder.id_data = animal.getAnml_lote();
        holder.numero.setText(animal.getAnml_chapeta());
        holder.fecha_ingreso.setText(animal.getAnml_fecha_ingreso());
        holder.fecha_salida.setText(animal.getAnml_fecha_salida());
        holder.id_tipo = animal.getAnml_tipo();
        holder.genero = animal.getAnml_genero();
        holder.preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);

        if (!holder.url.equals("vacio")) {
            Glide
                    .with(contextt)
                    .load(holder.url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mfoto_animal);

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


    }

    @Override
    public int getItemCount() {
        if (animalArrayList_animal.size() > 0) {
            animalArrayList_animal.size();
        }
        return animalArrayList_animal.size();
    }

    public class Animal_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombre, numero, estado, fecha_ingreso, fecha_salida;
        View view;
        ImageView mfoto_animal;
        String id_data = "", genero, id_tipo, url;
        SharedPreferences preferences;
        FragmentManager manager;


        public Animal_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre = itemView.findViewById(R.id.tv_nombre_animal_list_general);
            numero = itemView.findViewById(R.id.tv_numero_animal_list_general);
            fecha_salida = itemView.findViewById(R.id.tv_fecha_salida_animal);
            fecha_ingreso = itemView.findViewById(R.id.tv_fecha_ingreso);
            estado = itemView.findViewById(R.id.tv_estado_animal_list_general);
            mfoto_animal = itemView.findViewById(R.id.lista_general_imagen_animal_view_holder);
        }

        @Override
        public void onClick(View v) {

            Share_References_interface share_references_interface = new Share_References_presenter(contextt);


            manager = ((AppCompatActivity) contextt).getSupportFragmentManager();
            String paddock = share_references_interface.tipo(preferences);
            if (paddock != null) {
                id_tipo = paddock;
            }
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
