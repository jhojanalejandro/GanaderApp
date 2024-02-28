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
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Detalle_Secado;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Detalle_enfermedad;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Detalle_calor;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Detalle_palpacion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Gasto_Insumos_Recycle_Anmls_Adapter extends RecyclerView.Adapter<Gasto_Insumos_Recycle_Anmls_Adapter.Gasto_Insumos_Anmls_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<Gastos_Insumos> animalArrayList_part;


    public Gasto_Insumos_Recycle_Anmls_Adapter(Context contextt, int layout_resur, ArrayList<Gastos_Insumos> animalArrayList_part) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.animalArrayList_part = animalArrayList_part;
    }

    @NonNull
    @Override
    public Gasto_Insumos_Anmls_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Gasto_Insumos_Anmls_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Gasto_Insumos_Anmls_viewhollder holder, int position) {
        Gastos_Insumos animal = animalArrayList_part.get(position);
        holder.tv_date.setText(animal.getFecha_registro());
        holder.tv_type.setText(animal.getTipo_registro());
        holder.id_animal = animal.getTratamiento();
        holder.id_data = animal.getObservaciones();

    }

    @Override
    public int getItemCount() {
        if (animalArrayList_part.size() > 0) {
            animalArrayList_part.size();
        }
        return animalArrayList_part.size();
    }

    public class Gasto_Insumos_Anmls_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_date, tv_type;
        String id_data = "",id_animal,tipo;
        SharedPreferences preferences;

        FragmentManager manager;


        public Gasto_Insumos_Anmls_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.tv_item_data_type_animal_date);
            tv_type = itemView.findViewById(R.id.tv_data_type_animal);

        }
        
        @Override
        public void onClick(View v) {
            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            tipo = tv_type.getText().toString();
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("id_registro", id_data);
            editors.putString("id_animal", id_animal);
            editors.putString("tipo_registro", tipo);

            editors.apply();

            switch (tipo){
                case "calor":
                case "parto":
                    Intent intent = new Intent(contextt, Detalle_calor.class);
                    contextt.startActivity(intent);
                    break;
                case "palpacion":
                    Intent palpacion = new Intent(contextt, Detalle_palpacion.class);
                    contextt.startActivity(palpacion);
                    break;
                case "vacunacion":
                case "enfermedad":
                    Intent others = new Intent(contextt, Detalle_enfermedad.class);
                    contextt.startActivity(others);
                    break;
                case "secado":
                    Intent dry = new Intent(contextt, Detalle_Secado.class);
                    contextt.startActivity(dry);
                    break;
            }




        }

    }
}
