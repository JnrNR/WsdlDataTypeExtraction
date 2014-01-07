//Author: Jorge Náder Roa

package jnr.datatypeextraction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import jnr.wsdltreestruct.ArbolWSDL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class WSDLExtractStructure {
    
    //Constantes
    private String ESPACIODENOMBRE_WSDL1 = "http://schemas.xmlsoap.org/wsdl/";
    //primer tag de WSDL 1 es definitions
    private String ESPACIODENOMBRE_WSDL2 = "http://www.w3.org/ns/wsdl";
    //primer tag de WSDL 2 es description
    
    
    //Parser DOM
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document documentoDOM = null;
    
    
    
    //Atributos
    private int versionWSDL;
    private List<ArbolWSDL> operaciones = new ArrayList<ArbolWSDL>();
    
    
    public WSDLExtractStructure(String WSDL_uri){
        
        try {
            
            dBuilder = dbFactory.newDocumentBuilder();
            documentoDOM = dBuilder.parse(WSDL_uri);
        
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(ParserWSDLwthDOM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Determinando la version de WSDL
            NodeList nodosMismoNivel = documentoDOM.getChildNodes();
            Element raiz = (Element)nodosMismoNivel.item(0);
            System.out.println("First tag:<"+ raiz.getTagName() +" xmlns:wsdl="+ raiz.getAttribute("xmlns:wsdl") +">");

            if(raiz.getAttribute("xmlns:wsdl").equalsIgnoreCase(ESPACIODENOMBRE_WSDL2) && raiz.getTagName().equalsIgnoreCase("wsdl:description")){
                versionWSDL = 2;
            }else if(raiz.getAttribute("xmlns:wsdl").equalsIgnoreCase(ESPACIODENOMBRE_WSDL1) && raiz.getTagName().equalsIgnoreCase("wsdl:definitions")){
                versionWSDL = 1;
            }
        //Obtención de los arboles de operaciones
            extraerArbolesDeOperaciones();
        
    }
    
    public int getVersionWSDL(){
        return versionWSDL;
    }
    
    public List<ArbolWSDL> getArbolesDeOperaciones(){
        return operaciones;
    }
    
    private void extraerArbolesDeOperaciones(){
        
        switch(versionWSDL){
            case 1:
                extraerArbolesDeOperaciones_WSDL1();
                break;
            case 2:
                System.err.println("Operación no implementada");
                break;
            
            
            default:
                System.err.println("Versión WSDL no soportada");
                break;
        }
        
    }
    
    private void extraerArbolesDeOperaciones_WSDL1(){
        
        System.out.println("-> Extrayendo operaciones en archivo WSDL v1\n");
        
        //Explorando el arbol DOM
        
            //Abriendo la raiz (definitions)
                NodeList nodosEnTurno = documentoDOM.getChildNodes();
                nodosEnTurno = nodosEnTurno.item(0).getChildNodes();
                
            //Extrayendo hijos de la raiz (types, message, portType, binding, service)
                System.out.println("Nodos Encontrados: " + nodosEnTurno.getLength());
                
                //Buscando el tag portType
                for(int noNodo = 0; noNodo<nodosEnTurno.getLength(); noNodo++){
                    Node nodoEnTurno = nodosEnTurno.item(noNodo);
                    if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                        System.out.println(noNodo+": "+((Element)nodoEnTurno).getTagName());
                        
                        //Identificando portType
                        if(((Element)nodoEnTurno).getTagName().equalsIgnoreCase("wsdl:portType")){
                            System.out.println("Index portType[" + noNodo + "]");
                            extraerOperaciones_WSDL1(null, null, nodoEnTurno);
                        }                        
                    }

                    
                }
        
    }
    
    private void extraerOperaciones_WSDL1(String servicio, String rutaDeOperacion, Node nodoAExaminar){//Recibe el nodo del PortType
        Node nodoEnTurno;
        NodeList nodos = nodoAExaminar.getChildNodes();
        //System.out.println("numero de elementos en portType:"+nodos.getLength());
        for(int noNodo = 0; noNodo<nodos.getLength(); noNodo++){
            nodoEnTurno = nodos.item(noNodo);
            
            if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                Element elemento = (Element)nodoEnTurno;
                System.out.println("Imprimiendo Nodo: "+ elemento.getTagName() + ">" + elemento.getNodeValue());
                
                //Formando las raices de los nodos
                    if(elemento.getTagName().equalsIgnoreCase("wsdl:operation")){
                        ElementoWSDL raiz = new ElementoWSDL(ElementoWSDL.ELEMENTO_OPERACION, elemento.getAttribute("name"), ElementoWSDL.TD_NULO);
                        ArbolWSDL arbol = new ArbolWSDL(raiz, ((Element)nodoAExaminar).getAttribute("name"));
                        operaciones.add(arbol);
                        extraerOperaciones_WSDL1(((Element)nodoAExaminar).getAttribute("name"), elemento.getAttribute("name"), nodoEnTurno);
                    }
                //Agregando los hijos directos del nodo operacion (wsdl:input y wsdl:output)
                    if(elemento.getTagName().equalsIgnoreCase("wsdl:input")){
                        int noOperacion;
                        for(noOperacion=0; noOperacion<operaciones.size();noOperacion++){
                            if(operaciones.get(noOperacion).getNombre().equals(rutaDeOperacion) && operaciones.get(noOperacion).getServicio().equals(servicio)){//Identificando la operacion para insertar correctamente los hijos
                                break;
                            }
                        }
                        ElementoWSDL input = new ElementoWSDL(ElementoWSDL.ELEMENTO_MENSAJE_IN, elemento.getAttribute("message"), ElementoWSDL.TD_NULO);
                        operaciones.get(noOperacion).insertarNodo(rutaDeOperacion, input);
                    }
                    if(elemento.getTagName().equalsIgnoreCase("wsdl:output")){
                        int noOperacion;
                        for(noOperacion=0; noOperacion<operaciones.size();noOperacion++){
                            if(operaciones.get(noOperacion).getNombre().equals(rutaDeOperacion) && operaciones.get(noOperacion).getServicio().equals(servicio)){//Identificando la operacion para insertar correctamente los hijos
                                break;
                            }
                        }
                        ElementoWSDL input = new ElementoWSDL(ElementoWSDL.ELEMENTO_MENSAJE_OUT, elemento.getAttribute("message"), ElementoWSDL.TD_NULO);
                        operaciones.get(noOperacion).insertarNodo(rutaDeOperacion, input);
                    }
            }
        }
        
    }
    
    protected Node getNodoDOM(String nombreDelTag, NodeList nodos) {
        for ( int x = 0; x < nodos.getLength(); x++ ) {
            Node node = nodos.item(x);
            if (node.getNodeName().equalsIgnoreCase(nombreDelTag)) {
                return node;
            }
        }
        return null;
    }
    
    
    public static void main(String[] args){
        //WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
        WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("http://www.xignite.com/xBATSLastSale.asmx?WSDL");//Direccion de Xmethod - http://www.xmethods.org/ve2/ViewListing.po;jsessionid=4g2EFxE845cgLvWrrPJxuswz?key=430207
        //Obtención de los arboles del documento parseado por WSDLEXtractStructure
            List<ArbolWSDL> estructuras;
            estructuras = objetoPruebas.getArbolesDeOperaciones();
            for(ArbolWSDL arbol:estructuras){
                System.out.println("\nImprimiendo Arbol: " + arbol.getNombre() + " del servicio:" + arbol.getServicio());
                arbol.imprimirArbol();
            }   
    }
    
}
