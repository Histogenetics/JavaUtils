package com.histo.pacbiosnp.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Scanner;

import com.histo.pacbiosnp.implementation.FileCopyImplementation;
import com.histo.pacbiosnp.implementation.WriteBarCodeJson;
import com.histo.pacbiosnp.model.BarcodeInputs;

public class PacBioSNPEntryPoint {
	public static void main(String arg[]) throws SQLException, FileNotFoundException, MalformedURLException {
		//com.histo.pacbiosnp.process.PacBioSNPEntryPoint
		if(arg.length == 2) {
			try {
			BarcodeInputs barcodeData =  WriteBarCodeJson.BarcodeIntoJsonFile(arg);
//			FileCopyImplementation.FastqFileCopyBySMB(arg,barcodeData);
			FileCopyImplementation.FastaFileCopyBySMB(arg,barcodeData);
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		else {
			System.err.println("Invalid Params");
		}
		
	}

}
