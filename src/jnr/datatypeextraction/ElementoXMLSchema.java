package jnr.datatypeextraction;

import com.predic8.schema.ComplexType;
import com.predic8.schema.SimpleType;

/**
 *
 * @author Jorge Náder Roa
 */
public class ElementoXMLSchema extends ElementoXML {

    public enum TipoDeElementoXMLSchema {TIPO_COMPLEJO, TIPO_SIMPLE, TIPO_PRIMITIVO}
    
    private String nombreDeVariable;
    private String prefijo;
    
    private TipoDeElementoXMLSchema tipoDeElemento;
    private String tipoDeDato;
    
    private String elementoPrimitive;
    private SimpleType elementoSimpleType;
    private ComplexType elementoComplexType;
    
    public ElementoXMLSchema(){}
     
    public ElementoXMLSchema(SimpleType elementoSimple){
        //Iniciando Constructor Super Clase
        super.setTag("simpleType");
        
        tipoDeElemento = TipoDeElementoXMLSchema.TIPO_SIMPLE;

        elementoSimpleType = elementoSimple;
    }
    
    public ElementoXMLSchema(ComplexType elementoComplejo){
        //Iniciando Constructor Super Clase
        super.setTag("complexType");
        
        tipoDeElemento = TipoDeElementoXMLSchema.TIPO_COMPLEJO;
        
        elementoComplexType = elementoComplejo;
    }
    
    public ElementoXMLSchema(String elementoPrimitivo){
        //Iniciando Constructor Super Clase
        super.setTag("element");
        
        tipoDeElemento = TipoDeElementoXMLSchema.TIPO_PRIMITIVO;
        
        elementoPrimitive = elementoPrimitivo; 
        
    }

    public void setNombreDeVariable(String nombreDeVariable){
        this.nombreDeVariable = nombreDeVariable;
    }
    
    public String getNombreDeVariable(){
        return nombreDeVariable;
    }
    
    public void setTipoDeDato(String tipoDeDato){
        this.tipoDeDato = tipoDeDato; 
    }
    
    public String getTipoDeDato(){
        return tipoDeDato;
    }
    
    public void setTipoDeElementoXMLSchema(TipoDeElementoXMLSchema tipoDeElemento){
        this.tipoDeElemento = tipoDeElemento;
    }
    
    public TipoDeElementoXMLSchema getTipoDeElementoXMLSchema(){
        return tipoDeElemento;
    }
    
}
