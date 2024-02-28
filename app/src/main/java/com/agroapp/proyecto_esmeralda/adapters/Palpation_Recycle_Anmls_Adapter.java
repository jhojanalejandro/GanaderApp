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
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Detalle_palpacion;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Palpation_Recycle_Anmls_Adapter extends RecyclerView.Adapter<Palpation_Recycle_Anmls_Adapter.Part_Anmls_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<Palpacion_Model> animalArrayList_part;


    public Palpation_Recycle_Anmls_Adapter(Context contextt, int layout_resur, ArrayList<Palpacion_Model> animalArrayList_part) {
        this.contextt = contextt;
        this.layout_resur = layout_resur;
        this.animalArrayList_part = animalArrayList_part;
    }

    @NonNull
    @Override
    public Part_Anmls_viewhollder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(contextt).inflate(layout_resur, viewGroup, false);
        return new Part_Anmls_viewhollder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Part_Anmls_viewhollder holder, int position) {
        Palpacion_Model animal = animalArrayList_part.get(position);
        holder.tv_date.setText(animal.getFecha_registro());
        holder.tv_type.setText("palpacion");
        holder.id_data = animal.getTipo_registro();
        holder.id_anmal = animal.getTratamiento();

    }

    @Override
    public int getItemCount() {
        if (animalArrayList_part.size() > 0) {
            animalArrayList_part.size();
        }
        return animalArrayList_part.size();
    }

    public class Part_Anmls_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_date, tv_type;
        String id_data = "",id_anmal;
        SharedPreferences preferences;
        FragmentManager manager;


        public Part_Anmls_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.tv_item_data_type_animal_date);
            tv_type = itemView.findViewById(R.id.tv_data_type_animal);

        }


        @Override
        public void onClick(View v) {
            preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
            Intent intent = new Intent(contextt, Detalle_palpacion.class);
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("id_palpacion", id_data);
            editors.putString("id_animal", id_anmal);
            editors.apply();
            contextt.startActivity(intent);

        }

    }
}
