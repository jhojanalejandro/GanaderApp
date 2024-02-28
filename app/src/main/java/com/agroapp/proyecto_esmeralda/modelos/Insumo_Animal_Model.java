package com.agroapp.proyecto_esmeralda.modelos;


import java.io.Serializable;


public class Insumo_Animal_Model implements Serializable {

    private String ins_animal_tipo;
    private String ins_animal_fecha;
    private String ins_animal_observaciones;
    private String ins_animal_tratamiento;
    private int ins_cant;
    private String ins_nombre;
    private String ins_animal_retiro;
    private String ins_animal_n_empleado;



    public Insumo_Animal_Model() {
    }

    public String getIns_animal_retiro() {
        return ins_animal_retiro;
    }

    public void setIns_animal_retiro(String ins_animal_retiro) {
        this.ins_animal_retiro = ins_animal_retiro;
    }

    public String getIns_animal_fecha() {
        return ins_animal_fecha;
    }

    public void setIns_animal_fecha(String ins_animal_fecha) {
        this.ins_animal_fecha = ins_animal_fecha;
    }


    public String getIns_nombre() {
        return ins_nombre;
    }

    public void setIns_nombre(String ins_nombre) {
        this.ins_nombre = ins_nombre;
    }

    public int getIns_cant() {
        return ins_cant;
    }

    public void setIns_cant(int ins_cant) {
        this.ins_cant = ins_cant;
    }

    public String getIns_animal_observaciones() {
        return ins_animal_observaciones;
    }

    public void setIns_animal_observaciones(String ins_animal_observaciones) {
        this.ins_animal_observaciones = ins_animal_observaciones;
    }



    public String getIns_animal_tipo() {
        return ins_animal_tipo;
    }

    public void setIns_animal_tipo(String ins_animal_tipo) {
        this.ins_animal_tipo = ins_animal_tipo;
    }

    public String getIns_animal_tratamiento() {
        return ins_animal_tratamiento;
    }

    public void setIns_animal_tratamiento(String ins_animal_tratamiento) {
        this.ins_animal_tratamiento = ins_animal_tratamiento;
    }


    public String getIns_animal_n_empleado() {
        return ins_animal_n_empleado;
    }

    public void setIns_animal_n_empleado(String ins_animal_n_empleado) {
        this.ins_animal_n_empleado = ins_animal_n_empleado;
    }
}


