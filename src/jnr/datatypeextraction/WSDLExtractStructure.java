
package jnr.datatypeextraction;

import com.predic8.schema.ComplexContent;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Derivation;
import com.predic8.schema.Extension;
import com.predic8.schema.ModelGroup;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.SimpleContent;
import com.predic8.schema.SimpleType;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import groovy.xml.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import jnr.utilities.Directorio;
import jnr.utilities.Log;
import jnr.wsdltreestruct.ArbolWSDL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * 
 * @author Jorge Náder Roa
 */
public class WSDLExtractStructure {
    //Depuración
    public Log log = new Log(true, true, Log.ANSI_BLUE);
    /////////////////////////////////////////////////
    
    
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
    private Schema esquemaWSDL;
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
            esquemaWSDL = wsdl.getSchema(wsdl.getTargetNamespace());
            if(esquemaWSDL!=null){
                esquemaDeTipos = new XMLSchemaParser(esquemaWSDL);
            }else{
                esquemaDeTipos = new XMLSchemaParser();
            }
        
        
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
                log.printLogErrorMessage("Operación no implementada");//DEPURACION
                break;
            
            
            default:
                log.printLogErrorMessage("Versión WSDL no soportada");//DEPURACION
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
        
        log.printLogMessage("-> Extrayendo operaciones en archivo WSDL v1\n");//DEPURACION
        
        //Explorando el arbol DOM
        
            //Abriendo la raiz (definitions)
                NodeList nodosEnTurno = documentoDOM.getChildNodes();
                nodosEnTurno = nodosEnTurno.item(0).getChildNodes();
                
            //Extrayendo hijos de la raiz (types, message, portType, binding, service)
                log.printLogMessage("Nodos Encontrados: " + nodosEnTurno.getLength());//DEPURACION
                
                //Buscando los tags portType
                for(int noNodo = 0; noNodo<nodosEnTurno.getLength(); noNodo++){
                    Node nodoEnTurno = nodosEnTurno.item(noNodo);
                    if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                        log.printLogMessage(noNodo+": "+((Element)nodoEnTurno).getTagName());//DEPURACION
                        
                        //Identificando tag portType
                        if(((Element)nodoEnTurno).getTagName().equalsIgnoreCase(prefijoWSDL + ":portType") || ((Element)nodoEnTurno).getTagName().equalsIgnoreCase("portType")){
                            log.printLogMessage("Index portType[" + noNodo + "]");//DEPURACION
                            extraerPortType_WSDL1(null, null, nodoEnTurno);
                        }                        
                    }   
                }
                
                //Buscando los tags message
                for(int noNodo = 0; noNodo<nodosEnTurno.getLength(); noNodo++){
                    Node nodoEnTurno = nodosEnTurno.item(noNodo);
                    if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                        log.printLogMessage(noNodo+": "+((Element)nodoEnTurno).getTagName());//DEPURACION
                        
                        //Identificando tag message
                        if(((Element)nodoEnTurno).getTagName().equalsIgnoreCase(prefijoWSDL + ":message") || ((Element)nodoEnTurno).getTagName().equalsIgnoreCase("message")){
                            log.printLogMessage("Index message[" + noNodo + "]");//DEPURACION
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
            if(elementoMessage.getTagName().equalsIgnoreCase(prefijoWSDL + ":message") || elementoMessage.getTagName().equalsIgnoreCase("message")){
                String mensaje = elementoMessage.getAttribute("name");
                log.printLogMessage("buscando operacion: " + mensaje);//DEPURACION
                
                for(int noOperacion=0; noOperacion<operaciones.size(); noOperacion++){
                    log.printLogMessage("Operacion:" + operaciones.get(noOperacion).getOperacion() + " MensajeIn:" + operaciones.get(noOperacion).getMensajeEntrada() + " MensajeOut:" + operaciones.get(noOperacion).getMensajeSalida());//DEPURACION
                    if(mensaje.equals(operaciones.get(noOperacion).getMensajeEntrada())){
                        rutaInsercion = operaciones.get(noOperacion).getOperacion() + " " + operaciones.get(noOperacion).getMensajeEntrada();
                    }else if(mensaje.equals(operaciones.get(noOperacion).getMensajeSalida())){
                        rutaInsercion = operaciones.get(noOperacion).getOperacion() + " " + operaciones.get(noOperacion).getMensajeSalida();
                    }else{
                        continue;
                    }
                
                    //Extrayendo elementos del message
                        NodeList parts = nodoAExaminar.getChildNodes();
                        for(int noPart=0; noPart<parts.getLength(); noPart++){
                            part = parts.item(noPart);
                            if(part.getNodeType() == Node.ELEMENT_NODE){
                                partEnTurno = (Element)part;
                                String tipoDeDato;
                                
                                if(partEnTurno.getTagName().equalsIgnoreCase(prefijoWSDL + ":part") || partEnTurno.getTagName().equalsIgnoreCase("part")){
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
                                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO);
                                            nodo.setTipoDeDato(tipoDeDato);
                                        }

                                        //Insertando nodo
                                        operaciones.get(noOperacion).insertarNodo(rutaInsercion, nodo);
                                        log.printLogMessage("Ruta de insercionOP:"+rutaInsercion);//DEPURACION
                                        
                                        //Si es desconocido extraer el tipo
                                        if(nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                            extraerTipo(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO, nodo.getTipoDeDato(), rutaInsercion + " " + nodo.getNombre() , noOperacion);
                                        }
                                        
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
                                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO);
                                            nodo.setTipoDeDato(tipoDeDato);
                                        }

                                        //Insertando nodo
                                        operaciones.get(noOperacion).insertarNodo(rutaInsercion, nodo);
                                        log.printLogMessage("Ruta de insercionOP:"+rutaInsercion);//DEPURACION
                                        
                                        //Si es desconocido extraer el tipo
                                        if(nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                            extraerTipo(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO, nodo.getTipoDeDato(), rutaInsercion + " " + nodo.getNombre() , noOperacion);
                                        }
                                        
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
            log.printLogMessage("extraerPortType_WSDL1 nodo:" + noNodo);//DEPURACION
            
            if(nodoEnTurno.getNodeType() == Node.ELEMENT_NODE){
                Element elemento = (Element)nodoEnTurno;
                log.printLogMessage("Imprimiendo Nodo: "+ elemento.getTagName() + ">" + elemento.getNodeValue());//DEPURACION
                
                //Formando las raices de los nodos
                    if(elemento.getTagName().equalsIgnoreCase(prefijoWSDL + ":operation") || elemento.getTagName().equalsIgnoreCase("operation")){
                        ElementoWSDL raiz = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.OPERACION, elemento.getAttribute("name"));
                        ArbolWSDL arbol = new ArbolWSDL(raiz, ((Element)nodoAExaminar).getAttribute("name"));
                        operaciones.add(arbol);
                        extraerPortType_WSDL1(((Element)nodoAExaminar).getAttribute("name"), elemento.getAttribute("name"), nodoEnTurno);
                    }
                //Agregando los hijos directos del nodo operacion (wsdl:input y wsdl:output)
                    if(elemento.getTagName().equalsIgnoreCase(prefijoWSDL + ":input") || elemento.getTagName().equalsIgnoreCase("input")){
                        int noOperacion;
                        for(noOperacion=0; noOperacion<operaciones.size();noOperacion++){
                            if(operaciones.get(noOperacion).getOperacion().equals(rutaDeOperacion) && operaciones.get(noOperacion).getServicio().equals(servicio)){//Identificando la operacion para insertar correctamente los hijos
                                break;
                            }
                        }
                        ElementoWSDL input = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_ENTRADA, elemento.getAttribute("message"));
                        operaciones.get(noOperacion).insertarNodo(rutaDeOperacion, input);
                    }
                    if(elemento.getTagName().equalsIgnoreCase(prefijoWSDL + ":output") || elemento.getTagName().equalsIgnoreCase("output")){
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
    
    
    private ElementoXMLSchema buscarDefinicionDeTipo(String nombreDeTipo){
        ElementoXMLSchema definicion;
        
        nombreDeTipo = removerPrefijo(nombreDeTipo);
        log.printLogMessage("BUSCANDO::::::::::::::::::::::::::::::::::::"+nombreDeTipo);//DEPURACION
        //Buscando la definicion en el conjunto de elementos complejos del esquema
        definicion = esquemaDeTipos.buscarTipoComplejo(nombreDeTipo);
        if(definicion!=null){
            return definicion;
        }
        //Buscando la definicion en el conjunto de elementos elemento del esquema
        definicion = esquemaDeTipos.buscarElemento(nombreDeTipo);
        if(definicion!=null){
            return definicion;
        }
        //Buscando la definicion en el conjunto de elementos simples del esquema
        definicion = esquemaDeTipos.buscarTipoSimple(nombreDeTipo);
        if(definicion!=null){
            return definicion;
        }
        
        
        return definicion;
    }
    
    private void extraerTipo(ElementoXMLSchema.TipoDeElementoXMLSchema tipoDeElemento, String nombreDeTipo, String rutaDeInsercion, int operacion){
        ElementoWSDL nodo;
        ElementoXMLSchema elementoEsquema;
        log.printLogMessage("Ruta de insercion:"+rutaDeInsercion);//DEPURACION
        elementoEsquema = buscarDefinicionDeTipo(nombreDeTipo);
        if(elementoEsquema != null){
            //Acciones para un elemento complejo
                if(elementoEsquema.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO)){

                    //Determinar si el tipo complejo posee subelementos
                        if(elementoEsquema.tipoComplejo_poeseeSubelementos()){
                            ComplexType complejo = elementoEsquema.getElementoComplejo(); //extraccion del elemento
                            for (SchemaComponent sc : ((ModelGroup) complejo.getModel()).getParticles()) {
                                log.printLogMessage(" -Particle Kind: " + sc.getClass().getSimpleName());//DEPURACION
                                log.printLogMessage(" Particle Name: " + sc.getName());//DEPURACION
                                log.printLogMessage(" Prefix: " + sc.getPrefix());//DEPURACION
                                log.printLogMessage(" DataType: " + ((QName)sc.getProperty("type")).getQualifiedName() );//DEPURACION
                                log.printLogMessage(" String: " + sc.getAsString());//DEPURACION

                                //Creando nodo a insertar
                                    String tipo;
                                    nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, sc.getName());

                                    //Identificando el tipo del elemento
                                    tipo = ((QName)sc.getProperty("type")).getQualifiedName();
                                    if(tipo!=null){
                                        nodo.setTipoDeDato(tipo);
                                        nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO);
                                    }else{
                                        tipo = ((QName)sc.getProperty("element")).getQualifiedName();
                                        nodo.setTipoDeDato(tipo);
                                        nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.ELEMENTO);
                                    }
                                    //Verificando si el tipo es primitivo
                                    if(obtenerPrefijo(nodo.getTipoDeDato()).equals(elementoEsquema.getPrefijoXMLSchema())){
                                        nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                                    }


                                //Modificando tipo de elemento desconocido
                                if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                    operaciones.get(operacion).getNodo(rutaDeInsercion).setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO);
                                }

                                //Insertando nodo
                                operaciones.get(operacion).insertarNodo(rutaDeInsercion, nodo);
                                log.printLogMessage("Ruta de insercion:"+rutaDeInsercion);//DEPURACION

                                //Recursion
                                if(nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO) || nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.ELEMENTO)){
                                    extraerTipo(nodo.getTipoDeElementoXMLSchema(), nodo.getTipoDeDato(), rutaDeInsercion + " " + nodo.getNombre(), operacion);
                                }


                            }
                        }
                        
                    //Determinar si el tipo es extension o derivado
                        if(elementoEsquema.tipoComplejo_tieneContenidoSimple()){
                            //Es extension
                            Extension ext = ((SimpleContent) elementoEsquema.getElementoComplejo().getModel()).getExtension();
                            log.printLogMessage("QN:" + ext.getBasePN().getLocalName() + " prefijo:" + ext.getBasePN().getPrefix());//DEPURACION
                            
                            String tipo;
                            nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, elementoEsquema.getNombreDeVariable());
                            tipo = ext.getBasePN().getLocalName();

                            if(tipo != null){
                                nodo.setTipoDeDato(tipo);
                            }

                            //Identificando el tipo de elemento
                            if(obtenerPrefijo(nodo.getTipoDeDato()).equals(elementoEsquema.getPrefijoXMLSchema())){
                                nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                            }else{
                                nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO);
                            }

                            //Modificando tipo de elemento desconocido
                            if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                operaciones.get(operacion).getNodo(rutaDeInsercion).setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO);
                            }

                            //Insertando nodo
                            operaciones.get(operacion).insertarNodo(rutaDeInsercion, nodo);
                            log.printLogMessage("Ruta de insercion:"+rutaDeInsercion);//DEPURACION

                            //Recursion
                            if(nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                extraerTipo(nodo.getTipoDeElementoXMLSchema(), nodo.getTipoDeDato(), rutaDeInsercion + " " + nodo.getNombre(), operacion);
                            }
                            
                        }

                        if(elementoEsquema.tipoComplejo_tieneContenidoComplejo()){
                            //Es derivacion
                            Derivation der = ((ComplexContent) elementoEsquema.getElementoComplejo().getModel()).getDerivation();
                            
                            String tipo;
                            nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, elementoEsquema.getNombreDeVariable());
                            tipo = der.getBasePN().getLocalName();
                            

                            if(tipo != null){
                                nodo.setTipoDeDato(tipo);
                            }

                            //Identificando el tipo de elemento
                            if(der.getBasePN().getPrefix().equals(elementoEsquema.getPrefijoXMLSchema())){
                                nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                            }else{
                                nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO);
                            }

                            //Modificando tipo de elemento desconocido
                            if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                operaciones.get(operacion).getNodo(rutaDeInsercion).setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_COMPLEJO);
                            }

                            //Insertando nodo
                            operaciones.get(operacion).insertarNodo(rutaDeInsercion, nodo);
                            log.printLogMessage("Ruta de insercion:"+rutaDeInsercion);//DEPURACION

                            //Recursion
                            if(nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                                extraerTipo(nodo.getTipoDeElementoXMLSchema(), nodo.getTipoDeDato(), rutaDeInsercion + " " + nodo.getNombre(), operacion);
                            }
                            
                        }

                }
            //Acciones para un elemento simple
                if(elementoEsquema.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_SIMPLE)){
                    
                    SimpleType tipoSimple = elementoEsquema.getElementoSimple();
                    
                    log.printLogMessage("    SimpleType Name: " + tipoSimple.getName());//DEPURACION
                    log.printLogMessage("    SimpleType Restriction: " + tipoSimple.getRestriction());//DEPURACION
                    log.printLogMessage("               -----------:Base:" + tipoSimple.getRestriction().getBase().getQualifiedName());//DEPURACION
                    log.printLogMessage("               ----------:Prefix:" + tipoSimple.getRestriction().getBase().getPrefix());//DEPURACION
                    log.printLogMessage("    SimpleType Union: " + tipoSimple.getUnion());//DEPURACION
                    log.printLogMessage("    SimpleType List: " + tipoSimple.getList());//DEPURACION
                    
                    //Creando nodo a insertar
                        String tipo, prefijo;
                        nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, elementoEsquema.getNombreDeVariable());
                        
                        //Identificando el tipo del elemento
                        tipo = tipoSimple.getRestriction().getBase().getQualifiedName();
                        prefijo = tipoSimple.getRestriction().getBase().getPrefix();
                        if(tipo!=null){
                            nodo.setTipoDeDato(tipo);
                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_SIMPLE);
                            nodo.setDerivado();
                        }
                        
                        //Modificando tipo de elemento desconocido
                        if(tipoDeElemento.equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO)){
                            operaciones.get(operacion).getNodo(rutaDeInsercion).setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_SIMPLE);
                        }
                        
                        //Insertando nodo
                        operaciones.get(operacion).insertarNodo(rutaDeInsercion, nodo);
                        log.printLogMessage("Ruta de insercion:"+rutaDeInsercion);//DEPURACION
                        
                        if(prefijo!=null){
                            
                            //Verificando si el tipo es primitivo
                            if(prefijo.equals(elementoEsquema.getPrefijoXMLSchema())){
                                //El tipo es primitivo
                                ElementoWSDL nodoBase = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, elementoEsquema.getNombreDeVariable());  
                                nodoBase.setTipoDeDato(tipo);
                                nodoBase.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                                //Insertando nodo primitivo
                                operaciones.get(operacion).insertarNodo(rutaDeInsercion + " " + nodo.getNombre(), nodoBase);
                                
                            }else{
                                //El tipo es complejo
                                //Recursion
                                extraerTipo(nodo.getTipoDeElementoXMLSchema(), nodo.getTipoDeDato(), rutaDeInsercion + " " + nodo.getNombre(), operacion);
                            }
                            
                        }
                        
                }
            //Acciones para un elemento elemento
                if(elementoEsquema.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.ELEMENTO)){
                    
                    com.predic8.schema.Element elemento = elementoEsquema.getElementoElement();

                    log.printLogMessage("              prefijo: "+elemento.getPrefix());//DEPURACION
                    log.printLogMessage("    Element minoccurs: " + elemento.getMinOccurs());//DEPURACION
                    log.printLogMessage("    Element maxoccurs: " + elemento.getMaxOccurs());//DEPURACION
                    log.printLogMessage("  Schema del Elemento: " + elemento.getSchema().toString());//DEPURACION
                    
                    //Creando nodo a insertar
                        String tipo = "DESCONOCIDO";
                        nodo = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, elementoEsquema.getNombreDeVariable());

                        //Identificando el tipo del elemento
                        if(elemento.getType() != null){
                            tipo = ((QName)elemento.getType()).getQualifiedName();
                            nodo.setTipoDeDato(tipo);
                        }else{
                            log.printLogErrorMessage("Elemento " + elementoEsquema.getNombreDeVariable() + " no posee tipo");//DEPURACION
                        }                       
                        nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.ELEMENTO);
                        
                        //Verificando si el tipo es primitivo
                        if(obtenerPrefijo(nodo.getTipoDeDato()).equals(elementoEsquema.getPrefijoXMLSchema())){
                            nodo.setTipoDeElementoXMLSchema(ElementoXMLSchema.TipoDeElementoXMLSchema.TIPO_PRIMITIVO);
                        }
                        
                        //Insertando nodo
                        operaciones.get(operacion).insertarNodo(rutaDeInsercion, nodo);
                        log.printLogMessage("Ruta de insercion:"+rutaDeInsercion);//DEPURACION

                        //Recursion
                        if(nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.DESCONOCIDO) || nodo.getTipoDeElementoXMLSchema().equals(ElementoXMLSchema.TipoDeElementoXMLSchema.ELEMENTO)){
                            extraerTipo(nodo.getTipoDeElementoXMLSchema(), nodo.getTipoDeDato(), rutaDeInsercion + " " + nodo.getNombre(), operacion);
                        }
                        
                        
                }
        }
        
    }
    
    private String obtenerPrefijo(String cadena){
        if(cadena != null){
            int separador = cadena.indexOf(":");
            if(separador != -1){
                return cadena.substring(0, separador);
            }else{
                return "";
            }
        }else{
            return "";
        }
    }
    
    private String removerPrefijo(String cadena){
        if(cadena != null){
            int separador = cadena.indexOf(":");
            if(separador != -1){
                return cadena.substring(separador+1);
            }else{
                return cadena;
            }
        }else{
            return "";
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

        
        
        //Pruebas con un solo archivo WSDL
        
            //WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("http://www.xignite.com/xAnalysts.asmx?WSDL");
            //WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("https://api.networkip.net/jaduka/?WSDL");

            //WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
            WSDLExtractStructure objetoPruebas = new WSDLExtractStructure("http://www.xignite.com/xBATSLastSale.asmx?WSDL");//Direccion de Xmethod - http://www.xmethods.org/ve2/ViewListing.po;jsessionid=4g2EFxE845cgLvWrrPJxuswz?key=430207
            


            //Obtención de los arboles del documento parseado por WSDLEXtractStructure
                List<ArbolWSDL> estructuras;
                estructuras = objetoPruebas.getArbolesDeOperaciones();
                for(ArbolWSDL arbol:estructuras){
                    System.out.println("\nImprimiendo Arbol: " + arbol.getOperacion()+ " del servicio:" + arbol.getServicio());
                    arbol.imprimirArbol();
                    arbol.getSerializacionXML();
                }   
            
         
         
        /*
        //Pruebas con varios archivos WSDL
            WSDLExtractStructure objetoPruebas;
            List<String> wsdls = Directorio.getNombresDeFicherosDeURL("http://www.comparadordeoperacioneswsdl.netne.net/WSDLInterfaces/wsdl", ".wsdl");
            for(int wsdlNo = 0; wsdlNo<20; wsdlNo++){
                
                System.out.println("\n\n\n EXTRAYENDO WSDL[" + (wsdlNo+1) + "] " + wsdls.get(wsdlNo) + " \n\n\n");
                
                objetoPruebas = new WSDLExtractStructure(wsdls.get(wsdlNo));
                List<ArbolWSDL> estructuras;
                estructuras = objetoPruebas.getArbolesDeOperaciones();
                for(ArbolWSDL arbol:estructuras){
                    System.out.println("\nImprimiendo Arbol: " + arbol.getOperacion()+ " del servicio:" + arbol.getServicio());
                    arbol.imprimirArbol();
                    //arbol.getSerializacionXML();
                }   
            }  
          */  
    }    

}
