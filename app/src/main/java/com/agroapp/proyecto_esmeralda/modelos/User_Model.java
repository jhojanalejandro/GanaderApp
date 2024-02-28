package com.agroapp.proyecto_esmeralda.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User_Model implements Serializable {

    private String user_nombre;
    private String user_clave;
    private String user_cedula;
    private String user_id;
    private Boolean user_cession;
    private Boolean activacion_cuenta;
    private Boolean user_pago_factura;
    private int nivel_acceso;
    private ArrayList<String > fincas;
    private String user_tipo;
    private String user_alias;
    private Long user_pago_mensual;
    private String user_gmail;
    private String user_img;
    private Long user_telefono;


    public User_Model() {
    }



    public User_Model(String user_nombre, String user_clave, String user_cedula, String user_id, Boolean user_cession, Boolean user_pago_factura, int nivel_acceso, ArrayList<String> fincas, String user_tipo, String user_alias, Long user_pago_mensual, String user_gmail, String user_img, Long user_telefono, Boolean activacion_cuenta) {
        this.user_nombre = user_nombre;
        this.user_clave = user_clave;
        this.user_cedula = user_cedula;
        this.user_id = user_id;
        this.user_cession = user_cession;
        this.user_pago_factura = user_pago_factura;
        this.nivel_acceso = nivel_acceso;
        this.fincas = fincas;
        this.user_tipo = user_tipo;
        this.user_alias = user_alias;
        this.user_pago_mensual = user_pago_mensual;
        this.user_gmail = user_gmail;
        this.user_img = user_img;
        this.user_telefono = user_telefono;
        this.activacion_cuenta = activacion_cuenta;
    }

    public Long getUser_pago_mensual() {
        return user_pago_mensual;
    }

    public void setUser_pago_mensual(Long user_pago_mensual) {
        this.user_pago_mensual = user_pago_mensual;
    }

    public int getNivel_acceso() {
        return nivel_acceso;
    }

    public void setNivel_acceso(int nivel_acceso) {
        this.nivel_acceso = nivel_acceso;
    }

    public ArrayList<String> getFincas() {
        return fincas;
    }

    public void setFincas(ArrayList<String> fincas) {
        this.fincas = fincas;
    }

    public Boolean getUser_cession() {
        return user_cession;
    }

    public void setUser_cession(Boolean user_cession) {
        this.user_cession = user_cession;
    }

    public Boolean getUser_pago_factura() {
        return user_pago_factura;
    }

    public void setUser_pago_factura(Boolean user_pago_factura) {
        this.user_pago_factura = user_pago_factura;
    }

    public String getUser_nombre() {
        return user_nombre;
    }

    public void setUser_nombre(String user_nombre) {
        this.user_nombre = user_nombre;
    }

    public String getUser_clave() {
        return user_clave;
    }

    public void setUser_clave(String user_clave) {
        this.user_clave = user_clave;
    }


    public String getUser_cedula() {
        return user_cedula;
    }

    public void setUser_cedula(String user_cedula) {
        this.user_cedula = user_cedula;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_tipo() {
        return user_tipo;
    }

    public void setUser_tipo(String user_tipo) {
        this.user_tipo = user_tipo;
    }


    public String getUser_alias() {
        return user_alias;
    }

    public void setUser_alias(String user_alias) {
        this.user_alias = user_alias;
    }

    public String getUser_gmail() {
        return user_gmail;
    }

    public void setUser_gmail(String user_gmail) {
        this.user_gmail = user_gmail;
    }

    public String getUser_img() {
        return user_img;
    }

    public Boolean getActivacion_cuenta() {
        return activacion_cuenta;
    }

    public void setActivacion_cuenta(Boolean activacion_cuenta) {
        this.activacion_cuenta = activacion_cuenta;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public Long getUser_telefono() {
        return user_telefono;
    }

    public void setUser_telefono(Long user_telefono) {
        this.user_telefono = user_telefono;
    }
}
