/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jacopo
 */
public abstract class Graph {

    final static String GRAPHML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n"
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n";
    final static String ATTR_WEIGHT = "weight";
    HashMap<String, Node> nodes = new HashMap<String, Node>();
    HashMap<String, Edge> edgeMap = new HashMap<String, Edge>();
    private String name = "graph";
    boolean directed;

    Graph(boolean directed) {
        this.directed = directed;
    }

    abstract void fill(String file);

    public void writeGraphML(String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(GRAPHML_HEADER);
            writer.write("<key id=\"" + ATTR_WEIGHT + "\" for=\"edge\" attr.name=\"" + ATTR_WEIGHT + "\" attr.type=\"double\"/>\n");
            writer.write("<graph id=\"" + getName() + "\" edgedefault=\"" + (directed?"directed":"undirected") + "\">\n");
            Map<String, Node> sortedNodes = new TreeMap<String, Node>(nodes);
            for (Node node : sortedNodes.values()) {
                writer.write(node.toGraphML() + "\n");
            }
            System.out.println("nodes: " + sortedNodes.size());
            
            for (Edge edge : edgeMap.values()) {
                writer.write(edge.toGraphML() + "\n");
            }
            
            writer.write("</graph>\n</graphml>");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addNode(String ref){        
        if (!nodes.containsKey(ref)) {
            nodes.put(ref, new Node(ref));
        }
    }

    public void addEdge(String ref, String alter) {
        Edge edge = new Edge(ref, alter);
        if (directed) {
            if (!edgeMap.containsKey(edge.id)) {
                edgeMap.put(edge.getId(), edge);
            } else {
                edgeMap.get(edge.getId()).incrementWeight();
            }
        } else {
            Edge alterEdge = new Edge(alter, ref);
            if (edgeMap.containsKey(edge.getId())) {
                edgeMap.get(edge.getId()).incrementWeight();
            } else if (edgeMap.containsKey(alterEdge.getId())) {
                edgeMap.get(alterEdge.getId()).incrementWeight();
            } else {
                edgeMap.put(edge.getId(), edge);
            }
        }
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
