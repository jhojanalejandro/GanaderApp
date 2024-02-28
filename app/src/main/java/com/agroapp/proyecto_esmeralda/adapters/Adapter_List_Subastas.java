package com.agroapp.proyecto_esmeralda.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Mostrar_imagen;
import com.agroapp.proyecto_esmeralda.views.manejo_subastas_view.Perfil_Subastar_View;
import com.agroapp.proyecto_esmeralda.modelos.Subasta_Model;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_List_Subastas extends RecyclerView.Adapter<Adapter_List_Subastas.Subastas_viewhollder> {
    private ArrayList<Subasta_Model> list_subasta;
    private final Context contextt;
    private int layout_sub_resur;
    private Subasta_Model ficha_subasta;
    LayoutInflater inflater;
    TextView tv_item_subasta_fecha;


    public Adapter_List_Subastas(Context contextt, int layout_sub_resur, ArrayList<Subasta_Model> subastaArrayList_subasta) {
        this.contextt = contextt;
        this.layout_sub_resur = layout_sub_resur;
        this.list_subasta = subastaArrayList_subasta;
    }

    @NonNull
    @Override
    public Subastas_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_sub_resur, viewGroup, false);
        return new Subastas_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Subastas_viewhollder holder, int position) {
        Subasta_Model subasta = list_subasta.get(position);
        final String url = list_subasta.get(position).getImagenes_desmostracion().get(0);
        String precio = String.valueOf(subasta.getPrecio_total());
        holder.id_data = subasta.getId_venta();
        holder.tv_sub_fecha_inicio.setText(subasta.getFecha_registro());
        holder.tv_sub_precio.setText(precio);


        //datos foto
        Glide
                .with(contextt)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mfoto_subasta);

        holder.mfoto_subasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) contextt).getSupportFragmentManager();

                Mostrar_imagen mostrar_imagen = new Mostrar_imagen();
                mostrar_imagen.show(manager, "registro insumo subastaes");
            }
        });
        holder.video_subasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if (list_subasta.size() > 0) {
            list_subasta.size();
        }
        return list_subasta.size();
    }

    public class Subastas_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_sub_cant_animales, tv_sub_ubicacion,tv_sub_ubicacion_vereda,tv_sub_fecha_inicio,tv_sub_precio,tv_sub_descripcion;
        ImageView mfoto_subasta;
        VideoView video_subasta;

        String id_data = "";
        SharedPreferences preferences;
        FragmentManager manager;


        public Subastas_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_sub_descripcion = itemView.findViewById(R.id.tv_sub_list_descripcion);
            tv_sub_fecha_inicio = itemView.findViewById(R.id.edt_sub_fecha_inicio);
            tv_sub_cant_animales = itemView.findViewById(R.id.tv_sub_list_cant_anmls);
            tv_sub_ubicacion = itemView.findViewById(R.id.tv_sub_list_ubicacion_departamento);
            tv_sub_precio = itemView.findViewById(R.id.tv_sub_precio_animales);
            tv_sub_ubicacion_vereda = itemView.findViewById(R.id.tv_sub_list_ubicacion_vereda);
            video_subasta = itemView.findViewById(R.id.video_sub_list);
            mfoto_subasta = itemView.findViewById(R.id.img_sub_list_anml1);

        }


        @Override
        public void onClick(View v) {

            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
          if (id_data.equals("opciones")) {
                manager = ((AppCompatActivity) contextt).getSupportFragmentManager();
                Intent subasta = new Intent(contextt, Perfil_Subastar_View.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id_subasta", id_data);
              editor.putString("id_subasta", id_data);
                editor.putString("nombre_subasta", tv_sub_precio.getText().toString());
                editor.commit();
                contextt.startActivity(subasta);


          }
        }
    }

    public void filtrar(ArrayList<Subasta_Model> filtro_subasta) {
        this.list_subasta = filtro_subasta;
        notifyDataSetChanged();
    }


}
