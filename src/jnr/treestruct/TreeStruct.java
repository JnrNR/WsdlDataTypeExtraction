//Author: Jorge Náder Roa

package jnr.treestruct;

import java.util.ArrayList;
import java.util.List;


public class TreeStruct {
    
    private Node root;
    
    public TreeStruct(OperationElement rootElement){
        
        root.setFather(null);
        root.setData(rootElement);   
    }
    
    public class Node{
        
        private Node father;
        private List<Node> sons;
        
        private OperationElement data;
        
        public void setData(OperationElement data){
            this.data = data;
        }
        
        public void setFather(Node father){
            this.father = father;
        }
        
        public void addSon(Node son){
            if(sons == null || sons.isEmpty()){
                sons = new ArrayList<Node>();
            }else{
                sons.add(son);
            }          
        }
    }
    
}
