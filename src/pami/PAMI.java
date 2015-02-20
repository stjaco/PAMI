/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pami;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author jacopo
 */
public class PAMI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        aggregateFeats("/media/storage/running_papers/TPAMI/new_feats_IR",
//                "/media/storage/running_papers/TPAMI/new_featsIR.csv");
//        aggregateFeats("/media/storage/running_papers/TPAMI/new_feats_AUDIO",
//                "/media/storage/running_papers/TPAMI/new_featsAUDIO95.csv");

        aggregateFeats("/media/storage/running_papers/TPAMI/new_featsGT",
                "/media/storage/running_papers/TPAMI/new_featsGT120.csv");

//        GraphBuilder gb = new GraphBuilder();
//
//        gb.createGTGraph("/media/storage/running_papers/TPAMI/GT_NEW/SALSA-GT.txt", "/media/storage/running_papers/TPAMI/static/GT/SALSA-GT.graphml", false);
//        gb.createDynamicGTGraph("/media/storage/running_papers/TPAMI/GT_NEW/SALSA-GT.txt", "/media/storage/running_papers/TPAMI/dyn/GT", false, 120);

//        gb.createIRGraph("/media/storage/running_papers/TPAMI/IR_NEW", "static/ir/ir.graphml", false);
//        gb.createDynamicIRGraph("/media/storage/running_papers/TPAMI/IR_NEW", "/media/storage/running_papers/TPAMI/dyn/ir", false, 120);
//
//        gb.createAudioGraph("/media/storage/running_papers/TPAMI/AUDIO_NEW","static/audio/audio.graphml",false);
//        gb.createDynamicAudioGraph("/media/storage/running_papers/TPAMI/AUDIO_NEW", "/media/storage/running_papers/TPAMI/dyn/audio", false, 120);

    }

    private static void aggregateFeats(String path, String outfile) {
        try {
            HashMap<Integer, List<String>> valueMap = new HashMap<Integer, List<String>>();

            File[] files = new File(path).listFiles();
            Arrays.sort(files);
            List<String> header = new ArrayList<String>();

            for (File file : files) {
                System.out.println("processing " + file.getName());
                if (file.getName().startsWith("dyn")) {
                    List<String> lines = IOUtils.readLines(new FileReader(file.getAbsolutePath()));
                    HashMap<Integer, DescriptiveStatistics> statsMap = new HashMap<Integer, DescriptiveStatistics>(18);

                    for (int i = 1; i < 19; i++) {
                        statsMap.put(i, new DescriptiveStatistics());
                    }

                    for (String line : lines) {
                        String[] split = StringUtils.split(line, ",");

                        for (int i = 1; i < 19; i++) {
                            if (split[i - 1].equalsIgnoreCase("NaN")) {
                                continue;
                            }
                            double value = Double.parseDouble(split[i - 1]);
                            statsMap.get(i).addValue(value);
                        }
                    }

                    String h = file.getName().split("\\.")[0];
                    header.add(h + "_mean");
                    header.add(h + "_sd");
                    header.add(h + "_median");

                    for (int id : statsMap.keySet()) {
                        if (!valueMap.containsKey(id)) {
                            valueMap.put(id, new ArrayList<String>());
                        }
                        valueMap.get(id).add("" + statsMap.get(id).getMean());
                        valueMap.get(id).add("" + statsMap.get(id).getStandardDeviation());
                        valueMap.get(id).add("" + statsMap.get(id).getPercentile(50));
                    }
                } else {
                    String line = IOUtils.toString(new FileReader(file));
                    String[] splitline = StringUtils.split(line, ",");
                    String h = file.getName().split("\\.")[0];
                    header.add(h);
                    assert (splitline.length == 18);

                    for (int i = 0; i < splitline.length; i++) {
                        if (!valueMap.containsKey(i + 1)) {
                            valueMap.put(i + 1, new ArrayList<String>());
                        }
                        valueMap.get(i + 1).add(splitline[i].trim());
                    }
                }
            }

            TreeSet<Integer> keys = new TreeSet<Integer>(valueMap.keySet());

            List<String> output = new ArrayList<String>();
            output.add(StringUtils.join(header, ","));

            for (int key : keys) {
                System.out.println("doing subject: " + key);
                String string = StringUtils.join(valueMap.get(key), ",");
                output.add(string);
            }

            System.out.println("output size: " + output.size());

            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
            for (String s : output) {
                writer.write(s);
                writer.newLine();
            }
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(PAMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
