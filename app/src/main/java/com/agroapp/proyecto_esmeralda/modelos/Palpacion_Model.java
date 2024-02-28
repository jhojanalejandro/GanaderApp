package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Palpacion_Model extends  Gastos_Insumos implements Serializable {
    private String palp_veterinario;
    private String palp_result;
    private String palp_fecha_pre;


    public Palpacion_Model() {
    }

    public String getPalp_veterinario() {
        return palp_veterinario;
    }

    public void setPalp_veterinario(String palp_veterinario) {
        this.palp_veterinario = palp_veterinario;
    }

    public String getPalp_result() {
        return palp_result;
    }

    public void setPalp_result(String palp_result) {
        this.palp_result = palp_result;
    }

    public String getPalp_fecha_pre() {
        return palp_fecha_pre;
    }

    public void setPalp_fecha_pre(String palp_fecha_pre) {
        this.palp_fecha_pre = palp_fecha_pre;
    }
}
