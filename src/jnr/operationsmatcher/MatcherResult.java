/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jnr.operationsmatcher;

/**
 *
 * @author Jnr
 */
public class MatcherResult {
    private String servicioA;
    private String servicioB;
    private String estructuraA;
    private String estructuraB;
    private float porcentaje; 

    public MatcherResult(String servicioA, String servicioB, String estructuraA, String estructuraB, float porcentaje){
        this.servicioA = servicioA;
        this.servicioB = servicioB;
        this.estructuraA = estructuraA;
        this.estructuraB = estructuraB;
        this.porcentaje = porcentaje;
        
    }
    
    
    public String getServicioA() {
        return servicioA;
    }

    public void setServicioA(String servicioA) {
        this.servicioA = servicioA;
    }

    public String getServicioB() {
        return servicioB;
    }

    public void setServicioB(String servicioB) {
        this.servicioB = servicioB;
    }

    public String getEstructuraA() {
        return estructuraA;
    }

    public void setEstructuraA(String estructuraA) {
        this.estructuraA = estructuraA;
    }

    public String getEstructuraB() {
        return estructuraB;
    }

    public void setEstructuraB(String estructuraB) {
        this.estructuraB = estructuraB;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    
    
}
