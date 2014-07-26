package jnr.utilities;

import java.util.List;

/**
 *
 * @author Jorge Náder Roa
 * 
 * Creacion de grafos tipo digraph G
 */
public class GraphVizDigraphGPrinter {
    
    public static String URL_DOTPROGRAM = "C:\\Program Files\\Graphviz2.38\\bin\\dot.exe";

    private String OUT_DIRECTORY = "";
    
    
    public GraphVizDigraphGPrinter(String outDirectory) {
        OUT_DIRECTORY = outDirectory;
    }
    
    public int printDotFile(String DotFileUrl, String pictureName){
        
        String dotPath = URL_DOTPROGRAM;
        String tParam = "-Tjpg";
        String tOParam = "-o";

        try{
            String fileInputPath = DotFileUrl;
            String fileOutputPath = OUT_DIRECTORY + pictureName + ".jpg";

            String[] cmd = new String[5];
            cmd[0] = dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;

            Runtime rt = Runtime.getRuntime();

            rt.exec( cmd );

          }catch(Exception ex){
            ex.printStackTrace();
            return 0;
          }   
        
        return 1;
    }   
    
    /**
     * 
     * @param filesUrl The URL of the directory where the files are located.<br/>e.g. C:\\directory\\ for Windws or /home/directory/ in Linux or Mac case
     * @param files A list with the names of all the files to print.<br/>Note: this method assumes a *.dot extension. If the files are not of this extension type, the method will be crash.
     * @return 1 if was succesfull, 0 if not.
     */
    public int printListOfDotFiles(String filesUrl, List<String> files){
        
        System.out.println("Hola Mundo");
        
        String dotPath = URL_DOTPROGRAM;
        String tParam = "-Tjpg";
        String tOParam = "-o";
        
        for(String file : files){
            
            try{
                String fileInputPath = filesUrl + file + ".dot";
                String fileOutputPath = OUT_DIRECTORY + file + ".jpg";

                String[] cmd = new String[5];
                cmd[0] = dotPath;
                cmd[1] = tParam;
                cmd[2] = fileInputPath;
                cmd[3] = tOParam;
                cmd[4] = fileOutputPath;

                Runtime rt = Runtime.getRuntime();

                rt.exec( cmd );

              }catch(Exception ex){
                ex.printStackTrace();
                return 0;
              }   
        }

        return 1;
       
    } 
    
    
}
