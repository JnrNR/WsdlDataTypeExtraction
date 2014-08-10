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
    private String linkGoogleChartestructuraA;
    private String linkGoogleChartestructuraB;
    
    private float porcentajeT;
    private float porcentajeE;
    private float porcentajeS;

    public MatcherResult(String servicioA, String servicioB, String estructuraA, String estructuraB, String linkGoogleChartestructuraA, String linkGoogleChartestructuraB, float porcentajeT, float porcentajeE, float porcentajeS){
        this.servicioA = servicioA;
        this.servicioB = servicioB;
        this.estructuraA = estructuraA;
        this.estructuraB = estructuraB;
        this.porcentajeT = porcentajeT;
        this.porcentajeE = porcentajeE;
        this.porcentajeS = porcentajeS;
        this.linkGoogleChartestructuraA = linkGoogleChartestructuraA;
        this.linkGoogleChartestructuraB = linkGoogleChartestructuraB;
        
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

    public String getLinkGoogleChartestructuraA() {
        return linkGoogleChartestructuraA;
    }

    public void setLinkGoogleChartestructuraA(String linkGoogleChartestructuraA) {
        this.linkGoogleChartestructuraA = linkGoogleChartestructuraA;
    }

    public String getLinkGoogleChartestructuraB() {
        return linkGoogleChartestructuraB;
    }

    public void setLinkGoogleChartestructuraB(String linkGoogleChartestructuraB) {
        this.linkGoogleChartestructuraB = linkGoogleChartestructuraB;
    }   

    public float getPorcentajeT() {
        return porcentajeT;
    }

    public void setPorcentajeT(float porcentajeT) {
        this.porcentajeT = porcentajeT;
    }

    public float getPorcentajeE() {
        return porcentajeE;
    }

    public void setPorcentajeE(float porcentajeE) {
        this.porcentajeE = porcentajeE;
    }

    public float getPorcentajeS() {
        return porcentajeS;
    }

    public void setPorcentajeS(float porcentajeS) {
        this.porcentajeS = porcentajeS;
    }
    
    
    
    
}
