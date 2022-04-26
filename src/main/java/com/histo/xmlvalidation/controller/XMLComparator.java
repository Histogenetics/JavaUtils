package com.histo.xmlvalidation.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FilenameUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * Java program to compare two XML files using XMLUnit example
 * 
 * @author Javin Paul
 */
public class XMLComparator {

	public static void main(String args[]) throws FileNotFoundException, SAXException, IOException {
		try {
//			String systemPath = System.getProperty("user.dir");
//			String errorLogDetailpath = systemPath + File.separator + "configuration" + File.separator
//					+ "inputPathConfiguration.properties";
//			Properties prop = getURLProperities(errorLogDetailpath);
//			System.out.println(prop.getProperty("oldAutoTyperPath"));
//			System.out.println(prop.getProperty("newAutoTyperPath"));
//			System.out.println(prop.getProperty("discrepancySavePath"));

			File[] directories = new File(args[0]).listFiles(file -> file.isDirectory());
			for (File getDirectory : directories) {
				File[] listOfXMLFiles = new File(getDirectory.getAbsolutePath())
						.listFiles(file -> file.isFile() && file.getName().endsWith(".xml"));
				for (File getXmlFile : listOfXMLFiles) {
//				String xmlVersion1Extension = FilenameUtils.getExtension(getXmlFile.getAbsolutePath());
//				String xmlVersion2Extension = FilenameUtils.getExtension(args[1]);

//				if (xmlVersion1Extension.equals("xml") && xmlVersion2Extension.equals("xml")) {
//					Path path = Paths.get(args[0]);
//					Path path2 = Paths.get(args[1]);
//					if (Files.exists(path) && Files.exists(path2)) {
					File version1InputFile = new File(getXmlFile.getAbsolutePath());
					File version2InputFile = new File(args[1] + File.separator
							+ getXmlFile.getParentFile().getName() + File.separator + getXmlFile.getName());

					Document doc = null;
					try {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						factory.setNamespaceAware(true); // never forget this!
						DocumentBuilder builder = factory.newDocumentBuilder();
						doc = builder.parse(version1InputFile.getAbsolutePath());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

					// reading two xml file to compare in Java program
//        FileInputStream fis1 = new FileInputStream("C:\\\\Users\\\\manivasagamm\\\\Desktop\\\\Root.xml");
//        FileInputStream fis2 = new FileInputStream("C:\\\\Users\\\\manivasagamm\\\\Desktop\\\\Root2.xml");
					try {
						FileInputStream fis1 = new FileInputStream(version1InputFile.getAbsolutePath()); // version 1
																											// xml
						FileInputStream fis2 = new FileInputStream(version2InputFile.getAbsolutePath()); // version 2
																											// xml

						// using BufferedReader for improved performance
						BufferedReader source = new BufferedReader(new InputStreamReader(fis1));
						BufferedReader target = new BufferedReader(new InputStreamReader(fis2));

						// configuring XMLUnit to ignore white spaces
						XMLUnit.setIgnoreWhitespace(true);

						// comparing two XML using XMLUnit in Java
						List<Difference> differences = compareXML(source, target);

						// Instantiating the File class
//        File file = new File(args[2]+"\\XMLDifference.txt");
						// Instantiating the PrintStream class
						File file = new File(args[2] + File.separator
								+ getXmlFile.getParentFile().getName() + File.separator + version1InputFile.getName()
								+ ".txt");
						file.getParentFile().mkdirs(); // Will create parent directories if not exists
						PrintStream stream = new PrintStream(new FileOutputStream(file, false));
						System.setOut(stream);
						System.out.println("===============================");
						System.out.println(
								version1InputFile.getAbsolutePath() + "  " + version2InputFile.getAbsolutePath());
						System.out.println("===============================");
						// showing differences found in two xml files
						printDifferences(differences, doc);
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
//					} else {
//						System.err.println("Inputs files are not Found.");
//					}
//				} else {
//					System.err.println("Inputs files are not xml format.");
//				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Difference> compareXML(Reader source, Reader target) throws SAXException, IOException {

		// creating Diff instance to compare two XML files
		Diff xmlDiff = new Diff(source, target);

		// for getting detailed differences between two xml files
		DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff);

		return detailXmlDiff.getAllDifferences();
	}

	public static void printDifferences(List<Difference> differences, Document doc) {
		int totalDifferences = differences.size();
//        System.out.println("===============================");
		System.out.println("Total differences : " + totalDifferences);
//        System.out.println("================================");
		int diffCount = 1;
		for (Difference difference : differences) {
//        	System.out.println(difference.getControlNodeDetail().getXpathLocation());
//        	System.out.println(difference.getControlNodeDetail().getNode().getTextContent());
//        	String nodeDetails = printNode(difference.getControlNodeDetail().getNode());
//        	System.out.println(nodeDetails);
			try {
				String sampleBarcodeId = getXpathAttribute(difference.getControlNodeDetail().getXpathLocation(),
						"SampleBarcode", doc);
				String geneName = getXpathAttribute(difference.getControlNodeDetail().getXpathLocation(), "Gene", doc);

				System.out.println("SampleBarcode ID :" + sampleBarcodeId + " and Gene Name :" + geneName);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out.println(diffCount++ + ". " + difference + "\n");
			System.out.println("================================");
			// text file
		}
	}

	public static String getXpathAttribute(String xPathValue, String nodeName, Document doc) {
		StringBuilder sb = new StringBuilder();
		try {
			String[] splitValues = xPathValue.split("/");
			for (String val : splitValues) {
				if (val.contains(nodeName)) {
					sb.append(val).append("/");
					break;
				} else {
					sb.append(val).append("/");
				}

			}
			if (nodeName.contains("Gene"))
				sb.append("@Name");
			else
				sb.append("@ID");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
//        System.out.println(sb);
		String attributeVal = null;
		try {
			XPathExpression xp = XPathFactory.newInstance().newXPath().compile(sb.toString());
			attributeVal = xp.evaluate(doc);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return attributeVal;

	}

	public synchronized static Properties getURLProperities(String file) {
		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream(file);
			prop.load(input);

		} catch (IOException ex) {
			System.err.println("errorHandler :" + ex.getMessage());
			ex.printStackTrace();
		}
		return prop;

	}
}
