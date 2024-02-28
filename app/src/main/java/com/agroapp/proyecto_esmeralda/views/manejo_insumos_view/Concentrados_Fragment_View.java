package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class Concentrados_Fragment_View extends Fragment {
    Button registrar_v;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Insumo_Finca_Model insumo_model;
    int dia ,mes,ano;
    private String farm_name,user_name, id_propietario;

    SharedPreferences preferences;

    LinearLayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    Manejo_Insumos_Interface manejo_insumos_interface;

    private OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.concentrado_fragment_view, container, false);

        insumo_model = new Insumo_Finca_Model();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        if (farm_name != null & user_name != null){
            DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
            DocumentReference fincas_ref = registros_ref_array[0];
            manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
            recyclerView = view.findViewById(R.id.recycle_view_list_concentrados);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            manejo_insumos_interface.mostrar_concentrados("herramienta", recyclerView, layoutManager);
        }else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos fragment", Toast.LENGTH_LONG).show();

        }
        return  view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    private String validar_finca_n(){
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        farm_name = preferences.getString("finca",null);
        if ( farm_name!=null){
            return farm_name;
        }else {
            return  null;
        }
    }
    private String empdo_n(){
        user_name = preferences.getString("empleado",null);
        if ( user_name!=null){
            return user_name;
        }else {
            return  null;
        }
    }
    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }



}
