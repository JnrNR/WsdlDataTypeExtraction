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
    
    
    
    //Propiedades
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
        
    }
    
    public int getVersionWSDL(){
        return versionWSDL;
    }
    
    public List<ArbolWSDL> getArbolesDeOperaciones(){
        
        List<ArbolWSDL> arregloOperaciones = new ArrayList<ArbolWSDL>();
        
        switch(versionWSDL){
            case 1:
                //arregloOperaciones = getArbolesDeOperaciones_WSDL1();
                break;
            case 2:
                System.err.println("Operación no implementada");
                break;
            
            
            default:
                System.err.println("Versión WSDL no soportada");
                break;
        }
        
        return arregloOperaciones;
        
    }
    
    private void getArbolesDeOperaciones_WSDL1(){
        
        System.out.println("-> Extrallendo operaciones en archivo WSDL v1\n");
        
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
                            getOperaciones_WSDL1(nodoEnTurno);
                        }                        
                    }

                    
                }
        
    }
    
    private void getOperaciones_WSDL1(Node portTypeNode){
        NodeList operaciones = portTypeNode.getChildNodes();
        for(int noOperacion = 0; noOperacion<operaciones.getLength(); noOperacion++){
            
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
        WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
        objetoPruebas.getArbolesDeOperaciones();
        
        
    }
    
    
    
}
