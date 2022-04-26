package com.histo.pacbiosnp.implementation;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.histo.pacbiosnp.dbsetup.SqlConnectionSetup;
import com.histo.pacbiosnp.model.BarcodeInputs;

public class WriteBarCodeJson {
	
	public static BarcodeInputs BarcodeIntoJsonFile(String[] args) throws SQLException {
		BarcodeInputs barcode = new BarcodeInputs();
//		Connection con = getDBConnection();
		Connection con = SqlConnectionSetup.getConnection();
		if(con !=null) {
			String[] jobSplit = args[0].split("/");
			String splitedJobId = jobSplit[jobSplit.length - 1];
			int jobId = Integer.parseInt(splitedJobId.substring(1));
			String splitedBarCodeId = args[1].split("--")[0];
			int barCodeId = Integer.parseInt(splitedBarCodeId.substring(1));
		try {
			CallableStatement cstmt = con
					.prepareCall("exec PacBioSNPGetBarcodeData " + barCodeId + "," +jobId+ "");
			ResultSet rs1 = cstmt.executeQuery();
				while (rs1.next()) {
					barcode.setBarcodeID((rs1.getString("barcodeID")));
					barcode.setExperimentID((rs1.getString("experimentID")));
					barcode.setExperimentName((rs1.getString("experimentName")));
					barcode.setJobName((rs1.getString("jobName")));
					barcode.setSampleID((rs1.getString("sampleID")));
				}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} 
		}
		else {
			System.err.println("DB connection issue");
		}
		return barcode;
	}


}
