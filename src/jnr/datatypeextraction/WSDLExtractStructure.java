//Author: Jorge Náder Roa

package jnr.datatypeextraction;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
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
    private String ESPACIODENOMBRE_XMLS = "http://www.w3.org/2001/XMLSchema";
    //dirección del espacio de nombres de xml schema
    private String ESPACIODENOMBRE_WSDL1 = "http://schemas.xmlsoap.org/wsdl/";
    //primer tag de WSDL 1 es definitions
    private String ESPACIODENOMBRE_WSDL2 = "http://www.w3.org/ns/wsdl";
    //primer tag de WSDL 2 es description
    
    
    //Parser DOM
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document documentoDOM = null;
    
    //Parser tipos
    private XMLSchemaParser esquemaDeTipos;  
    
    //Atributos
    private int versionWSDL;
    private String prefijoXMLSchema;
    private String prefijoWSDL;
    
    private List<ArbolWSDL> operaciones = new ArrayList<ArbolWSDL>();
    
    
    public WSDLExtractStructure(String WSDL_uri){
        //Obtención del arbol DOM del documento
            try {

                dBuilder = dbFactory.newDocumentBuilder();
                documentoDOM = dBuilder.parse(WSDL_uri);

            } catch (SAXException | IOException | ParserConfigurationException ex) {
                Logger.getLogger(ParserWSDLwthDOM.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        //Extraccion de los tipos de dato del esquema
            WSDLParser parserWSDL = new WSDLParser();
            Definitions wsdl = parserWSDL.parse(WSDL_uri);
            esquemaDeTipos = new XMLSchemaParser(wsdl.getSchema(wsdl.getTargetNamespace()));     
        
        
        extraccionInformacionWSDL();
        
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
    
    
    /**
     * Este método extrae la información de relevancia referente al documento WSDL a parsear.<br>
     * <b>Operaciones:</b><br>
     * -Extrae los prefijos utilizados para los espacios de nombres de XMLSchema y WSDL<br>
     * -Identifica la version WSDL utilizada en el documento parseado.<br>
     */
    private void extraccionInformacionWSDL(){
        NodeList raiz = documentoDOM.getChildNodes();
        Node nodoRaiz = raiz.item(0);
        NamedNodeMap atributosRaiz = nodoRaiz.getAttributes();
        
        for(int noAtributo=0; noAtributo<atributosRaiz.getLength(); noAtributo++){
            
            
            //Se identifican los prefijos designados a los espacios de nombres WSDL y XML schema, con
            //la finalidad de hacer uso de ellos en la identificacion de elementos recuperados del arbol DOM
            
            //Extracción prefijo XML Schema
            if(atributosRaiz.item(noAtributo).getNodeValue().equalsIgnoreCase(ESPACIODENOMBRE_XMLS)){
                int inicioPrefijo = atributosRaiz.item(noAtributo).getNodeName().indexOf(":") + 1;
                prefijoXMLSchema = atributosRaiz.item(noAtributo).getNodeName().substring(inicioPrefijo);
            }
            
            //Extracción prefijo WSDL
            if(atributosRaiz.item(noAtributo).getNodeValue().equalsIgnoreCase(ESPACIODENOMBRE_WSDL1)){
                //Se detectó el uso de WSDL v1
                versionWSDL = 1;
                
                int inicioPrefijo = atributosRaiz.item(noAtributo).getNodeName().indexOf(":") + 1;
                prefijoWSDL = atributosRaiz.item(noAtributo).getNodeName().substring(inicioPrefijo);
            }else if(atributosRaiz.item(noAtributo).getNodeValue().equalsIgnoreCase(ESPACIODENOMBRE_WSDL2)){
                //Se detectó el uso de WSDL v2
                versionWSDL = 2;
                
                int inicioPrefijo = atributosRaiz.item(noAtributo).getNodeName().indexOf(":") + 1;
                prefijoWSDL = atributosRaiz.item(noAtributo).getNodeName().substring(inicioPrefijo);
            }
            
            
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
                
                //Buscando los tags portType
                for(int noNodo = 0; noNodo<nodosEnTurno.getLength(); noNodo++){
                    Node nodoEnTurno = nodosEnTurno.item(noNodo);
                    if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                        System.out.println(noNodo+": "+((Element)nodoEnTurno).getTagName());
                        
                        //Identificando tag portType
                        if(((Element)nodoEnTurno).getTagName().equalsIgnoreCase(prefijoWSDL + ":portType")){
                            System.out.println("Index portType[" + noNodo + "]");
                            extraerPortType_WSDL1(null, null, nodoEnTurno);
                        }                        
                    }   
                }
                
                //Buscando los tags message
                for(int noNodo = 0; noNodo<nodosEnTurno.getLength(); noNodo++){
                    Node nodoEnTurno = nodosEnTurno.item(noNodo);
                    if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                        System.out.println(noNodo+": "+((Element)nodoEnTurno).getTagName());
                        
                        //Identificando tag message
                        if(((Element)nodoEnTurno).getTagName().equalsIgnoreCase(prefijoWSDL + ":message")){
                            System.out.println("Index message[" + noNodo + "]");
                            extraerMessage_WSDL1(nodoEnTurno);
                        }                        
                    }   
                }
                
        
    }
    
    private void extraerMessage_WSDL1(Node nodoAExaminar){//Recibe el nodo del message
        Element elementoMessage;
        Node part;
        Element partEnTurno;
       
        String rutaInsercion = "";
        
        if(nodoAExaminar.getNodeType() == Node.ELEMENT_NODE){
            elementoMessage = (Element)nodoAExaminar;
            if(elementoMessage.getTagName().equalsIgnoreCase(prefijoWSDL + ":message")){
                String mensaje = elementoMessage.getAttribute("name");
                System.out.println("buscando operacion: " + mensaje);
                
                for(int noOperacion=0; noOperacion<operaciones.size(); noOperacion++){
                    System.out.println("Operacion:" + operaciones.get(noOperacion).getOperacion() + " MensajeIn:" + operaciones.get(noOperacion).getMensajeEntrada() + " MensajeOut:" + operaciones.get(noOperacion).getMensajeSalida());
                    if(mensaje.equals(operaciones.get(noOperacion).getMensajeEntrada())){
                        rutaInsercion = operaciones.get(noOperacion).getOperacion() + " " + operaciones.get(noOperacion).getMensajeEntrada();
                    }else if(mensaje.equals(operaciones.get(noOperacion).getMensajeSalida())){
                        rutaInsercion = operaciones.get(noOperacion).getOperacion() + " " + operaciones.get(noOperacion).getMensajeSalida();
                    }
                
                    //Extrayendo elementos del message
                        NodeList parts = nodoAExaminar.getChildNodes();
                        for(int noPart=0; noPart<parts.getLength(); noPart++){
                            part = parts.item(noPart);
                            if(part.getNodeType() == Node.ELEMENT_NODE){
                                partEnTurno = (Element)part;
                                String tipoDeDato;
                                
                                if(partEnTurno.getTagName().equalsIgnoreCase(prefijoWSDL + ":part")){
                                    ElementoWSDL nodo;
                                    
                                    if(partEnTurno.hasAttribute("type")){
                                        //Determinando si el tipo de dato es simple o complejo
                                        tipoDeDato = partEnTurno.getAttribute("type");
                                        if(tipoDeDato.startsWith(prefijoXMLSchema)){
                                        //Si el prefijo del espacio de nombres de XML schema, es un tipo de dato primitivo
                                            nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, partEnTurno.getAttribute("name"));
                                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                                            nodo.setTipoDeDato(tipoDeDato);
                                        }else{
                                        //Es un tipo de dato complejo.
                                            nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, partEnTurno.getAttribute("name"));
                                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO);
                                            nodo.setTipoDeDato(tipoDeDato);
                                        }

                                        //Insertando nodo
                                        operaciones.get(noOperacion).insertarNodo(rutaInsercion, nodo);System.out.println("Ruta de insercion:"+rutaInsercion);
                                        
                                    }else if(partEnTurno.hasAttribute("element")){
                                        //Determinando si el tipo de dato es simple o complejo
                                        tipoDeDato = partEnTurno.getAttribute("element");
                                        if(tipoDeDato.startsWith(prefijoXMLSchema)){
                                        //Si el prefijo del espacio de nombres de XML schema, es un tipo de dato primitivo
                                            nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, partEnTurno.getAttribute("name"));
                                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                                            nodo.setTipoDeDato(tipoDeDato);
                                        }else{
                                        //Es un tipo de dato complejo.
                                            nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, partEnTurno.getAttribute("name"));
                                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO);
                                            nodo.setTipoDeDato(tipoDeDato);
                                        }

                                        //Insertando nodo
                                        operaciones.get(noOperacion).insertarNodo(rutaInsercion, nodo);System.out.println("Ruta de insercion:"+rutaInsercion);
                                                                                
                                    }
                                }
                                
                            }
                        }
                
                }
                
            }
        }
        
    }
    
    private void extraerPortType_WSDL1(String servicio, String rutaDeOperacion, Node nodoAExaminar){//Recibe el nodo del PortType
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
                        ElementoWSDL raiz = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.OPERACION, elemento.getAttribute("name"));
                        ArbolWSDL arbol = new ArbolWSDL(raiz, ((Element)nodoAExaminar).getAttribute("name"));
                        operaciones.add(arbol);
                        extraerPortType_WSDL1(((Element)nodoAExaminar).getAttribute("name"), elemento.getAttribute("name"), nodoEnTurno);
                    }
                //Agregando los hijos directos del nodo operacion (wsdl:input y wsdl:output)
                    if(elemento.getTagName().equalsIgnoreCase(prefijoWSDL + ":input")){
                        int noOperacion;
                        for(noOperacion=0; noOperacion<operaciones.size();noOperacion++){
                            if(operaciones.get(noOperacion).getOperacion().equals(rutaDeOperacion) && operaciones.get(noOperacion).getServicio().equals(servicio)){//Identificando la operacion para insertar correctamente los hijos
                                break;
                            }
                        }
                        ElementoWSDL input = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_ENTRADA, elemento.getAttribute("message"));
                        operaciones.get(noOperacion).insertarNodo(rutaDeOperacion, input);
                    }
                    if(elemento.getTagName().equalsIgnoreCase(prefijoWSDL + ":output")){
                        int noOperacion;
                        for(noOperacion=0; noOperacion<operaciones.size();noOperacion++){
                            if(operaciones.get(noOperacion).getOperacion().equals(rutaDeOperacion) && operaciones.get(noOperacion).getServicio().equals(servicio)){//Identificando la operacion para insertar correctamente los hijos
                                break;
                            }
                        }
                        ElementoWSDL input = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_SALIDA, elemento.getAttribute("message"));
                        operaciones.get(noOperacion).insertarNodo(rutaDeOperacion, input);
                    }
            }
        }
        
    }
    
    
    private void extraerTipo(ElementoXMLSchema.TipoDeElementoXMLSchema tipoDeElemento, String nombreDeElemento, String rutaDeInsercion){
        ElementoXMLSchema elementoEsquema;
        
        if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO)){
            elementoEsquema = esquemaDeTipos.buscarTipoComplejo(nombreDeElemento);
            
        }
        
        if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_SIMPLE)){
            elementoEsquema = esquemaDeTipos.buscarTipoSimple(nombreDeElemento);
            
        }
        
        if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.ELEMENTO)){
            elementoEsquema = esquemaDeTipos.buscarElemento(nombreDeElemento);
            
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
                System.out.println("\nImprimiendo Arbol: " + arbol.getOperacion()+ " del servicio:" + arbol.getServicio());
                arbol.imprimirArbol();
            }   
    }
    
}
