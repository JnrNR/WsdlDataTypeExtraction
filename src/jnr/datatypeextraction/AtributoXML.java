package jnr.datatypeextraction;

/**
 *
 * @author Jorge Náder Roa
 */
public class AtributoXML {
    
    private String atributo;
    private String valor;
    
    public AtributoXML(String atributo, String valor){
        this.atributo = atributo;
        this.valor = valor;
    }
    
    public String getAtributo(){
        return atributo;
    }
    
    public String getValor(){
        return valor;
    }
    
}
