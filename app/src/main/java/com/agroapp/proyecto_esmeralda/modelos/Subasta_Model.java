package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;
import java.util.List;

public class Subasta_Model implements Serializable {

    private String id_vendedor;
    private String id_comprador;
    private Double puja;
    private String id_venta;
    private String resultado;
    private String descripcion;
    private String video_demostracion;
    private List<String > imagenes_desmostracion;
    private String tipo_venta;
    private int cantidad_animales;
    private long precio_total;
    private String ubicacion;
    private String municipio;
    private String observaciones;
    private String fecha_registro;
    private String fecha_inicio;
    private String fecha_final;

    public Subasta_Model() {
    }

    public Subasta_Model(String id_vendedor, String id_comprador, Double puja, String id_venta, String resultado, String descripcion, String video_demostracion, List<String> imagenes_desmostracion, String tipo_venta, int cantidad_animales, long precio_total, String ubicacion, String municipio, String observaciones, String fecha_registro, String fecha_inicio, String fecha_final) {
        this.id_vendedor = id_vendedor;
        this.id_comprador = id_comprador;
        this.puja = puja;
        this.id_venta = id_venta;
        this.resultado = resultado;
        this.descripcion = descripcion;
        this.video_demostracion = video_demostracion;
        this.imagenes_desmostracion = imagenes_desmostracion;
        this.tipo_venta = tipo_venta;
        this.cantidad_animales = cantidad_animales;
        this.precio_total = precio_total;
        this.ubicacion = ubicacion;
        this.municipio = municipio;
        this.observaciones = observaciones;
        this.fecha_registro = fecha_registro;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
    }

    public String getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(String id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public String getId_comprador() {
        return id_comprador;
    }

    public void setId_comprador(String id_comprador) {
        this.id_comprador = id_comprador;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVideo_demostracion() {
        return video_demostracion;
    }

    public void setVideo_demostracion(String video_demostracion) {
        this.video_demostracion = video_demostracion;
    }

    public List<String> getImagenes_desmostracion() {
        return imagenes_desmostracion;
    }

    public void setImagenes_desmostracion(List<String> imagenes_desmostracion) {
        this.imagenes_desmostracion = imagenes_desmostracion;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    public int getCantidad_animales() {
        return cantidad_animales;
    }

    public void setCantidad_animales(int cantidad_animales) {
        this.cantidad_animales = cantidad_animales;
    }

    public long getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(long precio_total) {
        this.precio_total = precio_total;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public Double getPuja() {
        return puja;
    }

    public void setPuja(Double puja) {
        this.puja = puja;
    }

    public String getId_venta() {
        return id_venta;
    }

    public void setId_venta(String id_venta) {
        this.id_venta = id_venta;
    }
}
