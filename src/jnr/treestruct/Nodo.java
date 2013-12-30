//Author: Jorge Náder Roa

package jnr.treestruct;


class Nodo <NT> {
        
        private int ramaDescendiente = -1;
        private NT nodo; 
        
        Nodo(NT nodo){
            this.nodo = nodo;
        }
        
        public void setRamaDescendiente(int noRama){
            ramaDescendiente = noRama;
        }
        
        public NT getNodo(){
            return nodo;
        }
        
        public int getRamaDescendiente(){
            return ramaDescendiente;
        }
        
}
