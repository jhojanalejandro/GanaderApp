package com.agroapp.proyecto_esmeralda.modelos;

public class Gastos_Insumos{

    private String fecha_registro;
    private int cantidad_droga;
    private String nombre_droga;
    private String observaciones;
    private String tratamiento;
    private String tipo_registro;

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public int getCantidad_droga() {
        return cantidad_droga;
    }

    public void setCantidad_droga(int cantidad_droga) {
        this.cantidad_droga = cantidad_droga;
    }

    public String getNombre_droga() {
        return nombre_droga;
    }

    public void setNombre_droga(String nombre_droga) {
        this.nombre_droga = nombre_droga;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

}
