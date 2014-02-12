package jnr.utilities;

/**
 *
 * @author Jorge Náder Roa
 */
public class Log {
  
    //Depuración
    private boolean logMessagesActivated = true;
    private boolean logErrorMessagesActivated = true;
    private String logDefaultColor = Log.ANSI_BLUE;
    ////////////////////////////////////////////////
    
   /**
    * Código ANSI para reestablecer el color por default del texto en consola. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_RESET = "\033[0m";
   
   
   
   /**
    * Color ANSI: Negro. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BLACK = "\033[30m";
   /**
    * Color ANSI: Rojo. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_RED = "\033[31m";
   /**
    * Color ANSI: Verde. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_GREEN = "\033[32m";
   /**
    * Color ANSI: Amarillo. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_YELLOW = "\033[33m";
   /**
    * Color ANSI: Azul. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BLUE = "\033[34m";
   /**
    * Color ANSI: Purpura. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_PURPLE = "\033[35m";
   /**
    * Color ANSI: Cyan. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_CYAN = "\033[36m";
   /**
    * Color ANSI: Blanco. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_WHITE = "\033[37m";
   
   
   /**
    * Color ANSI: Negro en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_BLACK = "\033[30;1m";
   /**
    * Color ANSI: Rojo en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_RED = "\033[31;1m";
   /**
    * Color ANSI: Verde en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_GREEN = "\033[32;1m";
   /**
    * Color ANSI: Amarillo en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_YELLOW = "\033[33;1m";
   /**
    * Color ANSI: Azul en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_BLUE = "\033[34;1m";
   /**
    * Color ANSI: Purpura en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_PURPLE = "\033[35;1m";
   /**
    * Color ANSI: Cyan en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_CYAN = "\033[36;1m";
   /**
    * Color ANSI: Blanco en negritas. <br><br> DOS no soporta colores ANSI, Puede ser utilizado en terminales Linux o Netbeans.
    */
   public static final String ANSI_BOLD_WHITE = "\033[37;1m";
   
   /**
    * Instancia un objeto Log, para escribir en salida estandard mensajes con formatos definidos.<br>
    * 
    * @param showLogMessages Habilita la escritura de mensajes en salida estandard, valores(true/False).
    * @param showErrorMessages Habilita la escritura de mensajes de error en salida estandard, valores(true/false).
    * @param defaultColor Establece el color por default para los mensajes del objeto. Recibe como parametro cualquier color disponible de la clase Log, Ejemplo: Log.ANSI_BLUE.
    */
   public Log(boolean showLogMessages, boolean showErrorMessages, String defaultColor){
       logMessagesActivated = showLogMessages;
       logErrorMessagesActivated = showErrorMessages;
       if(defaultColor!= null){
            logDefaultColor = defaultColor;
       }
   }
   
   /**
    * Activa la escritura de mensajes en salida estandard.
    */
   public void turnON_LogMessages(){
       logMessagesActivated = true;
   }
   /**
    * Desactiva la escritura de mensajes en salida estandard.
    */
   public void turnOFF_LogMessages(){
       logErrorMessagesActivated = false;
   }
   /**
    * Activa la escritura de mensajes de error en salida estandard.
    */
   public void turnON_LogErrorMessages(){
       logErrorMessagesActivated = true;
   }
   /**
    * Desactiva la escritura de mensajes de error en salida estandard.
    */
   public void turnOFF_LogErrorMessages(){
       logErrorMessagesActivated = false;
   }
   /**
    * Establece 
    * @param color Establece el color por default para los mensajes del objeto. Recibe como parametro cualquier color disponible de la clase Log, Ejemplo: Log.ANSI_BLUE.
    */
   public void setDefaultColor(String color){
       logDefaultColor = color;
   }
   

  /**
   * Imprime un texto en consola con un determinado color
      * <br/><br/>
      * <b>Ejemplo:<b/><br/><br/>
   * <blockquote>
      * Log.println("Texto rojo", Log.ANSI_BLUE);
      * </blockquote>
      * <br/>
      * @param color El color de la cadena
      * @param txt La cadena de texto a mostrar
      * @see <b>http://www.javaservletsjspweb.in/2013/06/coloring-java-output-on-console.html#.UvpBGOWVMlS</b>
      * @see http://en.wikipedia.org/wiki/ANSI_escape_code
      */
   public static void println(String txt, String color){
     System.out.println(color + txt + ANSI_RESET);
   }
    
   /**
    * Escribe en salida estandard un mensaje a color.<br>
    * El color puede ser definido con el metodo: setDefaultColor(color).<br>
    * <b>Funciona terminales Unix y Netbeans, En DOS no funciona.</b>
    * @param message Recibe la cadena del mensaje.
    */
   public void printLogMessage(String message){
       if(logMessagesActivated){
           println(message, logDefaultColor);
       }
   }
   /**
    * Escribe en salida estandard un mensaje de error en color rojo.<br>
    * <b>Funciona terminales Unix y Netbeans, En DOS no funciona.</b>
    * @param message Recibe la cadena del mensaje.
    */
   public void printLogErrorMessage(String error){
       if(logErrorMessagesActivated){
           println(error, ANSI_BOLD_RED);
       }
   }
   
   
   public static void main(String[] args){
       //Exemplo de Uso
        println("Negro", Log.ANSI_BLACK);
        println("Negro en negritas", Log.ANSI_BOLD_BLACK);
        println("Azul", Log.ANSI_BLUE);
        println("Azul en negritas", Log.ANSI_BOLD_BLUE);
        println("Cyan", Log.ANSI_CYAN);
        println("Cyan en negritas", Log.ANSI_BOLD_CYAN);
        println("Verde", Log.ANSI_GREEN);
        println("Verde en negritas", Log.ANSI_BOLD_GREEN);
        println("Purpura", Log.ANSI_PURPLE);
        println("Purpura en negritas", Log.ANSI_BOLD_PURPLE);
        println("Rojo", Log.ANSI_RED);
        println("Rojo en negritas", Log.ANSI_BOLD_RED);
        println("Blanco", Log.ANSI_WHITE);
        println("Blanco en negritas", Log.ANSI_BOLD_WHITE);
        println("Amarillo", Log.ANSI_YELLOW);
        println("Amarillo en negritas", Log.ANSI_BOLD_YELLOW);
       
   }
   
}
