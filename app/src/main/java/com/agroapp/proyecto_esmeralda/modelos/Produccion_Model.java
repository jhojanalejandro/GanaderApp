package com.agroapp.proyecto_esmeralda.modelos;

import android.util.Log;

import java.io.Serializable;

public class Produccion_Model implements Serializable {
    private Double prod_gasto_mensual;
    private Double prod_ganancia_mensual;
    private int prod_cant_dias_prom_leche;
    private int prod_dia_promedio;
    private int prod_dia_pesaje;
    private int prod_cant_animales_medidos_mes;
    private int prod_cant_animales_pesados_mes;
    private Boolean pago_nomina;
    private Double prod_cant_leche_medida_mes;
    private Double prod_cant_leche_producida_mes;
    private Double prod_cant_kilos_pesados_mes;
    private String prod_fecha;
    private String prod_obs;
    private Boolean pago_arriendo;

    public Produccion_Model() {
    }


    public Produccion_Model(Double prod_gasto_mensual, Double prod_ganancia_mensual,Double prod_cant_kilos_pesados_mes,  int prod_dia_pesaje,int prod_cantidad_dia_promedio, int prod_cant_animales_medidos_mes, int  prod_cant_animales_pesados_mes, Double prod_cant_leche_producida_mes,String prod_fecha, Double prod_cantidad_leche_medida_mes,int prod_dia_promedio  ,Boolean pago_nomina, Boolean pago_arriendo) {
        this.prod_gasto_mensual = prod_gasto_mensual;
        this.prod_ganancia_mensual = prod_ganancia_mensual;
        this.prod_cant_dias_prom_leche = prod_cantidad_dia_promedio;
        this.prod_dia_promedio = prod_dia_promedio;
        this.prod_dia_pesaje = prod_dia_pesaje;
        this.prod_cant_animales_medidos_mes = prod_cant_animales_medidos_mes;
        this.prod_cant_animales_pesados_mes = prod_cant_animales_pesados_mes;
        this.pago_nomina = pago_nomina;
        this.prod_cant_leche_medida_mes = prod_cantidad_leche_medida_mes;
        this.prod_cant_leche_producida_mes = prod_cant_leche_producida_mes;
        this.prod_cant_kilos_pesados_mes = prod_cant_kilos_pesados_mes;
        this.prod_fecha = prod_fecha;
        this.pago_arriendo = pago_arriendo;
        this.prod_obs = "VACIO";
    }

    public String getProd_obs() {
        return prod_obs;
    }

    public void setProd_obs(String prod_obs) {
        this.prod_obs = prod_obs;
    }

    public Double getProd_gasto_mensual() {
        return prod_gasto_mensual;
    }

    public void setProd_gasto_mensual(Double prod_gasto_mensual) {
        this.prod_gasto_mensual = prod_gasto_mensual;
    }

    public Double getProd_ganancia_mensual() {
        return prod_ganancia_mensual;
    }

    public void setProd_ganancia_mensual(Double prod_ganancia_mensual) {
        this.prod_ganancia_mensual = prod_ganancia_mensual;
    }

    public int getProd_cant_dias_prom_leche() {
        return prod_cant_dias_prom_leche;
    }

    public void setProd_cant_dias_prom_leche(int prod_cant_dias_prom_leche) {
        this.prod_cant_dias_prom_leche = prod_cant_dias_prom_leche;
    }

    public int getProd_dia_promedio() {
        return prod_dia_promedio;
    }

    public void setProd_dia_promedio(int prod_dia_promedio) {
        this.prod_dia_promedio = prod_dia_promedio;
    }

    public int getProd_dia_pesaje() {
        return prod_dia_pesaje;
    }

    public void setProd_dia_pesaje(int prod_dia_pesaje) {
        this.prod_dia_pesaje = prod_dia_pesaje;
    }

    public int getProd_cant_animales_medidos_mes() {
        return prod_cant_animales_medidos_mes;
    }

    public void setProd_cant_animales_medidos_mes(int prod_cant_animales_medidos_mes) {
        this.prod_cant_animales_medidos_mes = prod_cant_animales_medidos_mes;
    }

    public int getProd_cant_animales_pesados_mes() {
        return prod_cant_animales_pesados_mes;
    }

    public void setProd_cant_animales_pesados_mes(int prod_cant_animales_pesados_mes) {
        this.prod_cant_animales_pesados_mes = prod_cant_animales_pesados_mes;
    }

    public Boolean getPago_nomina() {
        return pago_nomina;
    }

    public void setPago_nomina(Boolean pago_nomina) {
        this.pago_nomina = pago_nomina;
    }

    public Double getProd_cant_leche_medida_mes() {
        return prod_cant_leche_medida_mes;
    }

    public void setProd_cant_leche_medida_mes(Double prod_cant_leche_medida_mes) {
        this.prod_cant_leche_medida_mes = prod_cant_leche_medida_mes;
    }

    public Double getProd_cant_leche_producida_mes() {
        return prod_cant_leche_producida_mes;
    }

    public void setProd_cant_leche_producida_mes(Double prod_cant_leche_producida_mes) {
        this.prod_cant_leche_producida_mes = prod_cant_leche_producida_mes;
    }

    public Double getProd_cant_kilos_pesados_mes() {
        return prod_cant_kilos_pesados_mes;
    }

    public void setProd_cant_kilos_pesados_mes(Double prod_cant_kilos_pesados_mes) {
        this.prod_cant_kilos_pesados_mes = prod_cant_kilos_pesados_mes;
    }

    public String getProd_fecha() {
        return prod_fecha;
    }

    public void setProd_fecha(String prod_fecha) {
        this.prod_fecha = prod_fecha;
    }

    public Boolean getPago_arriendo() {
        return pago_arriendo;
    }

    public void setPago_arriendo(Boolean pago_arriendo) {
        this.pago_arriendo = pago_arriendo;
    }
}
