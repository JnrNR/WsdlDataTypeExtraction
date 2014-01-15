//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import jnr.datatypeextraction.ElementoWSDL;


public class PruebasArbol {
    
    public static void main(String[] args){
        
        ElementoWSDL raiz = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.OPERACION, "Raiz");
        ElementoWSDL raizN1 = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, "N1");
        ElementoWSDL raizN1E1 = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, "E1");
        raizN1E1.setTipoDeDato("short");
        ElementoWSDL raizN1E2 = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, "E2");
        raizN1E2.setTipoDeDato("char");
        ElementoWSDL raizN1N1 = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, "N1");
        ElementoWSDL raizN1N1E1 = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, "E1");
        raizN1N1E1.setTipoDeDato("String");
        ElementoWSDL raizN2 = new ElementoWSDL(ElementoWSDL.TipoDeElementoWSDL.TIPO, "N2");
        raizN2.setTipoDeDato("int");
        
        ArbolWSDL arbol = new ArbolWSDL(raiz, "ServicioPrueba");
        arbol.insertarNodo("Raiz", raizN1);
        arbol.insertarNodo("Raiz N1", raizN1E1);
        arbol.insertarNodo("Raiz N1", raizN1E2);
        arbol.insertarNodo("Raiz N1", raizN1N1);
        arbol.insertarNodo("Raiz N1 N1", raizN1N1E1);
        arbol.insertarNodo("Raiz", raizN2);
        
        
        arbol.imprimirArbol();
        
    }
    
}
