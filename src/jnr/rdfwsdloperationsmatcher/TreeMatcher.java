
package jnr.rdfwsdloperationsmatcher;

import com.hp.hpl.jena.rdf.model.Model;
import java.util.List;
import jnr.datatypeextraction.WSDLExtractStructure;
import jnr.utilities.Directorio;
import jnr.utilities.Log;
import jnr.utilities.PrecisionRecall;
import jnr.wsdltreestruct.ArbolWSDL;

/**
 *
 * @author Jorge Náder Roa
 */
public class TreeMatcher {
    //Depuración
    public Log log = new Log(true, true, Log.ANSI_PURPLE);
    /////////////////////////////////////////////////
    //Métrica
    private PrecisionRecall metrica = new PrecisionRecall();
    private int RELEVANCIA_NONODOS = 15;
    private int TOTAL_OPERACIONES = 0;
    private float UMBRAL = (float) 0.75;
    /////////////////////////////////////////////////
    
    private Model rdf1;
    private Model rdf2;
    
    public TreeMatcher(){}
    
    public TreeMatcher(Model rdf1, Model rdf2){
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
        /*
        WSDLExtractStructure wsdlEstructurado = new WSDLExtractStructure("http://www.xignite.com/xBATSLastSale.asmx?WSDL");
        
        List<ArbolWSDL> operacionesWSDL;
        operacionesWSDL = wsdlEstructurado.getArbolesDeOperaciones();
        
        Model primerrdf = operacionesWSDL.get(0).getRDFModel();
        
        for(ArbolWSDL arbol : operacionesWSDL){
            System.out.println("\nImprimiendo Arbol: " + arbol.getOperacion()+ " del servicio:" + arbol.getServicio());
            arbol.imprimirArbol();
            arbol.mostrarSerializacionRDF_xml();
            arbol.imprimirArbol();
            log.printLogMessage("Comapacion entre:" + operacionesWSDL.get(0).getOperacion() + " y " + arbol.getOperacion() +"[" + TreeMatcher.matchRDFModels(primerrdf, arbol.getRDFModel()) + "]");
            
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
        */
        
        TreeMatcher comparador = new TreeMatcher();
        comparador.matchWSDLDirectory("D:\\CINVESTAV\\Tesis\\servicios\\pruebas2");
    }
    
    public void matchWSDLDirectory(String url){

        Directorio directorio = new Directorio();
        directorio.buscarElementosDirectorio(url);
        int noArchivos = directorio.getFicheros().size();

        
        //Determinando las comparativas a realizar por medio de una matriz triangular inferiror.
        for(int columna=0; columna<noArchivos-1; columna++ ){// Control de columnas
            
            //Extraccion de operaciones para el servicio A
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"\\"+directorio.getFicheros().get(columna));
                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                }
                
            
            for(int renglon=columna+1; renglon<noArchivos; renglon++){//Control de renglones
                log.printLogMessage("COMPARANDO SERVICIOS:\t" + directorio.getFicheros().get(columna) + "\t" + directorio.getFicheros().get(renglon) + "\n\n");
                
                WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url+"\\"+directorio.getFicheros().get(renglon));
                List<ArbolWSDL> operacionesWSDLservicioB;
                operacionesWSDLservicioB = wsdlEstructuradoB.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                    arbolB.ordenar();
                }
                
                //Comparando las operaciones del servicio A con las operaciones del servicio B
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    TOTAL_OPERACIONES++;
                    
                    for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                        log.printLogMessage("COMPARANDO OPERACIONES:" + arbolA.getServicio() + "[" + arbolA.getPeso() + "]" + arbolB.getServicio() + "[" + arbolB.getPeso() + "]");
                        
                        //Mediciones precision & recall
                            if(arbolA.getPeso()==RELEVANCIA_NONODOS){
                                metrica.incrementRegistrosRelevantes();
                            }
                            if(arbolB.getPeso()==RELEVANCIA_NONODOS){
                                metrica.incrementRegistrosRelevantes();
                            }
                            
                        ////////////////////////////////////////////////////////
                        
                        
                        //Comparando operacionA en turno con operacion B en turno
                        ArbolWSDL superarbol[];
                        superarbol = ArbolWSDL.generarSuperArboles(arbolA, arbolB);
                        List<Float> vectcaracA,vectcaracB; 
                        float correlacionAB;
                        vectcaracA = superarbol[0].getVectorCaracteristico();
                        vectcaracB = superarbol[1].getVectorCaracteristico();
                        correlacionAB = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB);
                        log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + correlacionAB );
                        
                        
                        //Mediciones precision & recall
                            if(correlacionAB >= UMBRAL){
                                if(arbolA.getPeso()==RELEVANCIA_NONODOS){
                                    metrica.incrementA();
                                }else{
                                    metrica.incrementC();
                                }
                                if(arbolB.getPeso()==RELEVANCIA_NONODOS){
                                    metrica.incrementA();
                                }else{
                                    metrica.incrementC();
                                }
                            }
                        ////////////////////////////////////////////////////////
                        
                        
                    }
                }
                
                
            }
        }
        
        //Mediciones precision & recall
            metrica.setNoRegistros(TOTAL_OPERACIONES);
            log.printLogMessage("TOTAL DE OPERACIONES:" + TOTAL_OPERACIONES + " REGISTROS RELEVANTES:"+metrica.getRegistrosRelevantes());
            log.printLogMessage("Valores metrica  A:" +metrica.getA()+ " B:" +metrica.getB()+ " C:" +metrica.getC());
            log.printLogMessage("<<<<<<<<<<PRECISION:" + metrica.getPrecision() + " RECALL:" + metrica.getRecall() + ">>>>>>>>>>");
        ////////////////////////////////////////////////////////
    }
    
}
