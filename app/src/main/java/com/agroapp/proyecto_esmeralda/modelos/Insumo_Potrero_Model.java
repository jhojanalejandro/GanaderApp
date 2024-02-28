package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Insumo_Potrero_Model implements Serializable {

    private String ins_pto_nombre;
    private String ins_pto_tipo;
    private int ins_pto_cant;
    private String ins_pto_n_e;
    private String ins_pto_observaciones;

    public Insumo_Potrero_Model() {
    }

    public Insumo_Potrero_Model(String ins_pto_nombre, String ins_pto_tipo, int ins_pto_cant, String ins_pto_n_e, String ins_pto_observaciones) {
        this.ins_pto_nombre = ins_pto_nombre;
        this.ins_pto_tipo = ins_pto_tipo;
        this.ins_pto_cant = ins_pto_cant;
        this.ins_pto_n_e = ins_pto_n_e;
        this.ins_pto_observaciones = ins_pto_observaciones;
    }

    public String getIns_pto_nombre() {
        return ins_pto_nombre;
    }

    public void setIns_pto_nombre(String ins_pto_nombre) {
        this.ins_pto_nombre = ins_pto_nombre;
    }

    public String getIns_pto_tipo() {
        return ins_pto_tipo;
    }

    public void setIns_pto_tipo(String ins_pto_tipo) {
        this.ins_pto_tipo = ins_pto_tipo;
    }

    public int getIns_pto_cant() {
        return ins_pto_cant;
    }

    public void setIns_pto_cant(int ins_pto_cant) {
        this.ins_pto_cant = ins_pto_cant;
    }

    public String getIns_pto_n_e() {
        return ins_pto_n_e;
    }

    public void setIns_pto_n_e(String ins_pto_n_e) {
        this.ins_pto_n_e = ins_pto_n_e;
    }


    public String getIns_pto_observaciones() {
        return ins_pto_observaciones;
    }

    public void setIns_pto_observaciones(String ins_pto_observaciones) {
        this.ins_pto_observaciones = ins_pto_observaciones;
    }

}