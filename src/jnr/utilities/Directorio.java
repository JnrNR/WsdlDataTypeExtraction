
package jnr.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author Jorge Náder Roa
 */
public class Directorio {
    //Depuración
    public static Log log = new Log(true, true, Log.ANSI_YELLOW);
    /////////////////////////////////////////////////
    List<String> ficheros;
    
    
    public Directorio(){}
    
    public List<String> getFicheros(){
        return ficheros;
    }
    
    public void buscarElementosDirectorio(String ruta){
        this.ficheros = new ArrayList<String>();
        
        File directorio = new File(ruta);
        
        String[] ficheros = directorio.list();
        if (ficheros == null)
            log.printLogErrorMessage("No hay ficheros en el directorio especificado");
        else { 
            for (int x=0;x<ficheros.length;x++)
                this.ficheros.add(ficheros[x]);
        }
    }
    
    
    public List<String> getNombresFicheroURL(String url, String extension){
        boolean formatoLista = false;
        
        this.ficheros = new ArrayList<String>();
        try{
            Document doc = Jsoup.connect(url).get();
            //System.out.println(doc.toString());
            for (Element file : doc.select("li a")) {
                //System.out.println(file.attr("href"));
                this.ficheros.add(file.text());
                formatoLista = true;
            }
            if(!formatoLista){
                for (Element file : doc.select("td a")) {
                    //System.out.println(file.attr("href"));
                    this.ficheros.add(file.text());
                }
            }
        }catch(IOException ex){
            log.printLogErrorMessage("Error en la apertura del listado EX:" + ex);
        }
        
        
        return ficheros;
    
    }
    
    
    
    
    public static void listarElementosDirectorio(String ruta){        
        File directorio = new File(ruta);
        
        String[] ficheros = directorio.list();
        if (ficheros == null)
            log.printLogErrorMessage("No hay ficheros en el directorio especificado");
        else { 
            for (int x=0;x<ficheros.length;x++)
                System.out.println(ficheros[x]);
        }
    }
    
    
    /**
     * Lista los ficheros contenidos en un directorio Web.<br>
     * 
     * <b>IMPORTANTE:</b> Es necesario que el servidor Web tenga activado "directory listing"<br> 
     * 
     * @see <a href="http://stackoverflow.com/questions/11561608/how-to-get-list-of-files-directories-of-an-directory-url-in-java">http://stackoverflow.com/questions/11561608/how-to-get-list-of-files-directories-of-an-directory-url-in-java</a>
     * 
     * @param url Ruta del directorio Web urlEjemplo = "http://www.ejemplo.com/directorioweb".
     */
    public static void listarElementosDeURL(String url){
        boolean formatoLista = false;
        try{
            Document doc = Jsoup.connect(url).get();
            //System.out.println(doc.toString());
            for (Element file : doc.select("li a")) {
                System.out.println(file.attr("href")+"        -"+file.text());
                
                formatoLista = true;
            }
            if(!formatoLista){
                for (Element file : doc.select("td a")) {
                    System.out.println(file.attr("href")+"        -"+file.text());
                }
            }
        }catch(IOException ex){
            log.printLogErrorMessage("Error en la apertura del listado EX:" + ex);
        }
    
    }
     
    
    /**
     * Devuelve el nombre de los ficheros contenidos en un directorio Web cuya extensón sea igual al parametro <b>extension</b>.<br>
     * 
     * <b>IMPORTANTE:</b> Es necesario que el servidor Web tenga activado "directory listing"<br> 
     * 
     * @see <a href="http://stackoverflow.com/questions/11561608/how-to-get-list-of-files-directories-of-an-directory-url-in-java">http://stackoverflow.com/questions/11561608/how-to-get-list-of-files-directories-of-an-directory-url-in-java</a>
     * 
     * @param url Ruta del directorio Web urlEjemplo = "http://www.ejemplo.com/directorioweb".
     * @param extension Extension filtro.
     */
    public static List<String> getNombresDeFicherosDeURL(String url, String extension){
        List<String> nombres = new ArrayList<String>();
        String nombre;
        try{
            Document doc = Jsoup.connect(url).get();

            for (Element file : doc.select("li a")) {
                nombre = file.attr("href"); 
                if(nombre.contains(extension)){
                    nombres.add(url+"/"+nombre);
                }
            }
        }catch(IOException ex){
            log.printLogErrorMessage("Error en la apertura del listado EX:" + ex);
        }
        
        return nombres;
    }
    
    
    public static void main(String[] args){
        //Directorio.listarElementosDeURL("http://www.comparadordeoperacioneswsdl.netne.net/WSDLInterfaces/wsdl");
        Directorio.listarElementosDeURL("http://148.247.102.37:8080/wsdlcomparison/servicios/TC3-WSDL");
        
    }
    
}
