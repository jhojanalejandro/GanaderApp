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
import com.agroapp.proyecto_esmeralda.views.manejo_usuarios_view.Detalle_Usuario;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Supplies_Droga_Recycle_Adapter_View extends RecyclerView.Adapter<Supplies_Droga_Recycle_Adapter_View.Part_Supplies_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<Insumo_Finca_Model> Supplies_lArrayList;

    public Supplies_Droga_Recycle_Adapter_View(Context contextt, int layout_resur, ArrayList<Insumo_Finca_Model> Supplies_lArrayList) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.Supplies_lArrayList = Supplies_lArrayList;
    }

    @NonNull
    @Override
    public Part_Supplies_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Part_Supplies_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Part_Supplies_viewhollder holder, int position) {
        Insumo_Finca_Model insumos = Supplies_lArrayList.get(position);
        holder.tv_nombre.setText( insumos.getIns_finca_nombre());
        holder.cant_droga = String.valueOf(insumos.getIns_finca_restante());
        holder.tv_cant_insumo.setText(holder.cant_droga);
        String precio = String.valueOf(insumos.getIns_finca_precio());
        holder.tv_precio.setText(precio);


    }

    @Override
    public int getItemCount() {
        if (Supplies_lArrayList.size() > 0) {
            Supplies_lArrayList.size();
        }
        return Supplies_lArrayList.size();
    }

    public class Part_Supplies_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_nombre, tv_cant_insumo, tv_precio;
        String id_data = "", cant_droga;
        SharedPreferences preferences;
        FragmentManager manager;


        public Part_Supplies_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_nombre = itemView.findViewById(R.id.tv_insumos_n_droga);
            tv_cant_insumo = itemView.findViewById(R.id.tv_cant_droga_item_ins);
            tv_precio = itemView.findViewById(R.id.tv_cant_precio_droga);
        }


        @Override
        public void onClick(View v) {
            id_data = tv_nombre.getText().toString();
            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            Intent intent = new Intent(contextt, Detalle_Usuario.class);
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("nombre_insumo", id_data);
            editors.apply();
            contextt.startActivity(intent);


        }

    }
}
