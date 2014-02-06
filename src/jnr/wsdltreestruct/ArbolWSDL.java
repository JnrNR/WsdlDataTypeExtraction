//Author: Jorge Náder Roa

package jnr.wsdltreestruct;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import jnr.datatypeextraction.ElementoWSDL;
import jnr.datatypeextraction.InterfazElementoWSDL;


public class ArbolWSDL {
    private String RAMIFICACION = "+";
    private String HOJA = "-";
    
    
    private List <List<Rama>> tronco;
    private final String operacion;
    private final String servicio;
    private String mensajeEntrada;
    private String mensajeSalida;
    
    
    public ArbolWSDL(ElementoWSDL elementoRaiz, String servicio){
        tronco = new ArrayList<>();
        operacion = elementoRaiz.getNombre();
        this.servicio = servicio;
        
        List<Rama> raizContenedor = new ArrayList<>();
        Rama ramaRaiz = new Rama(0,0,0,0);
        ramaRaiz.insertarNodo( new Nodo(elementoRaiz) );
        
        raizContenedor.add(ramaRaiz);
        tronco.add(raizContenedor);
        
    }
    
    public void insertarNodo(String rutaInsercion, ElementoWSDL nuevoNodo){
        StringTokenizer tokenizerRuta = new StringTokenizer(rutaInsercion);
        String elementoRuta;
        
        Rama ramaEnTurno = tronco.get(0).get(0);
        Nodo nodoEnTurno;
        InterfazElementoWSDL nodoEnTurnoDatos;
        
        while(tokenizerRuta.hasMoreTokens()){
            elementoRuta = tokenizerRuta.nextToken(); //Extraemos uno de los elementos de la ruta
            
            for(int nodo=0; nodo<ramaEnTurno.getNodos().size(); nodo++){
                nodoEnTurno = ramaEnTurno.getNodos().get(nodo);
                nodoEnTurnoDatos = (InterfazElementoWSDL)nodoEnTurno;
                
                //Determinar si el token extraido de la rutaInsercion corresponde con el nombre del nodo en turno.
                //Si se encuentra una igualdad entre el nombre del nodo y el token, el nodo en turno es parte de la ruta.
                if(elementoRuta.equals(nodoEnTurnoDatos.getNombre())){//El nodo pertenece a la ruta
                    
                    //Verificando que exista la ruta de inserción
                    //Verificando que el nodo encontrado poseea una rama descendiente y que existan mas tokens en la ruta
                    if(nodoEnTurno.getRamaDescendiente()!=-1 && tokenizerRuta.hasMoreTokens()){//El nodo posee rama descendiente y mas tokens
                        //Actualizar siguiente rama
                        int nuevaProfundidad = ramaEnTurno.getProfundidad() + 1;
                        ramaEnTurno = tronco.get(nuevaProfundidad).get(nodoEnTurno.getRamaDescendiente());
                        
                    }else if(nodoEnTurno.getRamaDescendiente()==-1 && !tokenizerRuta.hasMoreTokens()){//Se encontro la ruta completa y no existe la rama descendietne
                        //Determinar si ya existe el contenedor del siguiente nivel de profundidad y si no existe crearlo.
                        int nuevaProfundidad = ramaEnTurno.getProfundidad() + 1;
                        if(!(nuevaProfundidad<tronco.size())){
                            //Se crea el contenedor de ramas del siguiente nivel y se inserta en el tronco
                            List<Rama> nivelContenedor = new ArrayList<>();
                            tronco.add(nivelContenedor);
                        }
                        //Se agrega el nuevo nodo
                        
                        //Creando la nueva rama a insertar
                        Rama nuevaRama = new Rama(tronco.get(nuevaProfundidad).size(),nuevaProfundidad,ramaEnTurno.getId(),nodo);
                        //Referenciando la nueva rama en el nodo en turno
                        nodoEnTurno.setRamaDescendiente(nuevaRama.getId());
                        //Agregndo el nuevo nodo a la rama
                        nuevaRama.insertarNodo(new Nodo(nuevoNodo));
                        //Insertando la rama en el contenedor
                        tronco.get(nuevaProfundidad).add(nuevaRama);
                        //listo
                        
                        //Determinando si se inserto algun mensaje de entrada o salida y almacenandolo en el atributo correspondiente
                        if(nuevaProfundidad == 1){
                            String nombreMensaje = nuevoNodo.getNombre();
                            if(nuevoNodo.getTipoDeElementoWSDL().equals(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_ENTRADA)){
                                mensajeEntrada = (nombreMensaje.indexOf(":")!=-1)?nombreMensaje.substring(nombreMensaje.indexOf(":")+1):nombreMensaje;
                            }else if(nuevoNodo.getTipoDeElementoWSDL().equals(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_SALIDA)){
                                mensajeSalida = (nombreMensaje.indexOf(":")!=-1)?nombreMensaje.substring(nombreMensaje.indexOf(":")+1):nombreMensaje;
                            }
                        }
                        
                        
                    }else if(nodoEnTurno.getRamaDescendiente()!=-1 && !tokenizerRuta.hasMoreTokens()){//Se encontro la ruta completa y ya existe la rama descendiente
                        int nuevaProfundidad = ramaEnTurno.getProfundidad() + 1;
                        
                        //Agregndo el nuevo nodo a la rama ya referenciada por el nodo en turno
                        tronco.get(nuevaProfundidad).get(nodoEnTurno.getRamaDescendiente()).insertarNodo(new Nodo(nuevoNodo));
                        //listo
                        
                        //Determinando si se inserto algun mensaje de entrada o salida y almacenandolo en el atributo correspondiente
                        if(nuevaProfundidad == 1){
                            String nombreMensaje = nuevoNodo.getNombre();
                            if(nuevoNodo.getTipoDeElementoWSDL().equals(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_ENTRADA)){
                                mensajeEntrada = (nombreMensaje.indexOf(":")!=-1)?nombreMensaje.substring(nombreMensaje.indexOf(":")+1):nombreMensaje;
                            }else if(nuevoNodo.getTipoDeElementoWSDL().equals(ElementoWSDL.TipoDeElementoWSDL.MENSAJE_SALIDA)){
                                mensajeSalida = (nombreMensaje.indexOf(":")!=-1)?nombreMensaje.substring(nombreMensaje.indexOf(":")+1):nombreMensaje;
                            }
                        }
                    
                    }else{//No existe ruta de insercion 
                        System.err.println("No existe la ruta de inserción");
                    }
                    
                    break;
                }
            }
        }
        
        
    }
    
    public ElementoWSDL getNodo(String rutaDelNodo){
        StringTokenizer tokenizerRuta = new StringTokenizer(rutaDelNodo);
        String elementoRuta;
        
        Rama ramaEnTurno = tronco.get(0).get(0);
        Nodo nodoEnTurno;
        InterfazElementoWSDL nodoEnTurnoDatos;
        ElementoWSDL nodoBuscado = null;
        
        while(tokenizerRuta.hasMoreTokens()){
            elementoRuta = tokenizerRuta.nextToken(); //Extraemos uno de los elementos de la ruta
            
            for(int nodo=0; nodo<ramaEnTurno.getNodos().size(); nodo++){
                nodoEnTurno = ramaEnTurno.getNodos().get(nodo);
                nodoEnTurnoDatos = (InterfazElementoWSDL)nodoEnTurno;
                
                if(elementoRuta.equals(nodoEnTurnoDatos.getNombre())){
                    
                    if(nodoEnTurno.getRamaDescendiente()!=-1 && tokenizerRuta.hasMoreTokens()){//Aun existen mas elementos en la ruta y se encontro el elemento el elemento actual de la ruta
                        int nuevaProfundidad = ramaEnTurno.getProfundidad() + 1;
                        ramaEnTurno = tronco.get(nuevaProfundidad).get(nodoEnTurno.getRamaDescendiente());
                        
                    }else if(!tokenizerRuta.hasMoreTokens()){//Se encontro el nodo
                            nodoBuscado = nodoEnTurno.getNodo();
                                                        
                    }else if(nodoEnTurno.getRamaDescendiente() == -1 && tokenizerRuta.hasMoreTokens()){//Aun no existe la ruta desceada
                        System.err.println("La ruta:" + rutaDelNodo + " no existe en el arbol, profundidad encontrada hasta:" + elementoRuta);
                    }
                               
                } 
            }
        }
        return nodoBuscado;
        
    }
    
    @Deprecated
    public void insertarNodo(int profundidadPadre, String padre, ElementoWSDL nuevoNodo){
        
        if(tronco.size()==1){//Si solo existe la raiz, se inserta el nodo en el siguiente nivel
            
            //Se crea el contenedor de ramas del siguiente nivel
            List<Rama> nivelContenedor = new ArrayList<>();
            Rama nuevaRama = new Rama(1,0,0);
            nuevaRama.insertarNodo(new Nodo(nuevoNodo));
            
            nivelContenedor.add(nuevaRama);//agregando la rama al contenedor
            tronco.add(nivelContenedor);//agregando el cotenedor al tronco del arbol
            
            //Indicando la descendencia de la rama en el nodo raiz
            tronco.get(0).get(0).getNodos().get(0).setRamaDescendiente(0);
            //tronco.get(Contenedor de la Raiz).get(ramaRaiz).getNodos().get(nodoRaiz).setRamaDescendiente(ramaCero[DelSiguienteNivel]);
            
        }else{            
            if(profundidadPadre<tronco.size()){
                int numeroDeRamasEnNivel, numeroDeNodosEnRama;
                InterfazElementoWSDL interfazNodoEnTurno;
                
                //Se hace una busqueda del nombre del nodo padre que contendrá al nuevo elemento
                //entre todos los nodos pertenecientes a todas las ramas del nivel
                
                //NO CONTEMPLA QUE DOS RAMAS PUEDAN TENER UN NODO CON EL MISMO NOMBRE, DE LO CONTRARIO FALLARIA
                
                numeroDeRamasEnNivel = tronco.get(profundidadPadre).size();
                for(int rama=0; rama<numeroDeRamasEnNivel; rama++){
                    numeroDeNodosEnRama = tronco.get(profundidadPadre).get(rama).getNodos().size();
                    for(int nodo=0; nodo<numeroDeNodosEnRama; nodo++){
                        interfazNodoEnTurno = (InterfazElementoWSDL)tronco.get(profundidadPadre).get(rama).getNodos().get(nodo);
                        if(padre.equals(interfazNodoEnTurno.getNombre())){
                            //Se encontro el padre y se puede insertar el nuevo nodo
                            
                            //Insertando nodo
                            
                            //Verificando existencia de rama descendiente previamente asignada
                            if(tronco.get(profundidadPadre).get(rama).getNodos().get(nodo).getRamaDescendiente() == -1){
                                //No existe rama descendiente previamente asignada
                                    //Verificando Existencia de contenedor de ramas en el siguiente nivel de profundidad
                                        if(!(profundidadPadre<tronco.size())){//Si no existe contendeor hay que crearlo

                                            //Se crea el contenedor de ramas del siguiente nivel y se inserta en el tronco
                                            List<Rama> nivelContenedor = new ArrayList<>();
                                            tronco.add(nivelContenedor);

                                        }
                                        
                                        //Creando la nueva rama a insertar
                                        Rama nuevaRama = new Rama(profundidadPadre+1,rama,nodo);
                                        //Agregndo el nuevo nodo a la rama
                                        nuevaRama.insertarNodo(new Nodo(nuevoNodo));
                                        //Insertando la rama en el contenedor
                                        tronco.get(profundidadPadre+1).add(nuevaRama);
                                        //listo
                                //
                            }
                            
                            tronco.get(profundidadPadre).get(rama).getNodos().get(nodo).setRamaDescendiente(rama);
                            
                            
                        }else{
                            System.err.println("No se encontro el padre en el nivel especificado");
                        }
                    }
                }
                
            }
            
        }
        
    }
    
    @Deprecated
    private void imprimirNodo(String nombre, ElementoWSDL.TipoDeElementoWSDL tipoDeElemento, String tipoDeDato, int identacion, String tipoNodo){
        //Coloca identación
        for(int i=0; i<=identacion; i++){
                    tipoNodo = "    " + tipoNodo;
        }
        System.out.println(tipoNodo + "[N:" + nombre + ", T:" + tipoDeElemento.toString() + ", TD:"+ tipoDeDato + "]");
        
    }
    
    private void imprimirNodo(InterfazElementoWSDL datosNodo, int identacion, String tipoDeNodo){
        //Coloca identación
        for(int i=0; i<=identacion; i++){
                    tipoDeNodo = "    " + tipoDeNodo;
        }
        if(datosNodo.getTipoDeElementoWSDL().equals(ElementoWSDL.TipoDeElementoWSDL.TIPO)){
            System.out.println(tipoDeNodo + "[Variable:" + datosNodo.getNombre() + ", TipoDeElementoWSDL:" + datosNodo.getTipoDeElementoWSDL().toString() + ", [ESQUEMA TipoDeElementoXMLSchema:"+ datosNodo.getTipoDeElementoXMLSchema() +", TipoDeDato:"+ datosNodo.getTipoDeDato() + "]]");
        }else{
            System.out.println(tipoDeNodo + "[Nodo:" + datosNodo.getNombre() + ", TipoDeElementoWSDL:" + datosNodo.getTipoDeElementoWSDL().toString() + "]");
        }
        
    }
    
    private void recorreArbol(Rama ramaEnTurno, boolean recorridoRDF){
        //Variables Auxiliares
        Nodo nodoEnTurno;
        InterfazElementoWSDL nodoEnTurnoDatos;
            
        
        int noNodos = ramaEnTurno.getNodos().size();
 
        
        for(int nodo=0;nodo<noNodos;nodo++){//Extrae todos los nodos de una rama
            nodoEnTurno = ramaEnTurno.getNodos().get(nodo);
            nodoEnTurnoDatos = (InterfazElementoWSDL)nodoEnTurno;
            
            if(nodoEnTurno.getRamaDescendiente()!=-1){//Nodo con descenencia (imprime el nodo y analiza la descendencia)
                if(recorridoRDF){
                
                }else{
                    imprimirNodo(nodoEnTurnoDatos, ramaEnTurno.getProfundidad(), RAMIFICACION);
                }
                
                //Explorar la rama referenciada
                recorreArbol(tronco.get(ramaEnTurno.getProfundidad()+1).get(nodoEnTurno.getRamaDescendiente()), recorridoRDF);
                
            }else{//Nodo sin descendencia
                if(recorridoRDF){
                
                }else{
                    imprimirNodo(nodoEnTurnoDatos, ramaEnTurno.getProfundidad(), HOJA);
                }
                
            }
        }
        
    }
    
    public void imprimirArbol(){//Este método imprime en salida estandard el arbol del objeto
        
        //Inicializa el metodo desde el contenedor raiz(0) y la ramaRaiz(0) 
        recorreArbol(tronco.get(0).get(0), false);
        
    }
    
    public void getSerializacionXML(){//Requiere implementacion con una variación del método recorreArbol
        
        //Inicializa el metodo desde el contenedor raiz(0) y la ramaRaiz(0) 
        recorreArbol(tronco.get(0).get(0), true);
        
    }
    
    public String getServicio(){
        return servicio;
    }
    public String getOperacion(){
        return operacion;
    }
    public String getMensajeEntrada(){
        return mensajeEntrada;
    }
    public String getMensajeSalida(){
        return mensajeSalida;
    }
    
    public int getAltura(){
        return tronco.size();
    }
    
    
}
