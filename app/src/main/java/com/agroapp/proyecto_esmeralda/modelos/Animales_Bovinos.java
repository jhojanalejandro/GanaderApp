package com.agroapp.proyecto_esmeralda.modelos;

public class Animales_Bovinos extends Animales_Model{

    private Double anml_prod_litros;
    private Double anml_prod_kilos;
    private Double anml_prod_kilos_ganados;

    public Double getAnml_prod_litros() {
        return anml_prod_litros;
    }

    public void setAnml_prod_litros(Double anml_prod_litros) {
        this.anml_prod_litros = anml_prod_litros;
    }

    public Double getAnml_prod_kilos() {
        return anml_prod_kilos;
    }

    public void setAnml_prod_kilos(Double anml_prod_kilos) {
        this.anml_prod_kilos = anml_prod_kilos;
    }

    public Double getAnml_prod_kilos_ganados() {
        return anml_prod_kilos_ganados;
    }

    public void setAnml_prod_kilos_ganados(Double anml_prod_kilos_ganados) {
        this.anml_prod_kilos_ganados = anml_prod_kilos_ganados;
    }
}
