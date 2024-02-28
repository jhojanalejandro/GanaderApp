package com.agroapp.proyecto_esmeralda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;

import java.util.ArrayList;

public class Adapter_movt_pto extends BaseAdapter {
    private ArrayList<Animal_Model> list_pto;
    private Animal_Model mvt_pto_model;
    private final Context context;

    LayoutInflater inflater;


    public Adapter_movt_pto(ArrayList<Animal_Model> list_pto, Context context) {
        this.list_pto = list_pto;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list_pto.size();
    }

    @Override
    public Object getItem(int i) {
        return list_pto.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemview = view;
        if (view == null) {
             inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemview = inflater.inflate(R.layout.item_movimiento_potrero, viewGroup, false);
        }
        TextView tv_item_n = itemview.findViewById(R.id.tv_item_mvt_potreros_n);

        mvt_pto_model = list_pto.get(i);
        tv_item_n.setText(mvt_pto_model.getAnml_nombre());
        return itemview;
    }


}
