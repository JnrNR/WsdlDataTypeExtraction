/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jnr.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Jnr
 */
public class CompresionDeDatos {
    
    //Depuración
    public static Log log = new Log(true, true, Log.ANSI_YELLOW);
    /////////////////////////////////////////////////
    
    public CompresionDeDatos(){}
    
    public void archivoZip(String rutaArchivo, String nombreZip){
        
        try{
            FileOutputStream fos = new FileOutputStream(nombreZip);
            ZipOutputStream zos = new ZipOutputStream(fos);

            addToZipFile(rutaArchivo, zos);
            
            zos.close();
            fos.close();
            
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public void directorioZip(String rutaDirectorio, String nombreZip){
        File directorio = new File(rutaDirectorio);
        String[] ficheros = directorio.list();
        
        if(nombreZip == null){
            nombreZip = directorio.getName() + ".zip";
        }
                
        try{
            FileOutputStream fos = new FileOutputStream(nombreZip);
            ZipOutputStream zos = new ZipOutputStream(fos);

            if (ficheros == null)
                log.printLogErrorMessage("No hay ficheros en el directorio especificado");
            else { 
                for (int x=0;x<ficheros.length;x++)
                    addToZipFile(rutaDirectorio+"/"+ficheros[x], zos);
            }
            
            zos.close();
            fos.close();
            
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
    }
    
}
