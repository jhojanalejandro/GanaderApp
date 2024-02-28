package com.agroapp.proyecto_esmeralda.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Manejo_Insumos_View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Supplies_concentrados_Recycle_Adapter_View extends RecyclerView.Adapter<Supplies_concentrados_Recycle_Adapter_View.Part_Supplies_viewhollder> {
    private Context contextt;
    private int layout_resur;
    ArrayList<Insumo_Finca_Model> Supplies_lArrayList;

    public Supplies_concentrados_Recycle_Adapter_View(Context contextt, int layout_resur, ArrayList<Insumo_Finca_Model> Supplies_lArrayList) {
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

        holder.cant_droga = String.valueOf(insumos.getIns_finca_restante());
        holder.tv_cant_insumo.setText(holder.cant_droga);
        holder.tv_tipo.setText(insumos.getIns_finca_tipo().toUpperCase());
        holder.nombre = insumos.getIns_finca_nombre();
        holder.tv_nombre.setText(holder.nombre);
        String precio = String.valueOf(insumos.getIns_finca_precio());
        holder.tv_precio.setText(precio);
        if (holder.tv_tipo.getText().toString().equals("HERRAMIENTA")) {
            holder.img_insumo.setImageResource(R.drawable.ic_herramientas_de_jardineria);
        } else if (holder.tv_tipo.getText().toString().equals("DROGA")) {
            holder.img_insumo.setImageResource(R.drawable.ic_agua_roja);
        }

    }

    @Override
    public int getItemCount() {
        if (Supplies_lArrayList.size() > 0) {
            Supplies_lArrayList.size();
        }
        return Supplies_lArrayList.size();
    }

    public class Part_Supplies_viewhollder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_nombre, tv_cant_insumo, tv_precio, tv_tipo;
        ImageView img_insumo;
        String cant_droga;
        String nombre;

        FragmentManager manager;


        public Part_Supplies_viewhollder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_nombre = itemView.findViewById(R.id.tv_insumos_n_concentrados);
            tv_cant_insumo = itemView.findViewById(R.id.tv_cant_concentrado_item_ins);
            tv_tipo = itemView.findViewById(R.id.tv_tipo_insumo);
            img_insumo = itemView.findViewById(R.id.img_mostrar_insumos);
            tv_precio = itemView.findViewById(R.id.tv_cant_precio_concentrado);
        }


        @Override
        public void onClick(View v) {


            Snackbar deseas_agregar_este_registro = Snackbar.make(v, "deseas agregar este registro", Snackbar.LENGTH_INDEFINITE);
            deseas_agregar_este_registro.setAction("ELIMINAR REGISTRO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deseas_agregar_este_registro.show();
                    Share_References_interface share_references_interface = new Share_References_presenter(contextt);
                    SharedPreferences preferences = contextt.getSharedPreferences("preferences", MODE_PRIVATE);
                    String id_propietario = share_references_interface.id_propietario(preferences);
                    String id_animal = share_references_interface.id_animal(preferences);
                    String n_finca = share_references_interface.farm_name(preferences);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
                    DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
                    final DocumentReference coreff = docRef.collection("animales").document(id_animal);
                    final DocumentReference regis_ref = coreff.collection("registro_animal").document(nombre);

                    regis_ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(contextt, " eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                Intent manejo = new Intent(contextt, Manejo_Insumos_View.class);
                                contextt.startActivity(manejo);
                            }
                        }
                    });
                }

            });
            deseas_agregar_este_registro.show();

        }

    }
}
