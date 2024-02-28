package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;

public class Tareas_Model implements Serializable {
    private String tarea;
    private String tarea_tipo;
    private String tarea_obs;


    public Tareas_Model() {
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getTarea_tipo() {
        return tarea_tipo;
    }

    public void setTarea_tipo(String tarea_tipo) {
        this.tarea_tipo = tarea_tipo;
    }

    public String getTarea_obs() {
        return tarea_obs;
    }

    public void setTarea_obs(String tarea_obs) {
        this.tarea_obs = tarea_obs;
    }
}
