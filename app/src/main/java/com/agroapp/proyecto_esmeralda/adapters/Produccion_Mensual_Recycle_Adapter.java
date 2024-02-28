package com.agroapp.proyecto_esmeralda.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Opciones_animal_dialog;
import com.agroapp.proyecto_esmeralda.views.manejo_produccion.Detalle_Estadisticas;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Produccion_Mensual_Recycle_Adapter extends RecyclerView.Adapter<Produccion_Mensual_Recycle_Adapter.Produccion_viewhollder> {
    private Context contextt;
    private int layout_resur;

    Opciones_animal_dialog opciones_prodcuccion_dialog;
    ArrayList<Produccion_Model> produccionArrayList_animal;

    public Produccion_Mensual_Recycle_Adapter(Context contextt, int layout_resur, ArrayList<Produccion_Model> produccionArrayList_animal) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.produccionArrayList_animal = produccionArrayList_animal;
    }

    @NonNull
    @Override
    public Produccion_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Produccion_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Produccion_viewhollder holder, int position) {
        Produccion_Model produccion = produccionArrayList_animal.get(position);

        holder.id_data = produccion.getProd_obs();
        holder.tv_fecha_produccion.setText(produccion.getProd_fecha());
        holder.preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);

    }

    @Override
    public int getItemCount() {

        if (produccionArrayList_animal.size() > 0) {
            produccionArrayList_animal.size();
        }
        return produccionArrayList_animal.size();
    }

    public class Produccion_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_fecha_produccion;

        String id_data = "";
        SharedPreferences preferences;


        public Produccion_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_fecha_produccion = itemView.findViewById(R.id.tv_item_produccion_fecha);


        }

        @Override
        public void onClick(View v) {

            Intent detalle = new Intent(contextt, Detalle_Estadisticas.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id_produccion", id_data);
            editor.apply();
            contextt.startActivity(detalle);
        }
    }

    public void filtrar(ArrayList<Produccion_Model> filtro_animal) {
        this.produccionArrayList_animal = filtro_animal;
        notifyDataSetChanged();
    }
}
