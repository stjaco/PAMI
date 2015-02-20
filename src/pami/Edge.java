/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

/**
 *
 * @author jacopo
 */
public class Edge extends GraphElement{
    String source;
    String target;
    
    Edge(String source,String target){
        this.id=source+EDGE_SEPARATOR+target;
        this.source=source;
        this.target=target;
    }
    
    void incrementWeight(){
        this.weight.incrementAndGet();
    }
    
    @Override
    String toGraphML(){
        String ret="<edge id=\""+this.getId()+"\" source=\""+this.source+"\" target=\""+this.target+"\">\n";
        ret+=super.toGraphML();
        ret+="</edge>";
        return ret;
    }

    
}
