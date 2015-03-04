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
public class AudioGraph extends Graph {

    float th;

    public AudioGraph(boolean directed, float sr) {
        super(directed);
        this.th = sr;
    }

    @Override
    void fill(String file) {
        try {
            List<String> lines = IOUtils.readLines(new FileReader(file));
            for (int i = 1; i <= 18; i++) {
                String refNode = (i < 10) ? "0" + i : "" + i;
                addNode(refNode);
            }
            for (int i = 0; i < lines.size(); ++i) {
                String[] split = StringUtils.split(lines.get(i),",");

                String refId = (i + 1) < 10 ? "0" + (i+1) : "" + (i+1);

                for (int j = 0; j < split.length; ++j) {
                    if (i == j
                            ||Float.parseFloat(split[j])<th) {
                        continue;
                    }
                    String alter= (j + 1) < 10 ? "0" + (j+1) : "" + (j+1);

                    addEdge(refId,alter);

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AudioGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
