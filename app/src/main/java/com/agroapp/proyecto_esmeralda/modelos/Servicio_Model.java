package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Servicio_Model extends Gastos_Insumos implements Serializable {

    private String calor_n_toro;

    public Servicio_Model() {
    }

    public String getCalor_n_toro() {
        return calor_n_toro;
    }

    public void setCalor_n_toro(String calor_n_toro) {
        this.calor_n_toro = calor_n_toro;
    }

}
