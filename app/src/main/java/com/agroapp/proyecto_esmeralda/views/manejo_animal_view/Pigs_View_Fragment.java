package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Animals_Recycle_Adapter;

import com.google.firebase.firestore.FirebaseFirestore;

public class Pigs_View_Fragment extends Fragment {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    String nombre_animal, nombre_potrero;
    EditText buscar;
    private String n_finca, n_empleado;
    Animals_Recycle_Adapter animal_viewhollder;
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    
    private String porcino= "porcino";
    View view;

    private OnFragmentInteractionListener mListener;

    public Pigs_View_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_first, container, false);
        empdo_n();
        validar_finca_n();

        return view;

        
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        
    }

    private String validar_finca_n() {
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        n_finca = preferences.getString("finca", null);

        if (n_finca != null) {
            return n_finca;
        } else {
            return null;
        }
    }


    private String empdo_n() {
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        n_empleado = preferences.getString("empleado", null);


        if (n_empleado != null) {
            return n_empleado;
        } else {
            return null;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    
}