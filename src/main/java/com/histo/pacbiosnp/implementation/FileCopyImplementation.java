package com.histo.pacbiosnp.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.histo.pacbiosnp.model.BarcodeInputs;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class FileCopyImplementation {

	public static String FileCopy(String[] args,BarcodeInputs barcodeData) {
		String result = "";
		try {
			result = FastaFileCopyBySMB(args,barcodeData);
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage();
		}
//		jdbcTemplate.batchUpdate(result, null)
		return result;
	}

	public static String FastaFileCopyBySMB(String[] args,BarcodeInputs barcodeData) throws MalformedURLException {
//		barcodeData.setExperimentName("PAC042321-FCGR-01");
		//Volume1Backup\pbvol2\mani\074064
		String sourceurl = "smb:" + args[0]+"Reanalysis"+"/"+args[1]+"/";
//		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "test@histoindia.com", "Histo123.");
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "cloudsync",
				"H1st0.Cloud");
		SmbFile sourcedir = new SmbFile(sourceurl, auth);
//		SmbFile destFolder = new SmbFile("smb://10.201.100.103/test_folder/Newfolder_test/",auth);
		try {
			if (sourcedir.exists()) {
				SmbFile destFolder = new SmbFile( //// replace PAC042321-FCGR-01 to barcodeData.getExperimentName()
						"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/", auth);
					if (!destFolder.exists()) {
						System.out.println(destFolder.getUncPath() +"directory not presented");
						destFolder.mkdirs();
						System.out.println("directory created.");
					}
					//copy the file to read and trim operation
					SmbFile createFileName = new SmbFile(
								"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/"+args[1]+"_copy"+".fasta", auth);
					if(!createFileName.exists()) {
						createFileName.createNewFile();
					}
					
					SmbFile createJsonFile = new SmbFile(
							"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/"+args[1]+".json", auth);	
					if(!createJsonFile.exists())
						createJsonFile.createNewFile();
					ObjectMapper mapper = new ObjectMapper();
					String jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(barcodeData);
					SmbFileOutputStream smbfos = new SmbFileOutputStream(createJsonFile);
					smbfos.write(jsonData.getBytes());
					smbfos.close();
					System.out.println(createJsonFile.getUncPath()+" file created.");
					
					SmbFile[] filetoCopy = sourcedir.listFiles(f -> f.getName().startsWith("amplicon_analysis.fasta"));
					if(filetoCopy.length > 0)
					{
					try {
						System.out.println("Entered into file copy..");
						filetoCopy[0].copyTo(createFileName);
						System.out.println(filetoCopy[0].getUncPath() +" fasta File copied.");
//						SmbFileInputStream smbIn = new SmbFileInputStream(createFileName);
						SmbFile createFileNameOrginal = new SmbFile(
								"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/"+args[1]+".fasta", auth);
						if(!createFileNameOrginal.exists())
							createFileNameOrginal.createNewFile();
						
						Scanner reader = new Scanner(new InputStreamReader(new SmbFileInputStream(createFileName)));
						SmbFileOutputStream smbfostoWriteFastaFile = new SmbFileOutputStream(createFileNameOrginal, false);
						String line;
						while(reader.hasNextLine()) {
							try {
							line = reader.nextLine(); //firstline starts with >
//							line = reader.nextLine().substring(0, abc.lastIndexOf(","));
							if(line.contains("_noise"))
								smbfostoWriteFastaFile.write(line.substring(0, line.lastIndexOf("_")).getBytes());
							else
								smbfostoWriteFastaFile.write(line.getBytes());
							smbfostoWriteFastaFile.write("\n".getBytes());
							smbfostoWriteFastaFile.write(reader.nextLine().getBytes()); //nextline will be sequence
							smbfostoWriteFastaFile.write("\n".getBytes());
							}
							catch (Exception e) {
								System.err.println(e.getMessage());
							}
						}
						reader.close();
						smbfostoWriteFastaFile.close();
						System.out.println("fasta file updated with string trim operation");
						createFileName.delete();
						System.out.println("Done");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					}
					else {
						System.err.println("amplicon_analysis.fasta File not Found in "+sourcedir.getUncPath());
					}
				}
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return "Success";
	}
	
	public static String FastqFileCopyBySMB(String[] args,BarcodeInputs barcodeData) throws MalformedURLException {
//		barcodeData.setExperimentName("PAC042321-FCGR-01");
		//Volume1Backup\pbvol2\mani\074064
		String sourceurl = "smb:" + args[0]+"Reanalysis"+"/"+args[1]+"/";
//		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "test@histoindia.com", "Histo123.");
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "cloudsync",
				"H1st0.Cloud");
		SmbFile sourcedir = new SmbFile(sourceurl, auth);
//		SmbFile destFolder = new SmbFile("smb://10.201.100.103/test_folder/Newfolder_test/",auth);
		try {
			if (sourcedir.exists()) {
				SmbFile destFolder = new SmbFile( //// replace PAC042321-FCGR-01 to barcodeData.getExperimentName()
						"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/", auth);
					if (!destFolder.exists()) {
						destFolder.mkdirs();
						System.out.println(destFolder.getUncPath()+" directory created.");
					}
					
					SmbFile createFileName = new SmbFile(
								"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/"+args[1]+".fastq", auth);
					if(!createFileName.exists())
						createFileName.createNewFile();
					
					SmbFile createJsonFile = new SmbFile(
							"smb:" + args[0] + "SNP/"+barcodeData.getExperimentName()+"/"+args[1]+"/"+args[1]+".json", auth);	
					if(!createJsonFile.exists())
						createJsonFile.createNewFile();
					ObjectMapper mapper = new ObjectMapper();
					String jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(barcodeData);
					SmbFileOutputStream smbfos = new SmbFileOutputStream(createJsonFile);
					smbfos.write(jsonData.getBytes());
					smbfos.close();
//					createJsonFile.write
					System.out.println(createJsonFile.getUncPath()+" file created");
					
					SmbFile[] filetoCopy = sourcedir.listFiles(f -> f.getName().startsWith("amplicon_analysis.fastq"));
					if(filetoCopy.length > 0) {
					try {
					filetoCopy[0].copyTo(createFileName);
						System.out.println(filetoCopy[0].getUncPath()+" file copied.");
						System.out.println("Done");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					}
					else {
						System.err.println("amplicon_analysis.fasta File not Found in "+sourcedir.getUncPath());
					}
				}
				
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getMessage());
		}
		return "Success";
	}
}
