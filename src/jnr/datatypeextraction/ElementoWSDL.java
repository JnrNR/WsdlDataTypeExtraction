//Author: Jorge Náder Roa

package jnr.datatypeextraction;


public class ElementoWSDL implements InterfazElementoWSDL {
    
    public static int ELEMENTO_OPERACION = 1;
    public static int ELEMENTO_MENSAJE_IN = 2;
    public static int ELEMENTO_MENSAJE_OUT = 3;
    public static int ELEMENTO_TD_COMPLEJO = 4;
    public static int ELEMENTO_TD_SIMPLE = 5;
    
    public static String TD_NULO = "NULL";
    
    private int tipoDeElemento;
    private String nombre;
    private String tipoDeDato;
    
    public ElementoWSDL(){}
    
    public ElementoWSDL(int tipoDeElemento, String nombre, String tipoDeDato){
        this.tipoDeElemento = tipoDeElemento;
        this.nombre = nombre;
        this.tipoDeDato = tipoDeDato;
    }
    
    
    public void setTipoDeElemento(int tipoDeElemento){
        this.tipoDeElemento = tipoDeElemento;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setTipoDeDato(String tipoDeDato){
        this.tipoDeDato = tipoDeDato;
     }
    
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getTipoDeElemento() {
        return tipoDeElemento;
    }

    @Override
    public String getTipoDeDato() {
        return tipoDeDato;
    }
    
    
}
