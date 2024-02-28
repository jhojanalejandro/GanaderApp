package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Medida_Leche_Model implements Serializable {

    private String medida_tipo;
    private String medida_observacoines;
    private Double medida_leche_am;
    private Double medida_leche_pm;
    private String medida_anml_fecha;
    private int medida_count_dias;
    private Double medida_litros_medidos_mes;


    public Medida_Leche_Model() {
    }

    public  Medida_Leche_Model(String medida_tipo, String medida_observacoines, Double medida_leche_am, Double medida_leche_pm, String medida_anml_fecha, int medida_cont_dias, Double medida_litros_mes) {
        this.medida_tipo = medida_tipo;
        this.medida_observacoines = medida_observacoines;
        this.medida_leche_am = medida_leche_am;
        this.medida_leche_pm = medida_leche_pm;
        this.medida_anml_fecha = medida_anml_fecha;
        this.medida_count_dias = medida_cont_dias;
        this.medida_litros_medidos_mes = medida_litros_mes;
    }

    public Double getMedida_leche_am() {
        return medida_leche_am;
    }

    public void setMedida_leche_am(Double medida_leche_am) {
        this.medida_leche_am = medida_leche_am;
    }

    public Double getMedida_leche_pm() {
        return medida_leche_pm;
    }

    public void setMedida_leche_pm(Double medida_leche_pm) {
        this.medida_leche_pm = medida_leche_pm;
    }


    public Double getMedida_litros_medidos_mes() {
        return medida_litros_medidos_mes;
    }

    public void setMedida_litros_medidos_mes(Double medida_litros_medidos_mes) {
        this.medida_litros_medidos_mes = medida_litros_medidos_mes;
    }

    public String getMedida_anml_fecha() {
        return medida_anml_fecha;
    }

    public void setMedida_anml_fecha(String medida_anml_fecha) {
        this.medida_anml_fecha = medida_anml_fecha;
    }
    public int getMedida_count_dias() {
        return medida_count_dias;
    }

    public void setMedida_count_dias(int medida_cont_dias) {
        this.medida_count_dias = medida_cont_dias;
    }


    public String getMedida_tipo() {
        return medida_tipo;
    }

    public void setMedida_tipo(String medida_tipo) {
        this.medida_tipo = medida_tipo;
    }

    public String getMedida_observacoines() {
        return medida_observacoines;
    }

    public void setMedida_observacoines(String medida_observacoines) {
        this.medida_observacoines = medida_observacoines;
    }


}
