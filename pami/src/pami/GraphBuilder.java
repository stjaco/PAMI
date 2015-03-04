/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.File;

/**
 *
 * @author jacopo
 */
public class GraphBuilder {

    public GraphBuilder() {
    }
    
    void createDynamicIRGraph(String inPath, String outPath, boolean directed, int timeTh){
        DynamicIRGraph dg=new DynamicIRGraph(directed,timeTh);
        dg.fill(inPath);
        dg.writeToGraphML(outPath);
    }

    void createIRGraph(String inPath, String outFile, boolean directed) {

        IRGraph irGraph = new IRGraph(directed);
        irGraph.setName("infrared");

        File[] folders = new File(inPath).listFiles();

        for (File file : folders) {
            irGraph.fill(file.getAbsolutePath() + "/infrared_slow.csv");
        }
        irGraph.writeGraphML(outFile);
    }

    void createAudioGraph(String inPath, String outFile, boolean directed, float sr) {
        
        AudioGraph audioGraph=new AudioGraph(directed, sr);
        audioGraph.setName("audio");
        
        File[] files=new File(inPath).listFiles();
        
        for(File file : files){
            audioGraph.fill(file.getAbsolutePath());
        }
        
        audioGraph.writeGraphML(outFile);
    }

    void createVideoGraph(String inPath, String outFile, boolean directed) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void createGTGraph(String file,String outfile,boolean directed) {
        GTGraph graph=new GTGraph(directed);
        graph.setName("GT");
        graph.fill(file);
        graph.writeGraphML(outfile);
    }

    void createDynamicGTGraph(String file,String outfile,boolean directed, int timeTh) {
        DynamicGTGraph graph=new DynamicGTGraph(directed,timeTh);
        graph.fill(file);
        graph.writeToGraphML(outfile);
    }
    
    void createDynamicAudioGraph(String inPath,String outfile,boolean directed, int timeTh) {
        DynamicAudioGraph graph=new DynamicAudioGraph(directed, timeTh);
        graph.fill(inPath);
        graph.writeToGraphML(outfile);
    }
}
