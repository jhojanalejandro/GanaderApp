package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.DocumentReference;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Herramientas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences preferences;
    RecyclerView recyclerView;
    Manejo_Insumos_Interface manejo_insumos_interface;
    LinearLayoutManager layoutManager;
    Share_References_presenter share_preferences_presenter;
    private String farm_name, user_name, id_propietario;
    View view;
    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;


    private OnFragmentInteractionListener mListener;

    public Herramientas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_herrramientas, container, false);



        preferences = getContext().getSharedPreferences("preferences", MODE_PRIVATE);
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(),fincas_ref);

        if (farm_name != null & user_name != null){
            recyclerView = view.findViewById(R.id.recycle_view_list_tools);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            layoutManager = new LinearLayoutManager(getContext());
            manejo_insumos_interface.mostrar_insumos("herramienta", recyclerView, layoutManager);
        }else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos fragment", Toast.LENGTH_LONG).show();

        }

        return  view;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
