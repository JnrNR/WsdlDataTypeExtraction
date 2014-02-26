//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import java.util.ArrayList;
import java.util.List;


class Rama{
    
        private int ramaAntecesora;
        private int nodoAntecesor;
        private int profundidad;
        private int id;
        
        private List<Nodo> rama;
        
        Rama(int id, int profundidad, int ramaAntecesora, int nodoAntecesor){
            rama = new ArrayList<Nodo>();
            
            this.nodoAntecesor = nodoAntecesor;
            this.ramaAntecesora = ramaAntecesora;
            this.profundidad = profundidad;
            this.id = id;
        }
        
        @Deprecated
        Rama(int profundidad, int ramaAntecesora, int nodoAntecesor){
            rama = new ArrayList<Nodo>();
            
            this.nodoAntecesor = nodoAntecesor;
            this.ramaAntecesora = ramaAntecesora;
            this.profundidad = profundidad;
        }
        
        public void insertarNodo(Nodo nodo){
            rama.add(nodo);
        }
        public List<Nodo> getNodos(){
            return rama;
        }
        
        public void setRamaPorCopia(Rama ramaNueva){
            ramaAntecesora = ramaNueva.getRamaAntecesora();
            nodoAntecesor = ramaNueva.getNodoAntecesor();
            profundidad = ramaNueva.getProfundidad();
            id = ramaNueva.getId();

            rama = ramaNueva.getNodos();    
        }
        
        public int getNodoAntecesor(){
            return nodoAntecesor;
        }
        public int getRamaAntecesora(){
            return ramaAntecesora;
        }
        public int getProfundidad(){
            return profundidad;
        }
        public int getId(){
            return id;
        }
        
        
}
