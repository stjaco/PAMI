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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author jacopo
 */
public class PAMI {

    /**
     * @param args the command line arguments 
     */
    public static void main(String[] args) throws ParseException {
    	
    	// create Options object
    	Options options = new Options();
    	

    	// add input files option
    	Option option = new Option("if", false, "Set the input files or folders");
    	option.setArgName("paths");
    	option.setArgs(Option.UNLIMITED_VALUES);	// Set option if to take 1 to oo arguments
    	option.setType(String.class);
    	options.addOption(option);
    	
    	// add output files option
    	option = new Option("of", false, "set the output files or folders");
    	option.setArgName("paths");
    	option.setArgs(Option.UNLIMITED_VALUES);	// Set option if to take 1 to oo arguments
    	option.setType(String.class);
    	options.addOption(option);
    	
    	options.addOption("audio", false, "works with audio .csv files");
    	options.addOption("ir", false, "works with ir .csv files");
    	options.addOption("gt", false, "works with GT .csv files");
    	options.addOption("dg", false, "Create dynamic graph. Default false");
    	options.addOption("d", false, "Directed graph. Default false");
    	options.addOption("af", false, "Aggregate input folders in output files, In order to aggregate features obtained from a dynamic graph"
    			+ "the files must start with dyn e.g. dyn_betweenness.csv");
    	
    	option = new Option("sd", true, "Set thresholds for slice duration");
    	option.setType(Number.class);
    	option.setArgName("number");
    	options.addOption(option);
    	
    	option = new Option("sr", true, "Set data selection rate");
    	option.setType(Number.class);
    	option.setArgName("number");
    	options.addOption(option);
    	
    	options.addOption("h", "help", false, "");
    	
    	CommandLineParser parser = new GnuParser();
    	CommandLine cmd = parser.parse( options, args);
    	
    	if(cmd.hasOption("h"))
    	{
        	HelpFormatter formatter = new HelpFormatter();	// automatically generate the help statement
        	formatter.printHelp("myapp",/* "header", */options,/* "footer", */ true);
    	}
    	try
    	{
			String[] ifPaths = cmd.getOptionValues("if");
			String[] ofPaths = cmd.getOptionValues("of");
			
			if(ifPaths.length == ofPaths.length)
			{
				if(!cmd.hasOption("af"))
				{
					GraphBuilder gb = new GraphBuilder();
					boolean directed = false;
					
					if(cmd.hasOption("d"))
						directed = true;
					
					if(cmd.hasOption("dg"))
					{
						int slice_duration = 120;
						if(cmd.hasOption("sd"))
							slice_duration = ((Number)cmd.getParsedOptionValue("sd")).intValue();
						
						if(cmd.hasOption("audio"))
							gb.createDynamicAudioGraph	(ifPaths[0],ofPaths[0],directed,slice_duration);
						
						if(cmd.hasOption("ir"))
					        gb.createDynamicIRGraph		(ifPaths[0],ofPaths[0],directed,slice_duration);
						
						if(cmd.hasOption("gt"))
							gb.createDynamicGTGraph		(ifPaths[0],ofPaths[0],directed,slice_duration);
				        
				        
					}
					else
					{
						float	sr = 0.95f;
						
						if(cmd.hasOption("sr"))
							sr = ((Number)cmd.getParsedOptionValue("sr")).floatValue();

						if(cmd.hasOption("gt"))
							gb.createGTGraph		(ifPaths[0],ofPaths[0],directed);
						if(cmd.hasOption("ir"))
					        gb.createIRGraph		(ifPaths[0],ofPaths[0],directed);
						if(cmd.hasOption("audio"))
					        gb.createAudioGraph		(ifPaths[0],ofPaths[0],directed, sr);    					   					
					}
				}
				else
				{
					for(int c=0; c<ifPaths.length; c++)
					{
						aggregateFeats(ifPaths[c],ofPaths[c]);
					}
				}
			}
			else
				System.out.println("PLEASE INSERT AN EQUAL NUMBER OF INPUT AND OUTPUT FILES !!!");
    	} catch (ParseException e)
    	{
    		e.printStackTrace();
    	}

        
    	
//        aggregateFeats("/media/storage/running_papers/TPAMI/new_feats_IR",
//                "/media/storage/running_papers/TPAMI/new_featsIR.csv");
//        aggregateFeats("/media/storage/running_papers/TPAMI/new_feats_AUDIO",
//                "/media/storage/running_papers/TPAMI/new_featsAUDIO95.csv");

//        aggregateFeats("/media/storage/running_papers/TPAMI/new_featsGT","/media/storage/running_papers/TPAMI/new_featsGT120.csv");

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
