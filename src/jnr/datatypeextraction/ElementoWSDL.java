//Author: Jorge Náder Roa

package jnr.datatypeextraction;


public class ElementoWSDL extends ElementoXMLSchema implements InterfazElementoWSDL {
    
    public enum TipoDeElementoWSDL {OPERACION, MENSAJE_ENTRADA, MENSAJE_SALIDA, TIPO, ESTRUCTURA}
        
    private String nombre;
    private String prefijo;
    
    private TipoDeElementoWSDL tipoDeElemento;
    
    public ElementoWSDL(){}
    
    public ElementoWSDL(TipoDeElementoWSDL tipoDeElemento, String nombre){
        
        //Determinando si nombre posee prefijo, de ser asi separa el nombre y el prefijo
        if(nombre.indexOf(":") != -1){
            this.nombre = nombre.substring( nombre.indexOf(":")+1 );
            prefijo = nombre.substring(0, nombre.indexOf(":")-1);
        }else{
            this.nombre = nombre;
            prefijo = null;
        }
        
        this.tipoDeElemento = tipoDeElemento;

    }
    
    public void setTipoDeElemento(TipoDeElementoWSDL tipoDeElemento){
        this.tipoDeElemento = tipoDeElemento;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String getPrefijo() {
        return prefijo;
    }

    @Override
    public TipoDeElementoWSDL getTipoDeElementoWSDL() {
        return tipoDeElemento;
    }
    
    @Override
    public ElementoXMLSchema.TipoDeElementoXMLSchema getTipoDeElementoXMLSchema() {
        return super.getTipoDeElementoXMLSchema();
    }

    @Override
    public String getTipoDeDato() {
        return super.getTipoDeDato();
    }
    
}
