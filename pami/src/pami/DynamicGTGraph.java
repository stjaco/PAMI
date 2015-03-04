/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jacopo
 */
public class DynamicGTGraph extends DynamicGraph {

    DynamicGTGraph(boolean directed, int timeTh) {
        super(directed, timeTh);

    }

    @Override
    void fill(String file) {
        try {
            List<String> lines = IOUtils.readLines(new FileReader(file));

            for(int i=0;i<5;++i)
                lines.remove(0);    //remove 5 header lines
            
            int cnt=0;
            int sliceFrames=timeTh/3;
            
            GTGraph current=new GTGraph(directed);
            int id=0;
            current.setName("GT_"+id);

            for (String line:lines) {
                if(!line.contains("["))
                    continue;
                if(cnt%sliceFrames==0
                        &&cnt!=0){
                    graphs.add(current);
                    id++;
                    current=new GTGraph(directed);
                    current.setName("GT_"+id);
                }
                current.fillLine(line);
                ++cnt;
                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicGTGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DynamicGTGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
