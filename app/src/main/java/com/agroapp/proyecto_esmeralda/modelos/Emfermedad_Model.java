    package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Emfermedad_Model extends Gastos_Insumos implements Serializable {

    private String efmd_evento;
    private String efmd_veterinario;


    public String getEfmd_evento() {
        return efmd_evento;
    }

    public void setEfmd_evento(String efmd_evento) {
        this.efmd_evento = efmd_evento;
    }

    public String getEfmd_veterinario() {
        return efmd_veterinario;
    }

    public void setEfmd_veterinario(String efmd_veterinario) {
        this.efmd_veterinario = efmd_veterinario;
    }

}
