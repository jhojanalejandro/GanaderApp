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
import com.agroapp.proyecto_esmeralda.activities.Detalle_prod_individual;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Produccion_Leche_Recycle_Anmls_Adapter extends RecyclerView.Adapter<Produccion_Leche_Recycle_Anmls_Adapter.Leche_Anmls_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<Medida_Leche_Model> lecheArrayList_part;


    public Produccion_Leche_Recycle_Anmls_Adapter(Context contextt, int layout_resur, ArrayList<Medida_Leche_Model> lecheArrayList_part) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.lecheArrayList_part = lecheArrayList_part;
    }

    @NonNull
    @Override
    public Leche_Anmls_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Leche_Anmls_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Leche_Anmls_viewhollder holder, int position) {
        Medida_Leche_Model leche = lecheArrayList_part.get(position);
        holder.tv_date.setText(leche.getMedida_anml_fecha());
        holder.id_data = leche.getMedida_tipo();

    }

    @Override
    public int getItemCount() {
        if (lecheArrayList_part.size() > 0) {
            lecheArrayList_part.size();
        }
        return lecheArrayList_part.size();
    }

    public class Leche_Anmls_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_date;
        String id_data = "";
        SharedPreferences preferences;
        FragmentManager manager;


        public Leche_Anmls_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.tv_item_produccion_lehera_fecha);

        }

        @Override
        public void onClick(View v) {

            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);

            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            Intent intent = new Intent(contextt, Detalle_prod_individual.class);
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("id_produccion", id_data);
            editors.putString("id_leche", "medida");
            editors.apply();
            contextt.startActivity(intent);

        }

    }
}
