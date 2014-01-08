//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import jnr.datatypeextraction.ElementoWSDL;
import jnr.datatypeextraction.InterfazElementoWSDL;


class Nodo implements InterfazElementoWSDL {
        
        private int ramaDescendiente = -1;
        private ElementoWSDL nodo; 
        
        Nodo(ElementoWSDL nodo){
            this.nodo = nodo;
        }
        
        public void setRamaDescendiente(int noRama){
            ramaDescendiente = noRama;
        }
        
        public ElementoWSDL getNodo(){
            return nodo;
        }
        
        public int getRamaDescendiente(){
            return ramaDescendiente;
        }

        @Override
        public String getNombre() {
            return nodo.getNombre();
        }
        
        @Override
        public String getPrefijo() {
            return nodo.getPrefijo();
        }

        @Override
        public int getTipoDeElemento() {
            return nodo.getTipoDeElemento();
        }

        @Override
        public String getTipoDeDato() {
            return nodo.getTipoDeDato();
        }


        
}
