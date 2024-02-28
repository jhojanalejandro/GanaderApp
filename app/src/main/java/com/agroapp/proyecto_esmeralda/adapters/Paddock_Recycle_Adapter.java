package com.agroapp.proyecto_esmeralda.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Potreros_model;
import com.agroapp.proyecto_esmeralda.views.menejo_potreros.Detalle_Potero;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Paddock_Recycle_Adapter extends RecyclerView.Adapter<Paddock_Recycle_Adapter.Paddock_Anmls_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<Potreros_model> paddockArrayList;


    public Paddock_Recycle_Adapter(Context contextt, int layout_resur, ArrayList<Potreros_model> paddockArrayList_part) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.paddockArrayList = paddockArrayList_part;
    }

    @NonNull
    @Override
    public Paddock_Anmls_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Paddock_Anmls_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Paddock_Anmls_viewhollder holder, int position) {
        Potreros_model paddock = paddockArrayList.get(position);
        holder.tv_estado.setText(paddock.getPto_estado());
        holder.tv_nombre.setText(paddock.getPto_nombre());
        String extension = String.valueOf(paddock.getPto_extension());
        String cant_animales = String.valueOf(paddock.getPto_cant_anml_pto());
        holder.tv_extnsion.setText(extension);
        holder.tv_tipo_pasto.setText(paddock.getPto_tipo_pasto());
        holder.tv_cant_animales.setText(cant_animales);
    }

    @Override
    public int getItemCount() {
        if (paddockArrayList.size() > 0) {
            paddockArrayList.size();
        }
        return paddockArrayList.size();
    }

    public class Paddock_Anmls_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_cant_animales, tv_nombre,tv_estado, tv_extnsion, tv_tipo_pasto;

        SharedPreferences preferences;
        FragmentManager manager;


        public Paddock_Anmls_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_cant_animales = itemView.findViewById(R.id.tv_item_pto_cant_anml);
            tv_estado = itemView.findViewById(R.id.tv_item_pto_estado);
            tv_nombre = itemView.findViewById(R.id.tv_item_pto_nombre);
            tv_extnsion = itemView.findViewById(R.id.tv_item_pto_extension);
            tv_tipo_pasto = itemView.findViewById(R.id.tv_pto_tipo_pasto);

        }


        @Override
        public void onClick(View v) {

            String paddock_name= tv_nombre.getText().toString().trim();
            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            Intent detalle_potrero = new Intent(contextt, Detalle_Potero.class);
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("paddock_name", paddock_name);
            editors.apply();
            contextt.startActivity(detalle_potrero);

        }

    }

    public void filtrar(ArrayList<Potreros_model> filtro_animal) {
        this.paddockArrayList = filtro_animal;
        notifyDataSetChanged();
    }
}
