
package jnr.rdfwsdloperationsmatcher;

import com.hp.hpl.jena.rdf.model.Model;
import java.util.List;
import jnr.datatypeextraction.WSDLExtractStructure;
import jnr.utilities.Log;
import jnr.wsdltreestruct.ArbolWSDL;

/**
 *
 * @author Jorge Náder Roa
 */
public class RDFMatcher {
    //Depuración
    public Log log = new Log(true, true, Log.ANSI_PURPLE);
    /////////////////////////////////////////////////
    
    private Model rdf1;
    private Model rdf2;
    
    public RDFMatcher(Model rdf1, Model rdf2){
        this.rdf1 = rdf1;
        this.rdf2 = rdf2;
        
        if(rdf1!=null && rdf2!=null){
        
        }else{
            log.printLogErrorMessage("El objeto RDFMatcher no puede instanciarce por que alguno de los parametros es nulo");
        }
    }
    
    public int matchModels(){
        int similitud = 0;
        
        //Determinando si los modelos son isomorfos
        if(rdf1.equals(rdf2) == true){
            similitud = 1;
        }else{
            similitud = 0;
        }
        
        return similitud;
    }
    
    public static int matchRDFModels(Model rdf1, Model rdf2){
        
        int similitud = 0;
        if(rdf1!=null && rdf2!=null){
                //Determinando si los modelos son isomorfos
                if(rdf1.equals(rdf2) == true){
                    similitud = 1;
                }
            
        }else{
            Log log = new Log(true, true, Log.ANSI_GREEN);
            log.printLogErrorMessage("El objeto RDFMatcher no puede instanciarce por que alguno de los parametros es nulo");
        }
        
        return similitud;
        
    }
    
    
    public static void main(String[] args){
        Log log = new Log(true, true, Log.ANSI_PURPLE);
        
        WSDLExtractStructure wsdlEstructurado = new WSDLExtractStructure("http://www.xignite.com/xBATSLastSale.asmx?WSDL");
        
        List<ArbolWSDL> operacionesWSDL;
        operacionesWSDL = wsdlEstructurado.getArbolesDeOperaciones();
        
        Model primerrdf = operacionesWSDL.get(0).getRDFModel();
        
        for(ArbolWSDL arbol : operacionesWSDL){
            System.out.println("\nImprimiendo Arbol: " + arbol.getOperacion()+ " del servicio:" + arbol.getServicio());
            arbol.imprimirArbol();
            arbol.mostrarSerializacionRDF_xml();
            arbol.imprimirArbol();
            log.printLogMessage("Comapacion entre:" + operacionesWSDL.get(0).getOperacion() + " y " + arbol.getOperacion() +"[" + RDFMatcher.matchRDFModels(primerrdf, arbol.getRDFModel()) + "]");
            
        }
        
        ArbolWSDL superarbol[];
        
        superarbol = ArbolWSDL.generarSuperArboles(operacionesWSDL.get(0), operacionesWSDL.get(7));
        
        System.out.println("\n\n\n\nImprimiendo arboles originales");
        
        
        
        
        System.out.println("\n\n\n\nImprimiendo super arboles");
        operacionesWSDL.get(0).imprimirArbol();
        superarbol[0].imprimirArbol();
        
        operacionesWSDL.get(7).imprimirArbol();
        superarbol[1].imprimirArbol();
        
        
        List<Float> vectcaracA,vectcaracB;
        
        vectcaracA = superarbol[0].getVectorCaracteristico();
        superarbol[0].imprimirVectorCaracteristico();
        
        vectcaracB = superarbol[1].getVectorCaracteristico();
        superarbol[1].imprimirVectorCaracteristico();
        
        log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB) );
        
    }
    
}
