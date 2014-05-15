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
     * Número total de registros involucrados en la evaluación
     */
    int TOTAL_REGISTROS;
    /**
     * Número total de registros relevantes (obtenidos y no obtenidos) 
     */
    int REGISTROS_RELEVANTES;
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
    
    
    public void setNoRegistros(int total){
        TOTAL_REGISTROS = total;
    }
    public int getNoRegistros(){
        return TOTAL_REGISTROS;
    }
    /**
     * 
     * @return Número total de registros relevantes 
     */
    public int getRegistrosRelevantes() {
        return REGISTROS_RELEVANTES;
    }

    /**
     * Establece a cero el parametro REGISTROS_RELEVANTES <br/>
     * REGISTROS_RELEVANTES = Número total de registros relevantes
     */
    public void resetRegistrosRelevantes() {
        REGISTROS_RELEVANTES = 0;
    }
    
    /**
     * Incrementa en una unidad el parametro REGISTROS_RELEVANTES <br/>
     * REGISTROS_RELEVANTES = Número total de registros relevantes
     */
    public void incrementRegistrosRelevantes(){
        REGISTROS_RELEVANTES++;
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
        return REGISTROS_RELEVANTES - A;
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
