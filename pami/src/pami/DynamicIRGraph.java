/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jacopo
 */
public class DynamicIRGraph extends DynamicGraph {

    DynamicIRGraph(boolean directed, int timeTh) {
        super(directed, timeTh);

    }

    @Override
    void fill(String inPath) {
        File[] folders = new File(inPath).listFiles();
        HashMap<String, Graph> map = new HashMap<String, Graph>();

        for (File file : folders) {
            String[] split = StringUtils.split(file.getAbsolutePath(), '/');
            String refId = split[split.length - 1];
            if (refId.length() < 2) {
                refId = "0" + refId;
            }

            try {
                List<String> lines = IOUtils.readLines(new FileReader(file /* + "/infrared_slow.csv"*/));
                for (String line : lines) {
                    String[] splitline = StringUtils.split(line, ",");
                    float time = Float.parseFloat(splitline[0])-661;
                    if(time<0)
                        continue;
                    String graphId = "ir_" + (int) Math.floor(time / timeTh);
                    if(graphId.contains("-"))
                        System.out.println("cazz");
                    if (!map.containsKey(graphId)) {
                        map.put(graphId, new IRGraph(directed));
                        map.get(graphId).setName(graphId);
                        for (int i = 1; i < 19; ++i) {
                            if(i<10)
                                map.get(graphId).addNode("0"+i);
                            else map.get(graphId).addNode(""+i);
                        }
                    }

                    String alter = splitline[1];
                    if (alter.length() < 2) {
                        alter = "0" + alter;
                    }

                    map.get(graphId).addEdge(refId, alter);
                }
            } catch (IOException ex) {
                Logger.getLogger(DynamicIRGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            TreeSet<String> keys = new TreeSet<String>(map.keySet());
            for (String key : keys) {
                graphs.add(map.get(key));
            }
        }
    }
}
