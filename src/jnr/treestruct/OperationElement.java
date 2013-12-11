//Author: Jorge Náder Roa

package jnr.treestruct;


public class OperationElement {
    private String name;
    private String elementType;
    private String dataType;
    
    public OperationElement(String name, String elementType, String dataType){
        this.name = name;
        this.elementType = elementType;
        this.dataType = dataType;
    }
    
    public String getName(){
        return name;
    }
    
    public String getElementType(){
        return elementType;
    }
    
    public String getDataType(){
        return dataType;
    }
    
    
    @Override
    public boolean equals(Object o){
       
        if(o instanceof OperationElement){
            OperationElement object = (OperationElement)o; 
            
            if(name.equals(object.getName()) && elementType.equals(object.elementType) && dataType.equals(object.dataType)){
                return true;
            }else{
                return false;
            }

        }else{
            System.err.println("The object is not an OperationElement object");
            return false;
        }
    }
}
