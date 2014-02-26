
package jnr.wsdltreestruct;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import jnr.datatypeextraction.ElementoWSDL;
import jnr.datatypeextraction.InterfazElementoWSDL;
import jnr.rdfwsdloperationsmatcher.RDFVocabulary;
import jnr.utilities.Log;

/**
 * 
 * @author Jorge Náder Roa
 */
public class ArbolWSDL {
    //Depuración
    public Log log = new Log(true, true, Log.ANSI_BOLD_GREEN);
    /////////////////////////////////////////////////
    
    
    private String RAMIFICACION = "+";
    private String HOJA = "-";
    
    private Model modeloRDF;
    
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
    
    private void setTriplets(Nodo nodo, int profundidadDelNodo){
        Rama hijosDelNodo;
        
        hijosDelNodo = tronco.get(profundidadDelNodo+1).get(nodo.getRamaDescendiente());

        Resource sujeto = modeloRDF.createResource(RDFVocabulary.nameSpaceBase + "/" + nodo.getNombre());
        
        for(Nodo hijoDelNodo : hijosDelNodo.getNodos()){
            if(hijoDelNodo.getRamaDescendiente() != -1){
                sujeto.addProperty(RDFVocabulary.ELEMENTO, modeloRDF.createResource(RDFVocabulary.nameSpaceBase + "/" + hijoDelNodo.getNombre()));
            }else{
                sujeto.addProperty(RDFVocabulary.ELEMENTO, RDFVocabulary.nameSpaceBase + "/" + hijoDelNodo.getNombre());
            }
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
                    setTriplets(nodoEnTurno, ramaEnTurno.getProfundidad());
                }else{
                    imprimirNodo(nodoEnTurnoDatos, ramaEnTurno.getProfundidad(), RAMIFICACION);
                    log.printLogMessage("Hijos:"+nodoEnTurno.getNumeroDeHijos());//DEPURACION
                }
                
                //Explorar la rama referenciada
                recorreArbol(tronco.get(ramaEnTurno.getProfundidad()+1).get(nodoEnTurno.getRamaDescendiente()), recorridoRDF);
                
            }else{//Nodo sin descendencia
                if(!recorridoRDF){
                    imprimirNodo(nodoEnTurnoDatos, ramaEnTurno.getProfundidad(), HOJA);
                    log.printLogMessage("Hijos:"+nodoEnTurno.getNumeroDeHijos());//DEPURACION
                }
                
            }
        }
        
    }
    
    public void imprimirArbol(){//Este método imprime en salida estandard el arbol del objeto
        
        //Inicializa el metodo desde el contenedor raiz(0) y la ramaRaiz(0) 
        recorreArbol(tronco.get(0).get(0), false);
        
    }
    
    private void makeRDFmodel(){//Requiere implementacion con una variación del método recorreArbol
        modeloRDF = ModelFactory.createDefaultModel();
        
        //Inicializa el metodo desde el contenedor raiz(0) y la ramaRaiz(0) 
        recorreArbol(tronco.get(0).get(0), true);
        
    }
    
    public Model getRDFModel(){
        if(modeloRDF == null){
            makeRDFmodel();
        }
        return modeloRDF;
    }
    
    public void mostrarSerializacionRDF_xml(){
        if(modeloRDF == null){
            makeRDFmodel();
        }
        parametrizarArbol(tronco.get(0).get(0));
        imprimirArbol();
        ordenarArbol();
        imprimirArbol();
        //Escritura RDF
        //modeloRDF.write(System.out);
        
        
    }
    
    /**
     * Obtiene la cantidad de hijos que posee cada nodo y las almacena en la variable numeroDeHijos de cada objeto nodo.
     */
    private void parametrizarArbol(Rama ramaEnTurno){
        //Variables Auxiliares
        Nodo nodoEnTurno;    
        int noNodos = ramaEnTurno.getNodos().size();
        int noHijosNodoEnTurno;
        
        for(int nodo=0; nodo<noNodos;nodo++){
            nodoEnTurno = ramaEnTurno.getNodos().get(nodo);
            if(nodoEnTurno.getRamaDescendiente()!= -1){
                //Tiene hijos
                noHijosNodoEnTurno = tronco.get(ramaEnTurno.getProfundidad()+1).get(nodoEnTurno.getRamaDescendiente()).getNodos().size();
                nodoEnTurno.setNumeroDeHijos(noHijosNodoEnTurno);
                
                //Explorar la rama referenciada
                parametrizarArbol(tronco.get(ramaEnTurno.getProfundidad()+1).get(nodoEnTurno.getRamaDescendiente()));
            }else{
                //No tiene hijos
                nodoEnTurno.setNumeroDeHijos(0);
            }
            
        }
        
    }
    
    public void NodosQuicksort(int A[][], int izq, int der) { 

        int pivote = A[0][izq]; // tomamos primer elemento como pivote
        int acarreoDelPivote = A[1][izq];
        
        int i = izq; // i realiza la búsqueda de izquierda a derecha
        int j = der; // j realiza la búsqueda de derecha a izquierda
        int aux;
        int auxDeAcarreo;

        while(i<j){            // mientras no se crucen las búsquedas
            while(A[0][i]<=pivote && i<j) i++; // busca elemento mayor que pivote
                while(A[0][j]>pivote) j--;     // busca elemento menor que pivote
                    if (i<j) {                 // si no se han cruzado                      
                        aux = A[0][i];         // los intercambia
                        auxDeAcarreo = A[1][i];
                        
                        A[0][i] = A[0][j]; 
                        A[1][i] = A[1][j];
                        
                        A[0][j] = aux;
                        A[1][j] = auxDeAcarreo;
                    } 
        } 
        A[0][izq] = A[0][j]; // se coloca el pivote en su lugar de forma que tendremos
        A[1][izq] = A[1][j];
        
        A[0][j] = pivote; // los menores a su izquierda y los mayores a su derecha
        A[1][j] = acarreoDelPivote;
        if(izq<j-1)
            NodosQuicksort(A,izq,j-1); // ordenamos subarray izquierdo
        if(j+1 <der)
            NodosQuicksort(A,j+1,der); // ordenamos subarray derecho
    }
    
    
    /**
     * Ordena todos los nodos de una rama en base al numero de hijos que estos poseen, 
     * ordenando de izquierda a derecha los nodos con mayor numero de hijos.
     * @param rama Rama a ordenar.
     * @return Rama ordenada
     */
    private Rama ordenarRama(Rama rama){
        int[][] arregloNodos;
        int noDeNodos = rama.getNodos().size();
        arregloNodos = new int[2][noDeNodos];
        
        int i;
        for(i=0; i<noDeNodos; i++){
            //Almacenando posicion anterior
                arregloNodos[1][i] = i;
            //Almacenando numero de hijos
                arregloNodos[0][i] = rama.getNodos().get(i).getNumeroDeHijos();
        }
        
        
        ////Ordenamiento
            NodosQuicksort(arregloNodos, 0, noDeNodos-1);
        ////
        
        Rama ramaOrdenada = new Rama(rama.getId(), rama.getProfundidad(), rama.getRamaAntecesora(), rama.getNodoAntecesor());
        int anteriorPosicion;
        
        //Ingresando los nodos ordenados a la nueva rama en forma decreciente
        for(i=(noDeNodos-1); 0<=i; i--){
            anteriorPosicion = arregloNodos[1][i];
            ramaOrdenada.insertarNodo(rama.getNodos().get(anteriorPosicion));
        }
        
        return ramaOrdenada;
    }
    
    /**
     * Ordena de izquierda a derecha los nodos con mayor numero de hijos.<br>
     * El rodenamiento se realiza para todas las ramas del arbol.
     */
    private void ordenarArbol(){
        
        for(int nivel=0; nivel<tronco.size(); nivel++){
            for(int rama=0; rama<tronco.get(nivel).size(); rama++){
                tronco.get(nivel).get(rama).setRamaPorCopia(ordenarRama(tronco.get(nivel).get(rama)));
            }
        }
        
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
