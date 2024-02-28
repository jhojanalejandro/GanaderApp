package com.agroapp.proyecto_esmeralda.modelos;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Animal_Model implements Serializable {

    private String anml_nombre;
    private String anml_chapeta;
    private String anml_procedencia;
    private String anml_genero;
    private String anml_subasta;
    private String anml_id_subasta;
    private String anml_tipo;
    private String anml_etapa_tipo;
    private Double anml_prod_litros;
    private Double anml_prod_kilos;
    private Double anml_prod_kilos_ganados;
    private String anml_pto_estadia;
    private String anml_padre;
    private String anml_salida;
    private String anml_madre;
    private String anml_fecha_movimiento;
    private String anml_fecha_salida;
    private String anml_lote;
    private Double anml_g_vida;
    private Double anml_precio;
    private String anml_fecha_nacimiento;
    private String anml_fecha_ingreso;
    private String anml_imagen;
    private String anml_raza;
    private String anml_observaciones;


    public Animal_Model() {
    }


    public Animal_Model(String anml_nombre, String anml_fecha_ingreso, String anml_chapeta, String anml_procedencia, String id_subasta, String anml_genero, String anml_tipo, String anml_etapa_tipo, Double anml_prod_litros, Double anml_prod_kilos, Double anml_prod_kilos_ganados, String anml_pto_estadia, String anml_padre, String anml_salida, String anml_madre, String anml_fecha_movimiento, String anml_fecha_salida, String anml_lote, Double anml_g_vida, Double anml_precio, String anml_fecha_nacimiento, String anml_imagen, String anml_raza, String anml_observaciones) {
        this.anml_nombre = anml_nombre;
        this.anml_fecha_ingreso = anml_fecha_ingreso;
        this.anml_chapeta = anml_chapeta;
        this.anml_procedencia = anml_procedencia;
        this.anml_id_subasta = id_subasta;
        this.anml_genero = anml_genero;
        this.anml_tipo = anml_tipo;
        this.anml_etapa_tipo = anml_etapa_tipo;
        this.anml_prod_litros = anml_prod_litros;
        this.anml_prod_kilos = anml_prod_kilos;
        this.anml_prod_kilos_ganados = anml_prod_kilos_ganados;
        this.anml_pto_estadia = anml_pto_estadia;
        this.anml_padre = anml_padre;
        this.anml_salida = anml_salida;
        this.anml_madre = anml_madre;
        this.anml_fecha_movimiento = anml_fecha_movimiento;
        this.anml_fecha_salida = anml_fecha_salida;
        this.anml_lote = anml_lote;
        this.anml_g_vida = anml_g_vida;
        this.anml_precio = anml_precio;
        this.anml_fecha_nacimiento = anml_fecha_nacimiento;
        this.anml_imagen = anml_imagen;
        this.anml_raza = anml_raza;
        this.anml_observaciones = anml_observaciones;
        this.anml_subasta = "vacio";
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

    public String getAnml_fecha_ingreso() {
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

    public Double getAnml_prod_kilos() {
        return anml_prod_kilos;
    }

    public void setAnml_prod_kilos(Double anml_prod_kilos) {
        this.anml_prod_kilos = anml_prod_kilos;
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

    public Double getAnml_prod_litros() {
        return anml_prod_litros;
    }

    public void setAnml_prod_litros(Double anml_prod_litros) {
        this.anml_prod_litros = anml_prod_litros;
    }

    public String getAnml_observaciones() {
        return anml_observaciones;
    }

    public void setAnml_observaciones(String anml_observaciones) {
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

    public Double getAnml_precio() {
        return anml_precio;
    }

    public void setAnml_precio(Double anml_precio) {
        this.anml_precio = anml_precio;
    }

    public String getAnml_chapeta() {
        return anml_chapeta;
    }

    public void setAnml_chapeta(String anml_chapeta) {
        this.anml_chapeta = anml_chapeta;
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
