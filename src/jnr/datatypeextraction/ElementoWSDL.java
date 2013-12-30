//Author: Jorge Náder Roa

package jnr.datatypeextraction;


public class ElementoWSDL implements InterfazElementoWSDL {
    
    public static int ELEMENTO_OPERACION = 1;
    public static int ELEMENTO_COMPLEJO = 2;
    public static int ELEMENTO_SIMPLE = 3;
    
    public static String TD_NULO = "NULL";
    
    private int tipoDeElemento;
    private String nombre;
    private String tipoDeDato;
    
    ElementoWSDL(int tipoDeElemento, String nombre, String tipoDeDato){
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
