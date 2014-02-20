package jnr.rdfwsdloperationsmatcher;


import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 *
 * @author Jnr
 */
public class RDFVocabulary{

    public static final String nameSpaceBase = "http://RDF/Matcher";
    public static final String nameSpace = nameSpaceBase + "#";
    public static final String prefijoNameSpace = "RDFM";

    public static final Property OPERACION = ResourceFactory.createProperty( nameSpace + "OPERACION" );
    public static final Property MENSAJE_ENTRADA = ResourceFactory.createProperty( nameSpace + "MENSAJE_ENTRADA" );
    public static final Property MENSAJE_SALIDA = ResourceFactory.createProperty( nameSpace + "MENSAJE_SALIDA" );
    public static final Property TIPO = ResourceFactory.createProperty( nameSpace + "TIPO" );

    public static final Property TIPO_COMPLEJO = ResourceFactory.createProperty( nameSpace + "TIPO_COMPLEJO" );
    public static final Property TIPO_SIMPLE = ResourceFactory.createProperty( nameSpace + "TIPO_SIMPLE" );
    public static final Property ELEMENTO = ResourceFactory.createProperty( nameSpace + "ELEMENTO" );
    public static final Property TIPO_PRIMITIVO = ResourceFactory.createProperty( nameSpace + "TIPO_PRIMITIVO" );
    public static final Property DESCONOCIDO = ResourceFactory.createProperty( nameSpace + "DESCONOCIDO" );

}
