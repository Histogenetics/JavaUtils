package com.histo.csvTojson.model;

import java.util.ArrayList;

public class JSONOutputModel {
	private String sampleId;
	private ArrayList<JsonSNPsModel> SNPs;
	
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public ArrayList<JsonSNPsModel> getSNPs() {
		return SNPs;
	}
	public void setSNPs(ArrayList<JsonSNPsModel> sNPs) {
		SNPs = sNPs;
	}
	
}
