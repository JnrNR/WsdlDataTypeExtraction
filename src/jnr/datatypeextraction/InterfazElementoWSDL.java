//Author: Jorge Náder Roa

package jnr.datatypeextraction;


public interface InterfazElementoWSDL {
    
    public String getNombre();
    public String getPrefijo();
    public ElementoWSDL.TipoDeElementoWSDL getTipoDeElementoWSDL();
    public ElementoXMLSchema.TipoDeElementoXMLSchema getTipoDeElementoXMLSchema();
    public String getTipoDeDato();
    
}
