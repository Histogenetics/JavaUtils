package com.histo.jsoncreator.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 0) {
			System.out.println("Argument length should be one atleast");
			return;
		}
		long sumInsert_size = 0;
		long sumNum_full_passes = 0;
		double sumPredicted_accuracy = 0.0;
		int nodeCount = 0;
		long sumInsert_size_filtered = 0;
		long sumNum_full_passes_filtered = 0;
		double sumPredicted_accuracy_filtered = 0.0;
		int nodeCount_filtered = 0;
		
		JsonFactory f = new MappingJsonFactory();
        JsonParser jp = null;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode parentNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
//        parentNode.putArray("zmws").addA
		try {
			jp = f.createJsonParser(new File(args[0]));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JsonToken current = null;
        try {
			current = jp.nextToken();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (current != JsonToken.START_OBJECT) {
            System.out.println("Error: root should be object: quiting.");
            return;
        }
        try {
			while (jp.nextToken() != JsonToken.END_OBJECT) {
			    String fieldName = jp.getCurrentName();
			    // move from field name to field value
			    current = jp.nextToken();
			    if (fieldName.equals("zmws")) {
			        if (current == JsonToken.START_ARRAY) {
			            // For each of the records in the array
			            while (jp.nextToken() != JsonToken.END_ARRAY) {
			                // read the record into a tree model,
			                // this moves the parsing position to the end of it
			            	
			                JsonNode node = jp.readValueAsTree();
			                
			                // And now we have random access to everything in the object
			                int num_full_passes = node.get("num_full_passes").asInt(0);
			                Double predicted_accuracy = node.get("predicted_accuracy").asDouble(0);
			                double compare = Double.compare(predicted_accuracy, 0.99);
			                if(num_full_passes > 2 && compare >= 0.0) {
			                	arrayNode.add(node);
			                	sumInsert_size_filtered += node.get("insert_size").asInt(0);
				                sumNum_full_passes_filtered += node.get("num_full_passes").asInt(0);
				                sumPredicted_accuracy_filtered += node.get("predicted_accuracy").asDouble(0.0);
				                nodeCount_filtered++;
			                }
			                sumInsert_size += node.get("insert_size").asInt(0);
			                sumNum_full_passes += node.get("num_full_passes").asInt(0);
			                sumPredicted_accuracy += node.get("predicted_accuracy").asDouble(0.0);
			                nodeCount++;
//			                System.out.println("field1: " + node.get("status").asText());
//			                System.out.println("field2: " + node.get("wall_end").asDouble());
			            }
			            //Add arrayNode into parentNode
			            parentNode.putArray("zmws").addAll(arrayNode);
			            if(parentNode.size() > 0) {
				            String newFileName = args[0];
				            newFileName = newFileName.replace(".json", "_new.json");
				            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
				            writer.writeValue(new File(newFileName), parentNode);
			            }
			            
			            //Write into text file
			            String header = "HiFi-Yield(bp)|HiFi-ReadQuality|HiFi-Reads|HiFi-NumberofPasses";
			            /*
			             * sumInsert_size == sum of Insert_size
							sumNum_full_passes = sum of Num_full_passes
							qscore
								if qscore < 0 -1
								else
								qscore = qscoresum/nodecount (0.9887753435684865) = nlogn(0.9887753435684865) n-->10
							nodeCount =sum of nodeCount
							medNum_full_passes = (sumNum_full_passes/nodeCount)
			             */
			            String txtFileName = args[0].replace(".json", ".txt");
			            FileWriter myWriter = new FileWriter(txtFileName);
			            myWriter.write(header);
			            long qScore = 0;
			            if(sumPredicted_accuracy < 0) {
			            	qScore = -1;
			            } else {
			            	qScore =  Math.round(Math.log10(1 - sumPredicted_accuracy/nodeCount));
			            }

			            long medNum_full_passes = Math.round(sumNum_full_passes/nodeCount);
			            Long nodeCount_format = Long.parseLong(String.valueOf(medNum_full_passes));
			            Long sumInsert_size_format = Long.parseLong(String.valueOf(sumInsert_size));
			            DecimalFormat formatter = new DecimalFormat("#,###");

			            if(qScore > 0) {
			            	String content = formatter.format(sumInsert_size_format) + "|" + "Q" + Long.toString(qScore) + "|"  + formatter.format(nodeCount_format) + "|" + Long.toString(medNum_full_passes);
			            	myWriter.write("\n");
			            	myWriter.write(content);
			            }
			            
			            if(sumPredicted_accuracy_filtered < 0) {
			            	qScore = -1;
			            } else {
			            	qScore = Math.round(-10 * Math.log10(1 - sumPredicted_accuracy_filtered/nodeCount_filtered));
			            }
			            
			            medNum_full_passes = Math.round(sumNum_full_passes_filtered/nodeCount_filtered);
			            Long nodeCount_filtered_format = Long.parseLong(String.valueOf(nodeCount_filtered));
			            Long sumInsert_size_filtered_format = Long.parseLong(String.valueOf(sumInsert_size_filtered));
			            if(qScore > 0) {
			            	String content = formatter.format(sumInsert_size_filtered_format) + "|" + "Q" + Long.toString(qScore) + "|" + formatter.format(nodeCount_filtered_format) + "|" + Long.toString(medNum_full_passes);
				            myWriter.write("\n");
				            myWriter.write(content);
			            }
			            myWriter.close();
			        } else {
			            System.out.println("Error: records should be an array: skipping.");
			            jp.skipChildren();
			        }
			    } else {
			        System.out.println("Unprocessed property: " + fieldName);
			        jp.skipChildren();
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                    
            
		
	    }
	}

