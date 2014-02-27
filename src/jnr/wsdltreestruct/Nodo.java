//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import jnr.datatypeextraction.ElementoWSDL;
import jnr.datatypeextraction.ElementoXMLSchema;
import jnr.datatypeextraction.InterfazElementoWSDL;


class Nodo implements InterfazElementoWSDL {
        
        private int ramaDescendiente = -1;
        private int numeroDeHijos = -1;
        private int peso = -1;
        
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
        
        public int getNumeroDeHijos(){
            return numeroDeHijos;
        }
        
        public void setNumeroDeHijos(int numeroDeHijos){
            this.numeroDeHijos = numeroDeHijos;
        }
        
        public void setPeso(int peso){
            this.peso = peso;
        }
        
        public int getPeso(){
            return peso;
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
        public ElementoWSDL.TipoDeElementoWSDL getTipoDeElementoWSDL() {
            return nodo.getTipoDeElementoWSDL();
        }

        @Override
        public ElementoXMLSchema.TipoDeElementoXMLSchema getTipoDeElementoXMLSchema() {
            return nodo.getTipoDeElementoXMLSchema();
        }
        
        @Override
        public String getTipoDeDato() {
            return nodo.getTipoDeDato();
        }

        
}
