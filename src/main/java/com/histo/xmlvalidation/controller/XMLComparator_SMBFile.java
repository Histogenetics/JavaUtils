package com.histo.xmlvalidation.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class XMLComparator_SMBFile {
	public static void main(String args[]) throws FileNotFoundException, SAXException, IOException {
		if(args.length==0) {
			System.out.println("Invalid Arguments Passed");
			System.exit(0);
		}
		try {
			System.out.println("Process started and In Progress...");
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "cloudsync", "H1st0.Cloud");
			//10.201.20.198/Volume1Backup/pbvol2/mani/smrtlink/userdata/074016/Reanalysis/AutoType/
			//10.201.20.198/Volume1Backup/pbvol2/mani/smrtlink_new/userdata/074016/Reanalysis/AutoType/
			//10.201.20.198/Volume1Backup/pbvol2/mani/autoTyperValidator/
			SmbFile version1DataPath = new SmbFile("smb:"+args[0], auth); //oldVersionXMLPath
			SmbFile version2DataPath = new SmbFile("smb:"+args[1], auth); //newVwersionXMLPath
			SmbFile validatorDataPath = new SmbFile("smb:"+args[2], auth); //differenceSavePath
			if (version1DataPath.exists()) {
				Document doc = null;
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true); // never forget this!
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				SmbFile[] jobDataList = version1DataPath.listFiles(data -> data.isDirectory());
				for (SmbFile jobDir : jobDataList) {
					SmbFile[] xmlFile = jobDir.listFiles(file -> file.getName().endsWith(".xml"));
					for (SmbFile xmlFileData : xmlFile) {
//						System.out.println(xmlFileData.getName());
//						System.out.println(xmlFileData.getParent());
						try {
							doc = builder.parse(xmlFileData.getUncPath());
							SmbFile v2BarCodeName = new SmbFile(xmlFileData.getParent());
//							System.out.println(version2DataPath+V2.getName()+"/"+xmlFileData.getName());
							SmbFile v2File = new SmbFile(version2DataPath+v2BarCodeName.getName()+xmlFileData.getName());
							Reader source = new BufferedReader(new InputStreamReader(new SmbFileInputStream(xmlFileData)));
							Reader target = new BufferedReader(new InputStreamReader(new SmbFileInputStream(v2File)));
							// configuring XMLUnit to ignore white spaces
							XMLUnit.setIgnoreWhitespace(true);
							SmbFile differenceSavePath = new SmbFile(validatorDataPath+v2BarCodeName.getName(),auth);
							if(!differenceSavePath.exists())
								differenceSavePath.mkdir();
							SmbFile differenceSaveFilePath = new SmbFile(differenceSavePath+xmlFileData.getName().replace(".xml", ".txt"),auth);
							if(!differenceSaveFilePath.exists())
								differenceSaveFilePath.createNewFile();
							SmbFileOutputStream smbfostoWriteFastaFile = new SmbFileOutputStream(differenceSaveFilePath, false);
							// comparing two XML using XMLUnit in Java
							List<Difference> differences = compareXML(source, target);
							String twoPaths = xmlFileData.getUncPath()+"\n"+v2File.getUncPath()+"\n";
							smbfostoWriteFastaFile.write(twoPaths.getBytes());
							smbfostoWriteFastaFile.write("================================ \n".getBytes());
							// showing differences found in two xml files
							printDifferences(differences, doc,smbfostoWriteFastaFile);
							smbfostoWriteFastaFile.close();

						} catch (Exception e) {
							System.out.println(e.getMessage());
						}

					}
				}
				System.out.println("Completed, Please see the validation files in "+validatorDataPath.getUncPath());
			} else {
				System.out.println("No Data Path " + version1DataPath.getUncPath());
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

	public static void printDifferences(List<Difference> differences, Document doc,SmbFileOutputStream smbfostoWriteFastaFile) throws IOException {
		int totalDifferences = differences.size();
//        System.out.println("===============================");
//		System.out.println("Total differences : " + totalDifferences);
		String diffCountWrite = "Total differences :" + totalDifferences+"\n";
		smbfostoWriteFastaFile.write(diffCountWrite.getBytes());
		smbfostoWriteFastaFile.write("================================ \n".getBytes());
//        System.out.println("================================");
		int diffCount = 1;
		String diffeWithCode = "";
		for (Difference difference : differences) {
			try {
				String sampleBarcodeId = getXpathAttribute(difference.getControlNodeDetail().getXpathLocation(),
						"SampleBarcode", doc);
				String geneName = getXpathAttribute(difference.getControlNodeDetail().getXpathLocation(), "Gene", doc);
				diffeWithCode = "SampleBarcode ID :" + sampleBarcodeId + " and Gene Name :" + geneName +"\n";
//				System.out.println("SampleBarcode ID :" + sampleBarcodeId + " and Gene Name :" + geneName);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			String diffWithDetails = diffCount++ + ". " + difference + "\n";
//			System.out.println(diffCount++ + ". " + difference + "\n");
//			System.out.println("================================");
			smbfostoWriteFastaFile.write(diffeWithCode.getBytes());
			smbfostoWriteFastaFile.write(diffWithDetails.getBytes());
			smbfostoWriteFastaFile.write("================================".getBytes());
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
