
package jnr.operationsmatcher;

import com.hp.hpl.jena.rdf.model.Model;
import java.util.ArrayList;
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
        //comparador.matchWSDLDirectory("D:\\CINVESTAV\\Tesis\\servicios\\precisionREcall");//Para Windows 
        //comparador.getOperacionesDirectorio("D:\\CINVESTAV\\Tesis\\servicios\\precisionREcall\\patron");//Para Windows 
        //comparador.matchWSDLDirectory("/Users/JNR/CINVESTAV/Tesis/servicios/pruebas2");//Para Mac
        comparador.matchWSDLAndDirectory("D:\\CINVESTAV\\Tesis\\servicios\\precisionRecall\\patron\\1personbicycle4wheeledcar_price_service.wsdl", "D:\\CINVESTAV\\Tesis\\servicios\\precisionRecall\\repositorio");
        //comparador.matchWSDLDirectory_URL("http://148.247.102.37:8080/wsdlcomparison/servicios/precisionRecall/repositorio/");
        //comparador.getRDFsFromDirectory("D:\\CINVESTAV\\Tesis\\servicios\\precisionRecall\\repositorio", ".");
    }
    
    public List<MatcherResult> matchWSDLDirectory(String url){
        List<MatcherResult> resultados = new ArrayList<MatcherResult>();

        Directorio directorio = new Directorio();
        directorio.buscarElementosDirectorio(url);
        int noArchivos = directorio.getFicheros().size();

        int contador=0;
        //Determinando las comparativas a realizar por medio de una matriz triangular inferiror.
        for(int columna=0; columna<noArchivos-1; columna++ ){// Control de columnas
            
            //Extraccion de operaciones para el servicio A
                //WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"\\"+directorio.getFicheros().get(columna));//Para Windows
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"/"+directorio.getFicheros().get(columna));//Para Mac
                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                }*/
                contador=1;
            
            for(int renglon=columna+1; renglon<noArchivos; renglon++){//Control de renglones
                //log.printLogMessage("COMPARANDO SERVICIOS:\t" + directorio.getFicheros().get(columna) + "\t" + directorio.getFicheros().get(renglon) + "\n\n");//DEPURACION
                
                //WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url+"\\"+directorio.getFicheros().get(renglon));//Para Windows
                WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url+"/"+directorio.getFicheros().get(renglon));//Para Mac
                
                List<ArbolWSDL> operacionesWSDLservicioB;
                operacionesWSDLservicioB = wsdlEstructuradoB.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                    arbolB.ordenar();
                }*/
                
                //Comparando las operaciones del servicio A con las operaciones del servicio B
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    TOTAL_OPERACIONES++;
                    
                    for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                        //log.printLogMessage("COMPARANDO OPERACIONES:" + arbolA.getServicio() + "[" + arbolA.getPeso() + "]" + arbolB.getServicio() + "[" + arbolB.getPeso() + "]");//DEPURACION
                        
                        contador++;
                        
                        arbolA.ordenar();
                        arbolB.ordenar();
                        //Comparando operacionA en turno con operacion B en turno
                        ArbolWSDL superarbol[], superarbolEntrada[], superarbolSalida[];
                        superarbol = ArbolWSDL.generarSuperArboles(arbolA, arbolB);
                        
                        List<Float> vectcaracA,vectcaracB, vectcaracAEntrada, vectcaracBEntrada, vectcaracASalida, vectcaracBSalida; 
                        float correlacionAB, correlacionABEntrada, correlacionABSalida;
                        vectcaracA = superarbol[0].getVectorCaracteristico();
                        vectcaracB = superarbol[1].getVectorCaracteristico();
                        
                        
                        arbolA.ordenarPorMensajes();
                        arbolB.ordenarPorMensajes();
                        superarbolEntrada = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 1);
                        superarbolSalida = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 2);
                        vectcaracAEntrada = superarbolEntrada[0].getVectorCaracteristico();
                        vectcaracBEntrada = superarbolEntrada[1].getVectorCaracteristico();
                        vectcaracASalida = superarbolSalida[0].getVectorCaracteristico();
                        vectcaracBSalida = superarbolSalida[1].getVectorCaracteristico();
                        
                        correlacionAB = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB);
                        correlacionABEntrada = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracAEntrada, vectcaracBEntrada);
                        correlacionABSalida = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracASalida, vectcaracBSalida);
                        //log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + correlacionAB );//DEPURACION
                        
                        
                        String linkGoogleChartA, linkGoogleChartB;
                        linkGoogleChartA = arbolA.makeDotGoogleChartLink(false);
                        linkGoogleChartB =  arbolB.makeDotGoogleChartLink(false);
                        

                        if(correlacionAB < 0.75){
                            log.printLogMessage("<"+contador + ">");
                            log.printLogMessage("WSDL_A:" + directorio.getFicheros().get(columna));
                            log.printLogMessage("WSDL_B:" + directorio.getFicheros().get(renglon));
                            log.printLogMessage("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]");
                            log.printLogMessage("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]");
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                            
                        }else{
                            Log.println("<"+contador + ">", Log.ANSI_GREEN);
                            Log.println("WSDL_A:" + directorio.getFicheros().get(columna), Log.ANSI_GREEN);
                            Log.println("WSDL_B:" + directorio.getFicheros().get(renglon), Log.ANSI_GREEN);
                            Log.println("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]", Log.ANSI_GREEN);
                            Log.println("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]", Log.ANSI_GREEN);
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                        }
                        
                        
                    }
                }
                
                
            }
        }
        
        return resultados;
    }
    
    
    
     public void getRDFsFromDirectory(String url, String RDFstorageUrl){

        Directorio directorio = new Directorio();
        directorio.buscarElementosDirectorio(url);
        int noArchivos = directorio.getFicheros().size();

        //Determinando las comparativas a realizar por medio de una matriz triangular inferiror.
        for(int wsdl=0; wsdl<noArchivos; wsdl++ ){// Control de columnas
            
            //Extraccion de operaciones por servicio
                //WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"\\"+directorio.getFicheros().get(wsdl));//Para Windows
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"/"+directorio.getFicheros().get(wsdl));//Para Mac
                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                    arbolA.alamcenarSerializacionRDF_xml(RDFstorageUrl);
                }            

        }
    }
    
    
    /*********************************************************************************PARA WEB
     */
    public List<MatcherResult> matchWSDLOnetoOne_URL(String url_WSDL_A, String url_WSDL_B){
        List<MatcherResult> resultados = new ArrayList<MatcherResult>();

            
            //Extraccion de operaciones para el servicio A
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url_WSDL_A);

                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                }*/

         //Extraccion de operaciones para el servicio A
                WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url_WSDL_B);
                
                List<ArbolWSDL> operacionesWSDLservicioB;
                operacionesWSDLservicioB = wsdlEstructuradoB.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                    arbolB.ordenar();
                }*/
                
                
                int contador = 0;
                //Comparando las operaciones del servicio A con las operaciones del servicio B
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    TOTAL_OPERACIONES++;
                    
                    for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                        //log.printLogMessage("COMPARANDO OPERACIONES:" + arbolA.getServicio() + "[" + arbolA.getPeso() + "]" + arbolB.getServicio() + "[" + arbolB.getPeso() + "]");//DEPURACION
                        
                        contador++;
                        
                        arbolA.ordenar();
                        arbolB.ordenar();
                        //Comparando operacionA en turno con operacion B en turno
                        ArbolWSDL superarbol[], superarbolEntrada[], superarbolSalida[];
                        superarbol = ArbolWSDL.generarSuperArboles(arbolA, arbolB);
                        
                        List<Float> vectcaracA,vectcaracB, vectcaracAEntrada, vectcaracBEntrada, vectcaracASalida, vectcaracBSalida; 
                        float correlacionAB, correlacionABEntrada, correlacionABSalida;
                        vectcaracA = superarbol[0].getVectorCaracteristico();
                        vectcaracB = superarbol[1].getVectorCaracteristico();
                        
                        
                        arbolA.ordenarPorMensajes();
                        arbolB.ordenarPorMensajes();
                        superarbolEntrada = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 1);
                        superarbolSalida = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 2);
                        vectcaracAEntrada = superarbolEntrada[0].getVectorCaracteristico();
                        vectcaracBEntrada = superarbolEntrada[1].getVectorCaracteristico();
                        vectcaracASalida = superarbolSalida[0].getVectorCaracteristico();
                        vectcaracBSalida = superarbolSalida[1].getVectorCaracteristico();
                        
                        correlacionAB = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB);
                        correlacionABEntrada = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracAEntrada, vectcaracBEntrada);
                        correlacionABSalida = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracASalida, vectcaracBSalida);
                        //log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + correlacionAB );//DEPURACION
                        
                        
                        String linkGoogleChartA, linkGoogleChartB;
                        linkGoogleChartA = arbolA.makeDotGoogleChartLink(false);
                        linkGoogleChartB =  arbolB.makeDotGoogleChartLink(false);
                        

                        if(correlacionAB < 0.75){
                            log.printLogMessage("<"+contador + ">");
                            log.printLogMessage("WSDL_A:" + url_WSDL_A);
                            log.printLogMessage("WSDL_B:" + url_WSDL_B);
                            log.printLogMessage("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]");
                            log.printLogMessage("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]");
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                            
                        }else{
                            Log.println("<"+contador + ">", Log.ANSI_GREEN);
                            Log.println("WSDL_A:" + url_WSDL_A, Log.ANSI_GREEN);
                            Log.println("WSDL_B:" + url_WSDL_B, Log.ANSI_GREEN);
                            Log.println("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]", Log.ANSI_GREEN);
                            Log.println("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]", Log.ANSI_GREEN);
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                        }
                        
                    }
                }
        
        return resultados;
        
    }
    
    
    
    public List<MatcherResult> matchWSDLAndDirectory_URL(String url_WSDL, String url_Repository){
        List<MatcherResult> resultados = new ArrayList<MatcherResult>();
        
        List ficheros;
        
        Directorio directorio = new Directorio();
        ficheros = directorio.getNombresFicheroURL(url_Repository, "wsdl");
        int noArchivos = ficheros.size();
        
        
        int contador=0;

            
            //Extraccion de operaciones para el servicio A
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url_WSDL);//Para Windows

                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                }*/
                contador=1;
            
            for(int i=0; i<noArchivos; i++){//Control de renglones
                //log.printLogMessage("COMPARANDO SERVICIOS:\t" + directorio.getFicheros().get(columna) + "\t" + directorio.getFicheros().get(renglon) + "\n\n");//DEPURACION
                
                WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url_Repository+"/"+ficheros.get(i));//Para Windows
                //WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url_Repository+"/"+directorio.getFicheros().get(i));//Para Mac
                
                List<ArbolWSDL> operacionesWSDLservicioB;
                operacionesWSDLservicioB = wsdlEstructuradoB.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                    arbolB.ordenar();
                }*/
                
                //Comparando las operaciones del servicio A con las operaciones del servicio B
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    TOTAL_OPERACIONES++;
                    
                    for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                        //log.printLogMessage("COMPARANDO OPERACIONES:" + arbolA.getServicio() + "[" + arbolA.getPeso() + "]" + arbolB.getServicio() + "[" + arbolB.getPeso() + "]");//DEPURACION
                        
                        contador++;
                        
                        arbolA.ordenar();
                        arbolB.ordenar();
                        //Comparando operacionA en turno con operacion B en turno
                        ArbolWSDL superarbol[], superarbolEntrada[], superarbolSalida[];
                        superarbol = ArbolWSDL.generarSuperArboles(arbolA, arbolB);
                        
                        List<Float> vectcaracA,vectcaracB, vectcaracAEntrada, vectcaracBEntrada, vectcaracASalida, vectcaracBSalida; 
                        float correlacionAB, correlacionABEntrada, correlacionABSalida;
                        vectcaracA = superarbol[0].getVectorCaracteristico();
                        vectcaracB = superarbol[1].getVectorCaracteristico();
                        
                        
                        arbolA.ordenarPorMensajes();
                        arbolB.ordenarPorMensajes();
                        superarbolEntrada = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 1);
                        superarbolSalida = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 2);
                        vectcaracAEntrada = superarbolEntrada[0].getVectorCaracteristico();
                        vectcaracBEntrada = superarbolEntrada[1].getVectorCaracteristico();
                        vectcaracASalida = superarbolSalida[0].getVectorCaracteristico();
                        vectcaracBSalida = superarbolSalida[1].getVectorCaracteristico();
                        
                        correlacionAB = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB);
                        correlacionABEntrada = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracAEntrada, vectcaracBEntrada);
                        correlacionABSalida = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracASalida, vectcaracBSalida);
                        //log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + correlacionAB );//DEPURACION
                        
                        
                        String linkGoogleChartA, linkGoogleChartB;
                        linkGoogleChartA = arbolA.makeDotGoogleChartLink(false);
                        linkGoogleChartB =  arbolB.makeDotGoogleChartLink(false);
                        

                        if(correlacionAB < 0.75){
                            log.printLogMessage("<"+contador + ">");
                            log.printLogMessage("WSDL_A:" + url_WSDL);
                            log.printLogMessage("WSDL_B:" + ficheros.get(i));
                            log.printLogMessage("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]");
                            log.printLogMessage("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]");
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                            
                        }else{
                            Log.println("<"+contador + ">", Log.ANSI_GREEN);
                            Log.println("WSDL_A:" + url_WSDL, Log.ANSI_GREEN);
                            Log.println("WSDL_B:" + ficheros.get(i), Log.ANSI_GREEN);
                            Log.println("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]", Log.ANSI_GREEN);
                            Log.println("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]", Log.ANSI_GREEN);
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                        }
                        
                    }
                }
                
                
            }
        
        return resultados;
        
    }
        
    
        
    
    public List<MatcherResult> matchWSDLDirectory_URL(String url){
        List<MatcherResult> resultados = new ArrayList<MatcherResult>();
        
        List ficheros;
        
        Directorio directorio = new Directorio();
        ficheros = directorio.getNombresFicheroURL(url, "wsdl");
        int noArchivos = ficheros.size();
        
        
        int contador=0;
        //Determinando las comparativas a realizar por medio de una matriz triangular inferiror.
        for(int columna=0; columna<noArchivos-1; columna++ ){// Control de columnas
            
            //Extraccion de operaciones para el servicio A
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"/"+ficheros.get(columna));//Para Windows
                //WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"/"+directorio.getFicheros().get(columna));//Para Mac
                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                }*/
                contador=1;
            
            for(int renglon=columna+1; renglon<noArchivos; renglon++){//Control de renglones
                //log.printLogMessage("COMPARANDO SERVICIOS:\t" + directorio.getFicheros().get(columna) + "\t" + directorio.getFicheros().get(renglon) + "\n\n");//DEPURACION
                
                WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url+"/"+ficheros.get(renglon));//Para Windows
                //WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(url+"/"+directorio.getFicheros().get(renglon));//Para Mac
                
                List<ArbolWSDL> operacionesWSDLservicioB;
                operacionesWSDLservicioB = wsdlEstructuradoB.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                /*for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                    arbolB.ordenar();
                }*/
                
                //Comparando las operaciones del servicio A con las operaciones del servicio B
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    TOTAL_OPERACIONES++;
                    
                    for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                        //log.printLogMessage("COMPARANDO OPERACIONES:" + arbolA.getServicio() + "[" + arbolA.getPeso() + "]" + arbolB.getServicio() + "[" + arbolB.getPeso() + "]");//DEPURACION
                        
                        contador++;
                        
                        arbolA.ordenar();
                        arbolB.ordenar();
                        //Comparando operacionA en turno con operacion B en turno
                        ArbolWSDL superarbol[], superarbolEntrada[], superarbolSalida[];
                        superarbol = ArbolWSDL.generarSuperArboles(arbolA, arbolB);
                        
                        List<Float> vectcaracA,vectcaracB, vectcaracAEntrada, vectcaracBEntrada, vectcaracASalida, vectcaracBSalida; 
                        float correlacionAB, correlacionABEntrada, correlacionABSalida;
                        vectcaracA = superarbol[0].getVectorCaracteristico();
                        vectcaracB = superarbol[1].getVectorCaracteristico();
                        
                        
                        arbolA.ordenarPorMensajes();
                        arbolB.ordenarPorMensajes();
                        superarbolEntrada = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 1);
                        superarbolSalida = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 2);
                        vectcaracAEntrada = superarbolEntrada[0].getVectorCaracteristico();
                        vectcaracBEntrada = superarbolEntrada[1].getVectorCaracteristico();
                        vectcaracASalida = superarbolSalida[0].getVectorCaracteristico();
                        vectcaracBSalida = superarbolSalida[1].getVectorCaracteristico();
                        
                        correlacionAB = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB);
                        correlacionABEntrada = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracAEntrada, vectcaracBEntrada);
                        correlacionABSalida = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracASalida, vectcaracBSalida);
                        //log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + correlacionAB );//DEPURACION                        
                        
                        String linkGoogleChartA, linkGoogleChartB;
                        linkGoogleChartA = arbolA.makeDotGoogleChartLink(false);
                        linkGoogleChartB =  arbolB.makeDotGoogleChartLink(false);                        

                        if(correlacionAB < 0.75){
                            log.printLogMessage("<"+contador + ">");
                            log.printLogMessage("WSDL_A:" + ficheros.get(columna));
                            log.printLogMessage("WSDL_B:" + ficheros.get(renglon));
                            log.printLogMessage("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]");
                            log.printLogMessage("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]");
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                            
                        }else{
                            Log.println("<"+contador + ">", Log.ANSI_GREEN);
                            Log.println("WSDL_A:" + ficheros.get(columna), Log.ANSI_GREEN);
                            Log.println("WSDL_B:" + ficheros.get(renglon), Log.ANSI_GREEN);
                            Log.println("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]", Log.ANSI_GREEN);
                            Log.println("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]", Log.ANSI_GREEN);
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            //Agregando nodo
                            resultados.add(new MatcherResult(arbolA.getServicio(), arbolB.getServicio(), arbolA.getOperacion(), arbolB.getOperacion(), linkGoogleChartA, linkGoogleChartB, correlacionAB, correlacionABEntrada, correlacionABSalida));
                        }
                        
                    }
                }
                
                
            }
        }
        
        return resultados;
        
    }
    
    /*********************************************************************************
     */
    
    
    
    
    
    
    public void matchWSDLAndDirectory(String urlWSDL, String urlDirectory){
        //Obtencion nombres de los archivos WSDL contenidos en el directorio
        Directorio directorio = new Directorio();
        directorio.buscarElementosDirectorio(urlDirectory);
        
        //Extraccion de las operaciones del WSDL
        WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(urlWSDL);
        List<ArbolWSDL> operacionesWSDLservicioA;
        operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();

        //Ordenando los arboles de las operaciones del WSDL proporcionado
        for(ArbolWSDL arbolA : operacionesWSDLservicioA){
            arbolA.ordenar();
        }
        
        int contador=0;
        //Comparativa
        for(String archivoDirectorio : directorio.getFicheros()){
            
            //Extraccion de las operaciones del WSDL
            WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(urlDirectory+"\\"+archivoDirectorio);//Para Windows
            //WSDLExtractStructure wsdlEstructuradoB = new WSDLExtractStructure(urlDirectory+"/"+archivoDirectorio);//Para Mac
            
            List<ArbolWSDL> operacionesWSDLservicioB;
            operacionesWSDLservicioB = wsdlEstructuradoB.getArbolesDeOperaciones();

            
            //Ordenanacion y comparativa de las operaciones del directorio con las operaciones del WSDL proporcionado
            for(ArbolWSDL arbolB : operacionesWSDLservicioB){
                arbolB.ordenar();
                //comparativa
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                        arbolA.ordenar();
                        //Comparando operacionA en turno con operacion B en turno
                        ArbolWSDL superarbol[], superarbolEntrada[], superarbolSalida[];
                        superarbol = ArbolWSDL.generarSuperArboles(arbolA, arbolB);

                        List<Float> vectcaracA,vectcaracB, vectcaracAEntrada, vectcaracBEntrada, vectcaracASalida, vectcaracBSalida; 
                        float correlacionAB, correlacionABEntrada, correlacionABSalida;
                        vectcaracA = superarbol[0].getVectorCaracteristico();
                        vectcaracB = superarbol[1].getVectorCaracteristico();
                        
                        arbolA.ordenarPorMensajes();
                        arbolB.ordenarPorMensajes();
                        superarbolEntrada = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 1);
                        superarbolSalida = ArbolWSDL.generarSuperArboles(arbolA, arbolB, 2);
                        vectcaracAEntrada = superarbolEntrada[0].getVectorCaracteristico();
                        vectcaracBEntrada = superarbolEntrada[1].getVectorCaracteristico();
                        vectcaracASalida = superarbolSalida[0].getVectorCaracteristico();
                        vectcaracBSalida = superarbolSalida[1].getVectorCaracteristico();
                        
                        correlacionAB = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracA, vectcaracB);
                        correlacionABEntrada = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracAEntrada, vectcaracBEntrada);
                        correlacionABSalida = ArbolWSDL.calcularFactorDeCorrelacion(vectcaracASalida, vectcaracBSalida);
                    
                        //log.printLogMessage("El factor de correlacion para los vectores caracteristicos es de: " + correlacionAB );//DEPURACION
                        contador++;
                        if(correlacionAB < 0.75){
                            log.printLogMessage("<"+contador + ">");
                            log.printLogMessage("WSDL_A:" + urlWSDL);
                            log.printLogMessage("WSDL_B:" + archivoDirectorio);
                            log.printLogMessage("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]");
                            log.printLogMessage("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]");
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                            
                        }else{
                            Log.println("<"+contador + ">", Log.ANSI_GREEN);
                            Log.println("WSDL_A:" + urlWSDL, Log.ANSI_GREEN);
                            Log.println("WSDL_B:" + archivoDirectorio, Log.ANSI_GREEN);
                            Log.println("       Servicio_A:"+arbolA.getServicio()+" Operacion_A:"+arbolA.getOperacion()+" ["+arbolA.getPeso()+"]", Log.ANSI_GREEN);
                            Log.println("       Servicio_B:"+arbolB.getServicio()+" Operacion_B:"+arbolB.getOperacion()+" ["+arbolB.getPeso()+"]", Log.ANSI_GREEN);
                            log.printLogMessage("       Correlacion AB:<<<"+correlacionAB+">>>\tCorrelacion Entradas:<<<"+correlacionABEntrada+">>>\tCorrelacion Salidas:<<<"+correlacionABSalida+">>>");
                        }
                }
            }
        
        }
        

    }
    
    
    public void getOperacionesDirectorio(String url){
        Directorio directorio = new Directorio();
        directorio.buscarElementosDirectorio(url);

        int i=0;
        for(String archivo : directorio.getFicheros()){
            //Extraccion de operaciones para el servicio A
                WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"\\"+archivo);//Para Windows
                //WSDLExtractStructure wsdlEstructuradoA = new WSDLExtractStructure(url+"/"+directorio.getFicheros().get(columna));//Para Mac
                List<ArbolWSDL> operacionesWSDLservicioA;
                operacionesWSDLservicioA = wsdlEstructuradoA.getArbolesDeOperaciones();
                
                //Ordenando los arboles de las operaciones
                for(ArbolWSDL arbolA : operacionesWSDLservicioA){
                    arbolA.ordenar();
                    i++;
                    System.out.println("\n["+i+"]");
                    System.out.println("WSDL:" + archivo);
                    System.out.println("Servicio:" + arbolA.getServicio() + " Operacion:" + arbolA.getOperacion());
                    arbolA.imprimirArbol();
                    arbolA.ordenar();
                    arbolA.makeDotFile("D:\\CINVESTAV\\Tesis\\servicios\\precisionRecall\\grafosDotRepositorio\\");
                    arbolA.saveTreeImage("D:\\CINVESTAV\\Tesis\\servicios\\precisionRecall\\grafosRepositorio\\");
                }           
                
        }
    
    }
    
}
