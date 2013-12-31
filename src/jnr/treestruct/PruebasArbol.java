//Author: Jorge Náder Roa

package jnr.treestruct;

import java.util.StringTokenizer;


public class PruebasArbol {
    
    public static void main(String[] args){
        
        StringTokenizer tokenizer;
        tokenizer = new StringTokenizer("Hola cara de bola");
        
        System.out.println(tokenizer.countTokens());
        while(tokenizer.hasMoreTokens()){
            
            System.out.println("\n"+ tokenizer.nextToken());
            System.out.println(" " + tokenizer.hasMoreTokens());
            System.out.println(" " + tokenizer.hasMoreTokens());
            System.out.println(" " + tokenizer.hasMoreTokens());
            System.out.println(" " + tokenizer.hasMoreTokens());
        }
        
    }
    
}
