package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Pesaje_Animal_Model implements Serializable {

    private String pesaje_tipo;
    private String pesaje_observacoines;
    private Double pesaje_peso_ingreso;
    private Double pesaje_peso_nacimiento;
    private Double pesaje_peso_destete;
    private Double pesaje_peso_salida;
    private Double pesaje_peso_pos_ingreso;
    private String pesaje_fecha_pos_ingreso;
    private String pesaje_fecha_salida;
    private String pesaje_fecha_destete;
    private String pesaje_fecha_ingreso;
    private String pesaje_fecha_nacimiento;
    private String pesaje_fecha_pre_salida;
    private Double pesaje_peso_pre_salida;
    private Double pesaje_result;

    public Pesaje_Animal_Model() {
    }

    public Pesaje_Animal_Model(String pesaje_tipo, String pesaje_observacoines, Double pesaje_peso_ingreso, Double pesaje_peso_nacimiento, Double pesaje_peso_destete, Double pesaje_peso_salida, Double pesaje_pos_ingreso, String pesaje_fecha_pos_ingreso, String pesaje_fecha_salida, String pesaje_fecha_destete, String pesaje_fecha_ingreso, String pesaje_fecha_nacimiento, String pesaje_fecha_pre_salida, Double pesaje_pre_salida, Double pesaje_result) {
        this.pesaje_tipo = pesaje_tipo;
        this.pesaje_observacoines = pesaje_observacoines;
        this.pesaje_peso_ingreso = pesaje_peso_ingreso;
        this.pesaje_peso_nacimiento = pesaje_peso_nacimiento;
        this.pesaje_peso_destete = pesaje_peso_destete;
        this.pesaje_peso_salida = pesaje_peso_salida;
        this.pesaje_peso_pos_ingreso = pesaje_pos_ingreso;
        this.pesaje_fecha_pos_ingreso = pesaje_fecha_pos_ingreso;
        this.pesaje_fecha_salida = pesaje_fecha_salida;
        this.pesaje_fecha_destete = pesaje_fecha_destete;
        this.pesaje_fecha_ingreso = pesaje_fecha_ingreso;
        this.pesaje_fecha_nacimiento = pesaje_fecha_nacimiento;
        this.pesaje_fecha_pre_salida = pesaje_fecha_pre_salida;
        this.pesaje_peso_pre_salida = pesaje_pre_salida;
        this.pesaje_result = pesaje_result;
    }

    public String getPesaje_fecha_destete() {
        return pesaje_fecha_destete;
    }

    public void setPesaje_fecha_destete(String pesaje_fecha_destete) {
        this.pesaje_fecha_destete = pesaje_fecha_destete;
    }

    public String getPesaje_fecha_pre_salida() {
        return pesaje_fecha_pre_salida;
    }

    public void setPesaje_fecha_pre_salida(String pesaje_fecha_pre_salida) {
        this.pesaje_fecha_pre_salida = pesaje_fecha_pre_salida;
    }

    public String getPesaje_fecha_pos_ingreso() {
        return pesaje_fecha_pos_ingreso;
    }

    public void setPesaje_fecha_pos_ingreso(String pesaje_fecha_pos_ingreso) {
        this.pesaje_fecha_pos_ingreso = pesaje_fecha_pos_ingreso;
    }


    public String getPesaje_fecha_salida() {
        return pesaje_fecha_salida;
    }

    public void setPesaje_fecha_salida(String pesaje_fecha_salida) {
        this.pesaje_fecha_salida = pesaje_fecha_salida;
    }


    public String getPesaje_tipo() {
        return pesaje_tipo;
    }

    public void setPesaje_tipo(String pesaje_tipo) {
        this.pesaje_tipo = pesaje_tipo;
    }

    public String getPesaje_observacoines() {
        return pesaje_observacoines;
    }

    public void setPesaje_observacoines(String pesaje_observacoines) {
        this.pesaje_observacoines = pesaje_observacoines;
    }


    public Double getPesaje_peso_ingreso() {
        return pesaje_peso_ingreso;
    }

    public void setPesaje_peso_ingreso(Double pesaje_peso_ingreso) {
        this.pesaje_peso_ingreso = pesaje_peso_ingreso;
    }

    public Double getPesaje_peso_nacimiento() {
        return pesaje_peso_nacimiento;
    }

    public void setPesaje_peso_nacimiento(Double pesaje_peso_nacimiento) {
        this.pesaje_peso_nacimiento = pesaje_peso_nacimiento;
    }

    public Double getPesaje_peso_destete() {
        return pesaje_peso_destete;
    }

    public void setPesaje_peso_destete(Double pesaje_peso_destete) {
        this.pesaje_peso_destete = pesaje_peso_destete;
    }

    public Double getPesaje_peso_salida() {
        return pesaje_peso_salida;
    }

    public void setPesaje_peso_salida(Double pesaje_peso_salida) {
        this.pesaje_peso_salida = pesaje_peso_salida;
    }


    public Double getPesaje_peso_pos_ingreso() {
        return pesaje_peso_pos_ingreso;
    }

    public void setPesaje_peso_pos_ingreso(Double pesaje_peso_pos_ingreso) {
        this.pesaje_peso_pos_ingreso = pesaje_peso_pos_ingreso;
    }

    public Double getPesaje_peso_pre_salida() {
        return pesaje_peso_pre_salida;
    }

    public void setPesaje_peso_pre_salida(Double pesaje_peso_pre_salida) {
        this.pesaje_peso_pre_salida = pesaje_peso_pre_salida;
    }

    public Double getPesaje_result() {
        return pesaje_result;
    }

    public String getPesaje_fecha_ingreso() {
        return pesaje_fecha_ingreso;
    }

    public void setPesaje_fecha_ingreso(String pesaje_fecha_ingreso) {
        this.pesaje_fecha_ingreso = pesaje_fecha_ingreso;
    }

    public String getPesaje_fecha_nacimiento() {
        return pesaje_fecha_nacimiento;
    }

    public void setPesaje_fecha_nacimiento(String pesaje_fecha_nacimiento) {
        this.pesaje_fecha_nacimiento = pesaje_fecha_nacimiento;
    }

    public void setPesaje_result(Double pesaje_result) {
        this.pesaje_result = pesaje_result;
    }

}
