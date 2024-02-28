package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Vacunacion_Model extends Gastos_Insumos implements Serializable {

    private String vcn_tipo_eleccion;
    private String vcn_evento;
    private String vcn_veterinario;


    public Vacunacion_Model() {
    }

    public String getVcn_tipo_eleccion() {
        return vcn_tipo_eleccion;
    }

    public void setVcn_tipo_eleccion(String vcn_tipo_eleccion) {
        this.vcn_tipo_eleccion = vcn_tipo_eleccion;
    }

    public String getVcn_evento() {
        return vcn_evento;
    }

    public void setVcn_evento(String vcn_evento) {
        this.vcn_evento = vcn_evento;
    }

    public String getVcn_veterinario() {
        return vcn_veterinario;
    }

    public void setVcn_veterinario(String vcn_veterinario) {
        this.vcn_veterinario = vcn_veterinario;
    }
}
