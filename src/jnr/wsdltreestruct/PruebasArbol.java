//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import jnr.datatypeextraction.ElementoWSDL;


public class PruebasArbol {
    
    public static void main(String[] args){
        
        ElementoWSDL raiz = new ElementoWSDL(ElementoWSDL.ELEMENTO_OPERACION, "Raiz", ElementoWSDL.TD_NULO);
        ElementoWSDL raizN1 = new ElementoWSDL(ElementoWSDL.ELEMENTO_COMPLEJO, "N1", ElementoWSDL.TD_NULO);
        ElementoWSDL raizN1E1 = new ElementoWSDL(ElementoWSDL.ELEMENTO_SIMPLE, "E1", "short");
        ElementoWSDL raizN1E2 = new ElementoWSDL(ElementoWSDL.ELEMENTO_SIMPLE, "E2", "char");
        ElementoWSDL raizN1N1 = new ElementoWSDL(ElementoWSDL.ELEMENTO_COMPLEJO, "N1", ElementoWSDL.TD_NULO);
        ElementoWSDL raizN1N1E1 = new ElementoWSDL(ElementoWSDL.ELEMENTO_SIMPLE, "E1", "String");
        ElementoWSDL raizN2 = new ElementoWSDL(ElementoWSDL.ELEMENTO_SIMPLE, "N2", "int");
        
        
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
