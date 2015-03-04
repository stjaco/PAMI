/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jacopo
 */
public class DynamicAudioGraph extends DynamicGraph {

    DynamicAudioGraph(boolean directed, int timeTh) {
        super(directed, timeTh);

    }

    @Override
    void fill(String inPath) {
        File[] files = new File(inPath).listFiles();
        HashMap<Integer,String> frameToFile=new HashMap<Integer,String>(files.length);
        
        for (File file : files) {
            Integer sec=Integer.parseInt(StringUtils.split(file.getName(), "_")[1].split("\\.")[0]);
            frameToFile.put(sec, file.getAbsolutePath());            
        }
        
        TreeSet<Integer> keys=new TreeSet<Integer>(frameToFile.keySet());
        int cnt=0;
        AudioGraph current=new AudioGraph(directed, 0.95f);
        current.setName("audio_"+cnt);
        
        for(Integer key:keys){
            if(Math.floor(key/(timeTh*15))>cnt){
                cnt++;
                graphs.add(current);
                current=new AudioGraph(directed, 0.95f);
                current.setName("audio_"+cnt);
            }
            current.fill(frameToFile.get(key));
        }
    }
}
