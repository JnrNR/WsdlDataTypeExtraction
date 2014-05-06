/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jnr.utilities;

/**
 *
 * @author Jorge Náder Roa
 */
public class PrecisionRecall {
    
    /**
     * A = Número de registros obtenidos que son relevantes 
     */
    int A;
    /**
     * B = Número de registros no obtenidos que son relevantes
     */
    int B; 
    /**
     * C = Número de registros obtenidos que son irrelevantes
     */
    int C;
    
    public PrecisionRecall(){
        A = B = C = 0;
    }
    
    /**
     * 
     * @return Número de registros obtenidos que son relevantes 
     */
    public int getA() {
        return A;
    }

    /**
     * Establece a cero el parametro A <br/>
     * A = Número de registros obtenidos que son relevantes
     */
    public void resetA() {
        A = 0;
    }
    
    /**
     * Incrementa en una unidad el parametro A <br/>
     * A = Número de registros obtenidos que son relevantes
     */
    public void incrementA(){
        A++;
    }

    /**
     * 
     * @return Número de registros no obtenidos que son relevantes
     */
    public int getB() {
        return B;
    }

    /**
     * Establece a cero el parametro B <br/>
     * B = Número de registros no obtenidos que son relevantes
     */
    public void resetB() {
        B = 0;
    }
    
    /**
     * Incrementa en una unidad el parametro B <br/>
     * B = Número de registros no obtenidos que son relevantes
     */
    public void incrementB(){
        B++;
    }
    
    /**
     * 
     * @return Número de registros obtenidos que son irrelevantes
     */
    public int getC() {
        return C;
    }

    /**
     * Establece a cero el parametro C <br/>
     * C = Número de registros obtenidos que son irrelevantes
     */
    public void resetC() {
        C = 0;
    }
    
    /**
     * Incrementa en una unidad el parametro B <br/>
     * C = Número de registros obtenidos que son irrelevantes
     */
    public void incrementC(){
        C++;
    }
    
    /**
     * Calcula la metrica Precision
     * @return porcentaje de precision
     */
    public float getPrecision(){
        return (A / (A+C)) * 100;
    }
    
    /**
     * Calcula la metrica Recall
     * @return procentaje de recall
     */
    public float getRecall(){
        return (A / (A+B)) * 100;
    }
    
    
}
