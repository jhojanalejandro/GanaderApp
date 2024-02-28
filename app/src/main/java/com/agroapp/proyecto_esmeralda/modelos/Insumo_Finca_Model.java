package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;
import java.util.Date;

public class Insumo_Finca_Model implements Serializable {

    private String ins_finca_nombre;
    private String ins_finca_tipo;
    private String ins_finca_fecha;
    private int ins_finca_restante;
    private int ins_finca_cantidad;
    private Double ins_finca_precio;
    private Double ins_finca_precio_unitario;
    private String ins_finca_observaciones;


    public Insumo_Finca_Model() {
    }

    public Insumo_Finca_Model(String ins_finca_nombre, String ins_finca_tipo, String ins_finca_fecha, int ins_finca_restante, int ins_finca_cantidad, Double ins_finca_precio, Double ins_finca_precio_unitario, String ins_finca_observaciones) {
        this.ins_finca_nombre = ins_finca_nombre;
        this.ins_finca_tipo = ins_finca_tipo;
        this.ins_finca_fecha = ins_finca_fecha;
        this.ins_finca_restante = ins_finca_restante;
        this.ins_finca_cantidad = ins_finca_cantidad;
        this.ins_finca_precio = ins_finca_precio;
        this.ins_finca_precio_unitario = ins_finca_precio_unitario;
        this.ins_finca_observaciones = ins_finca_observaciones;
    }

    public String getIns_finca_fecha() {
        return ins_finca_fecha;
    }

    public void setIns_finca_fecha(String ins_finca_fecha) {
        this.ins_finca_fecha = ins_finca_fecha;
    }

    public int getIns_finca_cantidad() {
        return ins_finca_cantidad;
    }

    public void setIns_finca_cantidad(int ins_finca_cantidad) {
        this.ins_finca_cantidad = ins_finca_cantidad;
    }

    public Double getIns_finca_precio() {
        return ins_finca_precio;
    }

    public void setIns_finca_precio(Double ins_finca_precio) {
        this.ins_finca_precio = ins_finca_precio;
    }

    public Double getIns_finca_precio_unitario() {
        return ins_finca_precio_unitario;
    }

    public void setIns_finca_precio_unitario(Double ins_finca_precio_unitario) {
        this.ins_finca_precio_unitario = ins_finca_precio_unitario;
    }

    public String getIns_finca_nombre() {
        return ins_finca_nombre;
    }

    public void setIns_finca_nombre(String ins_finca_nombre) {
        this.ins_finca_nombre = ins_finca_nombre;
    }

    public String getIns_finca_tipo() {
        return ins_finca_tipo;
    }

    public void setIns_finca_tipo(String ins_finca_tipo) {
        this.ins_finca_tipo = ins_finca_tipo;
    }


    public int getIns_finca_restante() {
        return ins_finca_restante;
    }

    public void setIns_finca_restante(int ins_finca_restante) {
        this.ins_finca_restante = ins_finca_restante;
    }



    public String getIns_finca_observaciones() {
        return ins_finca_observaciones;
    }

    public void setIns_finca_observaciones(String ins_finca_observaciones) {
        this.ins_finca_observaciones = ins_finca_observaciones;
    }
}
