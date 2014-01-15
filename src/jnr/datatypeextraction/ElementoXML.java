package jnr.datatypeextraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Repesenta un elemento XML simple con las propiedades basicas de una etiqueta XML, tales como:<br>
 * -Tag<br>
 * -Atributos<br>
 * -Contenido Simple*<br>
 * 
 * <br><i>*Contenido Simple: Texto plano comprendido entre las marcas &lt;&gt; &lt;/&gt; del elemento XML.</i>
 * 
 * @author Jorge Náder Roa
 */
public class ElementoXML {
    
    private String tag;
    private List<AtributoXML> atributos;
    
    private String contenido;
    
    public ElementoXML(){
        atributos = new ArrayList<AtributoXML>();
    }
    
    public ElementoXML(String tag, String contenido){
        atributos = new ArrayList<AtributoXML>();
        
        this.tag = tag;
        this.contenido = contenido;
        
    }
    
    /**
     * Establece el nombre del tag perteneciente al elemento XML.<br> 
     * @param tag recibe una cadena de caracteres con el nombre del tag del elemento.
     */
    public void setTag(String tag){
        this.tag = tag;
    }
    
    /**
     * Consulta el nombre asigando al tag del elemento XML.<br>
     * @return devuelve el nombre del tag del elemento XML.
     */
    public String getTag(){
        return tag;
    }
    
    /**
     * Permite establecer el contenido simple* del elemento XML.<br>
     * <br><i>*Contenido Simple: Texto plano comprendido entre las marcas &lt;&gt; &lt;/&gt; del elemento XML.</i>
     * @param contenido Recibe el contenido simple del elemento XML.
     */
    public void setContenido(String contenido){
        this.contenido = contenido;
    }
    
    /**
     * Consultra el contenido simple* del elemento XML.<br>
     * <br><i>*Contenido Simple: Texto plano comprendido entre las marcas &lt;&gt; &lt;/&gt; del elemento XML.</i>
     * @return devuelve el contenido simple del elemento XML.
     */
    public String getContenido(){
        return this.contenido;
    }
    
    /**
     * Permite agregar un atributo perteneciente a la marca de apertura del elemento XML.
     * @param atributo Recibe un objeto AtributoXML.
     */
    public void agregarAtributo(AtributoXML atributo){
        atributos.add(atributo);
    }
    
    /**
     * Recupera la lista completa de los atributos pertenecientes al elemento XML.
     * @return devuelve una lista de atributos List<AtributoXML>
     */
    public List<AtributoXML> getAtributos(){
        return atributos;
    }
    
    
}
