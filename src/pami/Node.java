/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

/**
 *
 * @author jacopo
 */
public class Node extends GraphElement{
    
    Node(String id){
        this.id=id;
    }
    
    @Override
    String toGraphML(){
        String ret="<node id=\""+this.getId()+"\">\n";
//        ret+=super.toGraphML();
        ret+="</node>";
        return ret;
    }
}
