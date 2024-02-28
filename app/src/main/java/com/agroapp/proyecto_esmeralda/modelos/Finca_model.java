package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;
import java.util.List;

public class Finca_model implements Serializable {

    private String finca_nombre;
    private String tipo_propiedad;
    private String municipio;
    private String vereda;
    private Boolean pago_arriendo;
    private int  finca_extesion;
    private Long  finca_precio_leche;
    private String finca_ubicacion;
    private List<String > finca_tipos_produccion;

    private Long finca_precio_arriendo;

    public Finca_model() {
    }

    public Finca_model(String finca_nombre, int finca_extesion, String finca_ubicacion, List<String> finca_tipos_produccion, Long finca_precio_arriendo,String tipo_propiedad, String municipio, String vereda, Long finca_precio_leche, Boolean pago_arriendo) {
        this.finca_nombre = finca_nombre;
        this.finca_extesion = finca_extesion;
        this.finca_ubicacion = finca_ubicacion;
        this.finca_tipos_produccion = finca_tipos_produccion;
        this.finca_precio_arriendo = finca_precio_arriendo;
        this.tipo_propiedad = tipo_propiedad;
        this.municipio = municipio;
        this.vereda = vereda;
        this.finca_precio_leche = finca_precio_leche;
        this.pago_arriendo = pago_arriendo;

    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getVereda() {
        return vereda;
    }

    public void setVereda(String vereda) {
        this.vereda = vereda;
    }

    public String getTipo_propiedad() {
        return tipo_propiedad;
    }

    public void setTipo_propiedad(String tipo_propiedad) {
        this.tipo_propiedad = tipo_propiedad;
    }

    public Boolean getPago_arriendo() {
        return pago_arriendo;
    }

    public void setPago_arriendo(Boolean pago_arriendo) {
        this.pago_arriendo = pago_arriendo;
    }

    public Long getFinca_precio_arriendo() {
        return finca_precio_arriendo;
    }

    public void setFinca_precio_arriendo(Long finca_precio_arriendo) {
        this.finca_precio_arriendo = finca_precio_arriendo;
    }

    public List<String> getFinca_tipos_produccion() {
        return finca_tipos_produccion;
    }

    public void setFinca_tipos_produccion(List<String> finca_tipos_produccion) {
        this.finca_tipos_produccion = finca_tipos_produccion;
    }


    public Long getFinca_precio_leche() {
        return finca_precio_leche;
    }

    public void setFinca_precio_leche(Long finca_precio_leche) {
        this.finca_precio_leche = finca_precio_leche;
    }

    public int getFinca_extesion() {
        return finca_extesion;
    }

    public void setFinca_extesion(int finca_extesion) {
        this.finca_extesion = finca_extesion;
    }


    public String getFinca_nombre() {
        return finca_nombre;
    }

    public void setFinca_nombre(String finca_nombre) {
        this.finca_nombre = finca_nombre;
    }


    public String getFinca_ubicacion() {
        return finca_ubicacion;
    }

    public void setFinca_ubicacion(String finca_ubicacion) {
        this.finca_ubicacion = finca_ubicacion;
    }


}
