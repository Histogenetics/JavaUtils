package com.histo.csvTojson.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.histo.csvTojson.model.*;

public class CSVtoJSONConverter {

	public static void main(String arg[]) throws Exception {
		
		if(arg.length == 0) {
			System.out.println("Mismatch number of arguments");
			return;
		}
		String csvFile = arg[0];
		ObjectMapper mapper = new ObjectMapper();
		CsvMapper csvMapper = new CsvMapper();
		File csvFileInputFile = new File(csvFile);

//		if (csvfiles.length > 0) {
//			for (File csvFileInputFile : csvfiles) {

//		File csvFileInputFile = new File("F:\\CSVtoJson\\AN25587853.csv");
				File jsonOutPutFile = new File(csvFileInputFile.getAbsolutePath().replace("csv", "json"));
				try {
					CsvSchema csvSchema = csvMapper.typedSchemaFor(CSVinput.class).withHeader().withColumnSeparator(',')
							.withComments();

					MappingIterator<CSVinput> CSVinput = csvMapper.readerWithTypedSchemaFor(CSVinput.class)
							.with(csvSchema).readValues(csvFileInputFile);

					java.util.List<CSVinput> parsedData = CSVinput.readAll();

					JSONOutputModel jsonOuputModel = new JSONOutputModel();
					ArrayList<JsonSNPsModel> listOfData = new ArrayList<JsonSNPsModel>();
					jsonOuputModel.setSampleId(parsedData.get(0).getSampleId());

					for (CSVinput field : parsedData) {
						JsonSNPsModel jsonSNPsModel = new JsonSNPsModel();
						jsonSNPsModel.setSnpName(field.getSnpName());
						jsonSNPsModel.setObserved1(field.getObserved1());
						jsonSNPsModel.setReads1(field.getReads1());
						jsonSNPsModel.setObserved2(field.getObserved2());
						jsonSNPsModel.setReads2(field.getReads2());
						jsonSNPsModel.setObserved3(field.getObserved3());
						jsonSNPsModel.setReads3(field.getReads3());
						jsonSNPsModel.setObserved4(field.getObserved4());
						jsonSNPsModel.setReads4(field.getReads4());

						listOfData.add(jsonSNPsModel);
					}

					jsonOuputModel.setSNPs(listOfData);

					mapper.writerWithDefaultPrettyPrinter().writeValue(jsonOutPutFile, jsonOuputModel);
//					if (csvFileInputFile.delete())
//						System.out.println(
//								"Deleted :" + csvFileInputFile.getName() + " and created " + jsonOutPutFile.getName());

				} catch (Exception e) {
					throw new Exception("Exception occured and stoped the program"+e.getMessage());
					
				}

//			}
//		} else
//			System.out.println("CSV files not found");
		/*
		 * File input = new File("F:\\CSVtoJson\\AN25587853.csv"); File output = new
		 * File("F:\\CSVtoJson\\AN25587853.json");
		 * 
		 * CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
		 * CsvMapper csvMapper = new CsvMapper(); csvMapper.schemaFor(CSVinput.class);
		 * // Read data from CSV file java.util.List<Object> readAll =
		 * csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();
		 * 
		 * ObjectMapper mapper = new ObjectMapper();
		 * 
		 * // Write JSON formated data to output.json file
		 * mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);
		 * 
		 * // Write JSON formated data to stdout
		 * System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString
		 * (readAll));
		 */

	}
	
	public static Properties getCSVFolderProperities() {
		Properties prop = new Properties();
		try {
//			InputStream input = new FileInputStream(file);
			InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties");
            prop.load(input);

        } catch (IOException ex) {
        	System.err.println("Error in Properties file :"+ ex.getMessage());
            ex.printStackTrace();
        }
		return prop;
		
	}

}
