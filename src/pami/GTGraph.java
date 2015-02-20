/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jacopo
 */
public class GTGraph extends Graph {

    static final HashMap<String, String> oldToNewId = new HashMap<String, String>(18);

    public GTGraph(boolean directed) {
        super(directed);
        for (int i = 1; i < 19; ++i) {
            if (i < 10) {
                addNode("0" + i);
            } else {
                addNode("" + i);
            }

        }
        //ugly mapping, 'cause ids are different in the different datasets..
        if (oldToNewId.isEmpty()) {
            oldToNewId.put("119", "01");
            oldToNewId.put("132", "02");
            oldToNewId.put("140", "03");
            oldToNewId.put("169", "04");
            oldToNewId.put("177", "05");
            oldToNewId.put("180", "06");
            oldToNewId.put("216", "07");
            oldToNewId.put("238", "08");
            oldToNewId.put("241", "09");
            oldToNewId.put("261", "10");
            oldToNewId.put("262", "11");
            oldToNewId.put("267", "12");
            oldToNewId.put("286", "13");
            oldToNewId.put("307", "14");
            oldToNewId.put("313", "15");
            oldToNewId.put("350", "16");
            oldToNewId.put("351", "17");
            oldToNewId.put("353", "18");
        }
    }

    void fillLine(String line) {
        if (line.contains("[")) {
            List<String> groups = getGroups(line);
            for (String group : groups) {
                String[] ids = StringUtils.split(group);
                for (int i = 0; i < ids.length; ++i) {
                    addNode(oldToNewId.get(ids[i]));
                    for (int j = i + 1; j < ids.length; ++j) {
                        addEdge(oldToNewId.get(ids[i]), oldToNewId.get(ids[j]));
//                                System.out.println("added "+PAMI.oldToNewId.get(ids[i])+"-"+PAMI.oldToNewId.get(ids[j]));
                    }
                }
            }
        }
    }

    @Override
    void fill(String file) {
        try {
            List<String> lines = IOUtils.readLines(new FileReader(file));
            for(int i=0;i<5;++i)
                lines.remove(0);    //remove 5 header lines
            for (String line : lines) {
                if (line.contains("[")) {
                    List<String> groups = getGroups(line);
                    for (String group : groups) {
                        String[] ids = StringUtils.split(group);
                        for (int i = 0; i < ids.length; ++i) {
                            addNode(oldToNewId.get(ids[i]));
                            for (int j = i + 1; j < ids.length; ++j) {
                                addEdge(oldToNewId.get(ids[i]), oldToNewId.get(ids[j]));
//                                System.out.println("added "+PAMI.oldToNewId.get(ids[i])+"-"+PAMI.oldToNewId.get(ids[j]));
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GTGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<String> getGroups(String line) {

        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(line);
        List<String> groups = new ArrayList<String>();

        int pos = -1;
        while (matcher.find(pos + 1)) {
            pos = matcher.start();
            groups.add(matcher.group(1).trim());
        }

        return groups;
    }
}
