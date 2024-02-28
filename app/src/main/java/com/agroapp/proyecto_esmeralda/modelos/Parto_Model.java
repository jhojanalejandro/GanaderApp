package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Parto_Model extends Gastos_Insumos implements Serializable {

    private String part_raza_cria;
    private String part_result_real;
    private String part_result;
    private String part_number_breeding;
    private int part_number;
    private String part_father_name;

    public Parto_Model() {
    }

    public String getPart_raza_cria() {
        return part_raza_cria;
    }

    public void setPart_raza_cria(String part_raza_cria) {
        this.part_raza_cria = part_raza_cria;
    }

    public String getPart_number_breeding() {
        return part_number_breeding;
    }

    public void setPart_number_breeding(String part_number_breeding) {
        this.part_number_breeding = part_number_breeding;
    }

    public String getPart_father_name() {
        return part_father_name;
    }

    public void setPart_father_name(String part_father_name) {
        this.part_father_name = part_father_name;
    }

    public String getPart_result_real() {
        return part_result_real;
    }

    public void setPart_result_real(String part_result_real) {
        this.part_result_real = part_result_real;
    }

    public String getPart_result() {
        return part_result;
    }

    public void setPart_result(String part_result) {
        this.part_result = part_result;
    }



    public int getPart_number() {
        return part_number;
    }

    public void setPart_number(int part_number) {
        this.part_number = part_number;
    }


}
