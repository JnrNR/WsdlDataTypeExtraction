
package jnr.utilities;

/**
 *
 * @author Jorge Náder Roa
 */
public class Estadistica {
    
    
    public static double desviacionEstandard(double[] vector){
        
        double varianza;
        double sumaDeCuadrados = 0;
        double sumaVector = 0;
        
        //Calculando la varianza
        for(int i=0; i<vector.length; i++){
            sumaVector += vector[i];
            sumaDeCuadrados += Math.pow(vector[i], 2);
        }

        varianza = (sumaDeCuadrados - ( sumaVector/vector.length ))/(vector.length -1);
        
        return Math.sqrt(varianza);
    }
    
    public static double covarianza(double[] vectorX, double[] vectorY){
        double sumaX = 0, sumaY = 0, sumaXY = 0;
        double covarianza = 0;
        
        if(vectorX.length == vectorY.length){
            for(int i=0; i<vectorX.length; i++){
                sumaX += vectorX[i];
                sumaY += vectorY[i];
                sumaXY += vectorX[i]*vectorY[i];
            }
            
            covarianza = ( sumaXY - ((sumaX*sumaY)/vectorX.length) )/(vectorX.length-1);
        }
        
        return covarianza;
    }
    
}
