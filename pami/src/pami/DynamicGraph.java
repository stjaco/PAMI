/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacopo
 */
abstract class DynamicGraph {
    List<Graph> graphs=new ArrayList<Graph>();
    boolean directed=false;
    int timeTh;
    
    DynamicGraph(boolean directed,int timeTh){
        this.directed=directed;
        this.timeTh=timeTh;
    }
    
    abstract void fill(String file);
    
    void writeToGraphML(String outpath){
        if(!outpath.endsWith("/"))
            outpath+="/";
        for(Graph g:graphs){
            g.writeGraphML(outpath+g.getName()+".graphml");
        }
    }
}
