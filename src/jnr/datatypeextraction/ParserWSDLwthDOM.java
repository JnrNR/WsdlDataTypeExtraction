

package jnr.datatypeextraction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParserWSDLwthDOM {
    
    private String NsWSDL1 = "http://schemas.xmlsoap.org/wsdl/";
    //WSDL 1 primer etiqueta definitions
    
    private String NSWSDL2 = "http://www.w3.org/ns/wsdl";
    //WSDL 2 primer etiqueta description
    
    public static void main(String[] args){
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder;
        Document doc = null;

        
        try {
            
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
        
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(ParserWSDLwthDOM.class.getName()).log(Level.SEVERE, null, ex);
        }
 
            // Get the document's root XML node
            NodeList nodoEnTurno = doc.getChildNodes();
            
            System.out.println("Tipo de nodo:"+nodoEnTurno.item(0).getNodeType());
            
            
            System.out.println("nodeNAme:" + nodoEnTurno.item(0).getNodeName());
            System.out.println("localName:" + nodoEnTurno.item(0).getLocalName());
            //System.out.println("textContext:" + root.item(0).getTextContent());
                        System.out.println("Atributes:" + nodoEnTurno.item(0).getAttributes());
            NamedNodeMap attrs = nodoEnTurno.item(0).getAttributes();
            for (int y = 0; y < attrs.getLength(); y++ ) {
                Node attr = attrs.item(y);    
                    System.out.println(attr.getNodeName() + "  " +attr.getNodeValue());
            }
            
            System.out.println("prefix:" + nodoEnTurno.item(0).getPrefix());

            System.out.println("Hijos:");
            NodeList hijosNodoEnTurno = nodoEnTurno.item(0).getChildNodes();
            for(int hijo=0; hijo<hijosNodoEnTurno.getLength(); hijo++ ){
                Node nodo = hijosNodoEnTurno.item(hijo);
                if(nodo.getNodeType() == Node.ELEMENT_NODE){
                    Element elemento = (Element)nodo;
                    System.out.println("nombre: " + elemento.getNodeName());
                }
                
                
            }
            
    }
    
}
