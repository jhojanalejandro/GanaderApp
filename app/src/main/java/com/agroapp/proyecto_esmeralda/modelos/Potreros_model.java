package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Potreros_model implements Serializable {
    private String pto_nombre;
    private int pto_extension;
    private String pto_lote;
    private String pto_tipo_pasto;
    private String pto_estado;
    private String fecha_salida;
    private String fecha_ingreso;
    private int pto_cant_anml_pto;
    private String pto_observaciones;


    public Potreros_model() {
    }


    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getPto_lote() {
        return pto_lote;
    }

    public void setPto_lote(String pto_lote) {
        this.pto_lote = pto_lote;
    }

    public String getPto_tipo_pasto() {
        return pto_tipo_pasto;
    }

    public void setPto_tipo_pasto(String pto_tipo_pasto) {
        this.pto_tipo_pasto = pto_tipo_pasto;
    }

    public String getPto_estado() {
        return pto_estado;
    }

    public void setPto_estado(String pto_estado) {
        this.pto_estado = pto_estado;
    }

    public int getPto_extension() {
        return pto_extension;
    }

    public void setPto_extension(int pto_extension) {
        this.pto_extension = pto_extension;
    }


    public String getPto_nombre() {
        return pto_nombre;
    }

    public void setPto_nombre(String pto_nombre) {
        this.pto_nombre = pto_nombre;
    }


    public int getPto_cant_anml_pto() {
        return pto_cant_anml_pto;
    }

    public void setPto_cant_anml_pto(int pto_cant_anml_pto) {
        this.pto_cant_anml_pto = pto_cant_anml_pto;
    }


    public String getPto_observaciones() {
        return pto_observaciones;
    }

    public void setPto_observaciones(String pto_observaciones) {
        this.pto_observaciones = pto_observaciones;
    }

}
