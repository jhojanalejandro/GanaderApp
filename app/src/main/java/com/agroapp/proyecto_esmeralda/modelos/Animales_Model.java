package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Animales_Model implements Serializable {

    private String anml_nombre;
    private String anml_id;
    private String anml_procedencia;
    private String anml_genero;
    private String anml_subasta;
    private String anml_id_subasta;
    private String anml_tipo;
    private String anml_etapa_tipo;
    private Double anml_prod_kilos_ganados;
    private String anml_pto_estadia;
    private String anml_padre;
    private String anml_salida;
    private String anml_madre;
    private String anml_fecha_movimiento;
    private String anml_fecha_salida;
    private String anml_lote;
    private Double anml_g_vida;
    private Long anml_precio;
    private String anml_fecha_nacimiento;
    private String anml_fecha_ingreso;
    private String anml_imagen;
    private String anml_raza;
    private String anml_observaciones;

    public Animales_Model() {
    }

    public String getAnml_id() {
        return anml_id;
    }

    public void setAnml_id(String anml_id) {
        this.anml_id = anml_id;
    }

    public String getAnml_subasta() {
        return anml_subasta;
    }

    public void setAnml_subasta(String anml_subasta) {
        this.anml_subasta = anml_subasta;
    }

    public String getAnml_id_subasta() {
        return anml_id_subasta;
    }

    public void setAnml_id_subasta(String anml_id_subasta) {
        this.anml_id_subasta = anml_id_subasta;
    }

    private String getAnml_fecha_ingreso() {
        return anml_fecha_ingreso;
    }

    public void setAnml_fecha_ingreso(String anml_fecha_ingreso) {
        this.anml_fecha_ingreso = anml_fecha_ingreso;
    }

    public Double getAnml_prod_kilos_ganados() {
        return anml_prod_kilos_ganados;
    }

    public void setAnml_prod_kilos_ganados(Double anml_prod_kilos_ganados) {
        this.anml_prod_kilos_ganados = anml_prod_kilos_ganados;
    }


    public String getAnml_fecha_nacimiento() {
        return anml_fecha_nacimiento;
    }

    public void setAnml_fecha_nacimiento(String anml_fecha_nacimiento) {
        this.anml_fecha_nacimiento = anml_fecha_nacimiento;
    }

    public String getAnml_fecha_movimiento() {
        return anml_fecha_movimiento;
    }

    public void setAnml_fecha_movimiento(String anml_fecha_movimiento) {
        this.anml_fecha_movimiento = anml_fecha_movimiento;
    }

    public String getAnml_fecha_salida() {
        return anml_fecha_salida;
    }

    public void setAnml_fecha_salida(String anml_fecha_salida) {
        this.anml_fecha_salida = anml_fecha_salida;
    }


    public String getAnml_imagen() {
        return anml_imagen;
    }

    public void setAnml_imagen(String anml_imagen) {
        this.anml_imagen = anml_imagen;
    }

    private String getAnml_observaciones() {
        return anml_observaciones;
    }

    private void setAnml_observaciones(String anml_observaciones) {
        this.anml_observaciones = anml_observaciones;
    }

    public String getAnml_lote() {
        return anml_lote;
    }

    public void setAnml_lote(String anml_lote) {
        this.anml_lote = anml_lote;
    }

    public String getAnml_etapa_tipo() {
        return anml_etapa_tipo;
    }

    public void setAnml_etapa_tipo(String anml_etapa_tipo) {
        this.anml_etapa_tipo = anml_etapa_tipo;
    }

    public String getAnml_salida() {
        return anml_salida;
    }

    public void setAnml_salida(String anml_salida) {
        this.anml_salida = anml_salida;
    }

    public String getAnml_procedencia() {
        return anml_procedencia;
    }

    public void setAnml_procedencia(String anml_procedencia) {
        this.anml_procedencia = anml_procedencia;
    }


    public String getAnml_genero() {
        return anml_genero;
    }

    public void setAnml_genero(String anml_genero) {
        this.anml_genero = anml_genero;
    }


    public String getAnml_pto_estadia() {
        return anml_pto_estadia;
    }

    public void setAnml_pto_estadia(String anml_pto_estadia) {
        this.anml_pto_estadia = anml_pto_estadia;
    }

    public Double getAnml_g_vida() {
        return anml_g_vida;
    }

    public void setAnml_g_vida(Double anml_g_vida) {
        this.anml_g_vida = anml_g_vida;
    }


    public Long getAnml_precio() {
        return anml_precio;
    }

    public void setAnml_precio(Long anml_precio) {
        this.anml_precio = anml_precio;
    }



    public void setAnml_tipo(String anml_tipo) {
        this.anml_tipo = anml_tipo;
    }

    public String getAnml_tipo() {
        return anml_tipo;
    }

    public void setAnml_madre(String anml_madre) {
        this.anml_madre = anml_madre;
    }


    public String getAnml_madre() {
        return anml_madre;
    }

    public String getAnml_raza() {
        return anml_raza;
    }

    public void setAnml_raza(String anml_raza) {
        this.anml_raza = anml_raza;
    }

    public String getAnml_nombre() {
        return anml_nombre;
    }


    public void setAnml_nombre(String anml_nombre) {
        this.anml_nombre = anml_nombre;
    }


    public String getAnml_padre() {
        return anml_padre;
    }

    public void setAnml_padre(String anml_padre) {
        this.anml_padre = anml_padre;
    }
}
