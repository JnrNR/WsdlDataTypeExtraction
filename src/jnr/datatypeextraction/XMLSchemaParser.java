/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jnr.datatypeextraction;

import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexContent;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Derivation;
import com.predic8.schema.Element;
import com.predic8.schema.Extension;
import com.predic8.schema.ModelGroup;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.SchemaParser;
import com.predic8.schema.SimpleContent;
import com.predic8.schema.SimpleType;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import groovy.xml.QName;
import java.util.Hashtable;
import jnr.utilities.Log;

/**
 * Esta clase permite extraer los tipos definifos en el esquema xml proporcionado.
 * 
 * @author Jorge Náder Roa
 */
public class XMLSchemaParser {
    //Depuración
    public Log log = new Log(false, true, Log.ANSI_PURPLE);
    /////////////////////////////////////////////////
    
    public static final String XMLSCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
    
    Schema esquema;
    
    Hashtable<String, ElementoXMLSchema> complexTypes = new Hashtable<String, ElementoXMLSchema>();
    Hashtable<String, ElementoXMLSchema> simpleTypes = new Hashtable<String, ElementoXMLSchema>();
    Hashtable<String, ElementoXMLSchema> elements = new Hashtable<String, ElementoXMLSchema>();
    
    /**
     * Instancia un objeto XMLSchemaParser vacio.
     */
    public XMLSchemaParser(){}
    
    
    /**
     * Inicializa la clase parseando un esquema partiendo de su URI
     * @param esquema recibe la cadena de la URI del esquema.
     */
    public XMLSchemaParser(String esquema){
        SchemaParser parser = new SchemaParser();
        this.esquema = parser.parse(esquema);
          
        //Extraccion de elementos y tipos de datos complejos y simples pertenecientes al esquema y a los esquemas importados.
        extraccionDeElementos(this.esquema);
        
    }
    
    /**
     * Inicializa la clase a partir de un objeto Schema.
     * @param esquema recibe un objeto Schema para la extraccion de tipos.
     */
    public XMLSchemaParser(Schema esquema){
        this.esquema = esquema;
        
        //Extraccion de elementos y tipos de datos complejos y simples pertenecientes al esquema y a los esquemas importados.
        extraccionDeElementos(this.esquema);
    }
    
    
    /**
     * Extrae recursivamente los elementos element y tipos de datos complejos y simples contenidos
     * en el esquema proporcionado y sus esquemas importados.
     * @param esquemaEnTurno recibe un esquema ya parseado para realizar la extraccion de elementos.
     */
    private void extraccionDeElementos(Schema esquemaEnTurno){
        
        log.printLogMessage("Espacio de nombres:" + esquemaEnTurno.getTargetNamespace() + " Prefijo: " + esquemaEnTurno.getPrefix(esquemaEnTurno.getTargetNamespace()));//DEPURACION

        
        //Almacenamiento de los elementos complextype
        for(ComplexType tipoComplejo : esquemaEnTurno.getComplexTypes()){
            log.printLogMessage("[TC]" + tipoComplejo.getNamespaceUri() + ">" + tipoComplejo.getPrefix() + ":" + tipoComplejo.getName() );//DEPURACION
            ElementoXMLSchema nuevoComplejo = new ElementoXMLSchema(tipoComplejo);
            nuevoComplejo.setPrefijoXMLSchema( esquemaEnTurno.getPrefix(XMLSCHEMA_NS).toString() );
            nuevoComplejo.setDatosDelEspacioDeNombresDelEsquema( esquemaEnTurno.getTargetNamespace(), esquemaEnTurno.getPrefix(esquemaEnTurno.getTargetNamespace()).toString() );
            
            complexTypes.put(tipoComplejo.getName(), nuevoComplejo);
        }
        //Almacenamiento de los elementos simpleType
        for(SimpleType tipoSimple : esquemaEnTurno.getSimpleTypes()){
            log.printLogMessage("[TS]" + tipoSimple.getNamespaceUri() + ">" + tipoSimple.getPrefix() + ":" + tipoSimple.getName() );//DEPURACION
            ElementoXMLSchema nuevoSimple = new ElementoXMLSchema(tipoSimple);
            nuevoSimple.setPrefijoXMLSchema( esquemaEnTurno.getPrefix(XMLSCHEMA_NS).toString() );
            nuevoSimple.setDatosDelEspacioDeNombresDelEsquema( esquemaEnTurno.getTargetNamespace(), esquemaEnTurno.getPrefix(esquemaEnTurno.getTargetNamespace()).toString() );
            
            simpleTypes.put(tipoSimple.getName(), nuevoSimple);
        }
        //Almacenamientos de los elementos Element
        for(Element elemento : esquemaEnTurno.getElements()){
            log.printLogMessage("[E]" + elemento.getNamespaceUri() + ">" + elemento.getPrefix() + ":" + elemento.getName() );//DEPURACION
            ElementoXMLSchema nuevoElemento = new ElementoXMLSchema(elemento);
            nuevoElemento.setPrefijoXMLSchema( esquemaEnTurno.getPrefix(XMLSCHEMA_NS).toString() );
            nuevoElemento.setDatosDelEspacioDeNombresDelEsquema( esquemaEnTurno.getTargetNamespace(), esquemaEnTurno.getPrefix(esquemaEnTurno.getTargetNamespace()).toString() );
            
            elements.put(elemento.getName(), nuevoElemento);
        }
        
        //Realiza la recursion  para la extraccion de elementos en los esquemas importados.
        for(Schema esquemaImportado : esquemaEnTurno.getImportedSchemas()){
            extraccionDeElementos(esquemaImportado);
        }
        
    }
    
    /**
     * Busca un tipo de dato complejo definido en los esquemas 
     * @param nombre recibe el nombre del tipo a buscar.
     * @return devuelve el tipo complejo del tipo buscado.
     */
    public ElementoXMLSchema buscarTipoComplejo(String nombre){
        return complexTypes.get(nombre);
        
    }
    
    /**
     * Busca un tipo de dato simple definido en los esquemas 
     * @param nombre recibe el nombre del tipo a buscar.
     * @return devuelve el tipo complejo del tipo buscado.
     */
    public ElementoXMLSchema buscarTipoSimple(String nombre){
        return simpleTypes.get(nombre);
        
    }
    
    /**
     * Busca un elemento definido en los esquemas 
     * @param nombre recibe el nombre del elemento a buscar.
     * @return devuelve el tipo complejo del tipo buscado.
     */
    public ElementoXMLSchema buscarElemento(String nombre){
        return elements.get(nombre);
        
    }
    
    
    
    public void chequeoTablas(){
        System.out.println("------------ Numero de elementos Almacenados");
        System.out.println("Complejos: " + complexTypes.size());
        System.out.println("  Simples: " + simpleTypes.size());
        System.out.println("Elementos: " + elements.size());
        System.out.println("\n");
    }
    
    
    /**
     * Imprimie en salida estandard el esquema completo
     */
    public void mostarEsquema(){
        System.out.println(esquema.getAsString());
    }
    
    /**
     * Muestra la información del esquema principal proporcionado en el constructor.
     */
    public void mostrarInformacionEsquemaRaiz(){
        mostrarInformacionDelEsquema(esquema);
    }
    public void mostrarInformacionEsquemasImportados(){
        for(Schema esquemaImportado : esquema.getImportedSchemas()){
            mostrarInformacionDelEsquema(esquemaImportado);
        }
    }
    
    /**
     * Imprime en salida estandard la informacion referente al esquema proporcionado
     * @param esquema recibe como entrada el esquema a mostrar
     */
    private void mostrarInformacionDelEsquema(Schema esquema){
        
        System.out.println("\n\n<<<<<<<<<<<<<<< Información del Esquema >>>>>>>>>>>>>>>");
        System.out.println(">>Schema TargetNamespace: " + esquema.getTargetNamespace());
        System.out.println(">>AttributeFormDefault: " + esquema.getAttributeFormDefault());
        System.out.println(">>ElementFormDefault: " + esquema.getElementFormDefault());
        
        System.out.println("----- Elementos -----");
        System.out.println("No de elementos del esquema: " + esquema.getAllElements().size() );
        
        System.out.println("No de Imports: " + esquema.getImports().size() );
        System.out.println("No de Includes: " + esquema.getIncludes().size() );
        
        System.out.println("No de Tipos Complejos: " + esquema.getComplexTypes().size() );
        System.out.println("No de Tipos Simples: " + esquema.getSimpleTypes().size() );
        System.out.println("No de Elementos: " + esquema.getElements().size() );
        System.out.println("No de grupos: " + esquema.getGroups().size() );      
        
        
        for(ComplexType tipoComplejo: esquema.getComplexTypes()){
            System.out.println("Tipo de Dato complejo:" + tipoComplejo.getName());

            if (tipoComplejo.getAttributes().size() > 0) {
                System.out.println(tipoComplejo.getName() + " attributes: ");

                for (Attribute attr : tipoComplejo.getAttributes()) {
                    System.out.println("      Attribute Name: " + attr.getName());
                    System.out.println("      Attribute Form: " + attr.getForm());
                    System.out.println("      Attribute ID: " + attr.getId());
                    System.out.println("      Attribute Use: " + attr.getUse());
                    System.out.println("      Attribute FixedValue: " + attr.getFixedValue());
                    System.out.println("      Attribute DefaultValue: " + attr.getDefaultValue());
                }
            }
            
            System.out.println("ComplexType Model: " + tipoComplejo.getModel().getClass().getSimpleName());
            if (tipoComplejo.getModel() instanceof ModelGroup) {
                System.out.println("    Model Particles: ");
                for (SchemaComponent sc : ((ModelGroup) tipoComplejo.getModel()).getParticles()) {
                    System.out.println(" -Particle Kind: " + sc.getClass().getSimpleName());
                    System.out.println(" Particle Name: " + sc.getName());
                    System.out.println(" Prefix: " + sc.getPrefix());
                    System.out.println(" DataType: " + ((QName)sc.getProperty("type")).getQualifiedName() );
                    System.out.println(" String: " + sc.getAsString());
                    
                }
            }
            
            if (tipoComplejo.getModel() instanceof ComplexContent) {
                Derivation der = ((ComplexContent) tipoComplejo.getModel()).getDerivation();
                System.out.println("      ComplexConten Derivation: " + der.getClass().getSimpleName());
                System.out.println("      Derivation Base: " + der.getBase());
                System.out.println("------------------------------N:"+der.getBasePN().getLocalName());
                System.out.println("------------------------------P:"+der.getBasePN().getPrefix());
                
            }else if (tipoComplejo.getModel() instanceof SimpleContent){
                Extension ext = ((SimpleContent) tipoComplejo.getModel()).getExtension();
                System.out.println("      ComplexConten Derivation: " + ext.getClass().getSimpleName());
                System.out.println("      Derivation Base: " + ext.getBase());
                System.out.println("------------------------------N:"+ext.getBasePN().getLocalName());
                System.out.println("------------------------------P:"+ext.getBasePN().getPrefix());
            }
            
            System.out.println("\n\n");
            
        }
        
        
        

        
        
    }
    
    public static void main(String[] args){
        WSDLParser parser = new WSDLParser();
        Definitions wsdl = parser.parse("https://api.networkip.net/jaduka/?WSDL");
        
        XMLSchemaParser tipos = new XMLSchemaParser(wsdl.getSchema(wsdl.getTargetNamespace()));
        tipos.mostrarInformacionEsquemaRaiz();
        System.out.println("\n\n\n-------------------------------Esquemas importados:");
        //tipos.mostrarInformacionEsquemasImportados();
        tipos.chequeoTablas();
        
    }
    
}
