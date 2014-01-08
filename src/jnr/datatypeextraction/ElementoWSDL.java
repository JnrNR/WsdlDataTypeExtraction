//Author: Jorge Náder Roa

package jnr.datatypeextraction;


public class ElementoWSDL implements InterfazElementoWSDL {
    
    public static int ELEMENTO_OPERACION = 1;
    public static int ELEMENTO_MENSAJE_IN = 2;
    public static int ELEMENTO_MENSAJE_OUT = 3;
    public static int ELEMENTO_TD_COMPLEJO = 4;
    public static int ELEMENTO_TD_SIMPLE = 5;
    
    public static String TD_NULO = "NULL";
    
    private String nombre;
    private String prefijo;
    
    private int tipoDeElemento;
    private String tipoDeDato;
    
    public ElementoWSDL(){}
    
    public ElementoWSDL(int tipoDeElemento, String nombre, String tipoDeDato){
        
        //Determinando si nombre posee prefijo, de ser asi separa el nombre y el prefijo
        if(nombre.indexOf(":") != -1){
            this.nombre = nombre.substring( nombre.indexOf(":")+1 );
            prefijo = nombre.substring(0, nombre.indexOf(":")-1);
        }else{
            this.nombre = nombre;
            prefijo = null;
        }
        
        this.tipoDeElemento = tipoDeElemento;
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
    public String getPrefijo() {
        return prefijo;
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
