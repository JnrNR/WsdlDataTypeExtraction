//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import jnr.datatypeextraction.ElementoWSDL;
import jnr.datatypeextraction.ElementoXMLSchema;
import jnr.datatypeextraction.InterfazElementoWSDL;


class Nodo implements InterfazElementoWSDL {
        
        private String codigoEstructural;
        private int ramaDescendiente = -1;
        private int numeroDeHijos = -1;
        private int peso = -1;
        
        private boolean nodoComplementario = false;
        
        private ElementoWSDL nodo; 
        
        
        Nodo(ElementoWSDL nodo){
            this.nodo = nodo;
        }
        

        Nodo(ElementoWSDL nodo, boolean nodoComplementario){
            this.nodo = nodo;
            
            this.nodoComplementario = nodoComplementario;
            if(nodoComplementario){
                numeroDeHijos = 0;
            }
            
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
            if(!nodoComplementario){
                this.numeroDeHijos = numeroDeHijos;
            }
        }
        
        public void setPeso(int peso){
            if(!nodoComplementario){
                this.peso = peso;
            }
        }
        
        public int getPeso(){
            return peso;
        }
        
       
        public boolean esComplementario(){
            return nodoComplementario;
        }
        
        public void setCodigoEstructural(int nivel, int noRama, int noNodo){
            codigoEstructural = "N"+nivel+"R"+noRama+"n"+noNodo;
        }
        
        public String getCodigoEstructural(){
            return codigoEstructural;
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
