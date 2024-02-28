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
import com.agroapp.proyecto_esmeralda.modelos.User_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Mostrar_imagen;
import com.agroapp.proyecto_esmeralda.views.manejo_usuarios_view.Detalle_Usuario;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class User_Recycle_View extends RecyclerView.Adapter<User_Recycle_View.User_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<User_Model> user_Arraylist;


    public User_Recycle_View(Context contextt, int layout_resur, ArrayList<User_Model> user_Arraylist) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.user_Arraylist = user_Arraylist;
    }

    @NonNull
    @Override
    public User_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new User_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final User_viewhollder holder, int position) {
        User_Model user = user_Arraylist.get(position);
        final String url = user_Arraylist.get(position).getUser_img();
        holder.user_name = user.getUser_nombre();
        holder.tv_nombre.setText(holder.user_name);
        holder.tv_tipo.setText(user.getUser_tipo());
        String telefono = String.valueOf(user.getUser_telefono());
        holder.tv_telelfono.setText(telefono);
        String cedula = String.valueOf(user.getUser_cedula());
        holder.tv_cedula.setText(cedula);
        holder.id_tipo = user.getUser_tipo();
        //datos foto
        Glide
                .with(contextt)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mfoto_animal);

        holder.mfoto_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) contextt).getSupportFragmentManager();

                Mostrar_imagen mostrar_imagen = new Mostrar_imagen();
                mostrar_imagen.show(manager, "registro insumo animales");
            }
        });

    }

    @Override
    public int getItemCount() {
        if (user_Arraylist.size() > 0) {
            user_Arraylist.size();
        }
        return user_Arraylist.size();
    }

    public class User_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_nombre, tv_cedula,tv_tipo,tv_telelfono;
        ImageView mfoto_animal;
        String id_tipo, user_name;
        SharedPreferences preferences;


        public User_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_nombre = itemView.findViewById(R.id.tv_item_user_name);
            tv_telelfono = itemView.findViewById(R.id.tv_item_user_telefono);
            tv_tipo = itemView.findViewById(R.id.tv_item_user_tipo_usuario);
            tv_cedula = itemView.findViewById(R.id.tv_item_user_cedula);
            mfoto_animal = itemView.findViewById(R.id.imagen_user_view_holder);

        }

        @Override
        public void onClick(View v) {
            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            Intent perfil_macho = new Intent(contextt, Detalle_Usuario.class);
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("id_usuario", id_tipo);
            editors.apply();
            contextt.startActivity(perfil_macho);
        }
    }

}
