/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jacopo
 */
public class IRGraph extends Graph {

    IRGraph() {
        super(true);
    }

    IRGraph(boolean directed) {
        super(directed);
    }

    @Override
    void fill(String file) {
        String[] split = StringUtils.split(file, '/');
        String refId = split[split.length - 2];
        addNode(refId);

        try {
            List<String> lines = IOUtils.readLines(new FileReader(file));
            for (String line : lines) {
                String[] splitline = StringUtils.split(line, ",");
                
                float time = Float.parseFloat(splitline[0]) - 661; //ugly, to take into account offset with video data...
                if (time < 0) {
                    continue;
                }

                String alter = splitline[1];

                if (alter.length() < 2) {
                    alter = "0" + alter;
                }

                addEdge(refId, alter);

            }
        } catch (IOException ex) {
            Logger.getLogger(GraphBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
