package com.histo.csvTojson.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"experimentName", "sampleId", "snpName", "observed1", "reads1", 
	"observed2", "reads2", "observed3", "reads3", "observed4", "reads4"})
public class CSVinput {
	
	private String experimentName;
	private String sampleId;
	private String snpName;
	private String observed1;
	private String reads1;
	private String observed2;
	private String reads2;
	private String observed3;
	private String reads3;
	private String observed4;
	private String reads4;
	
	public String getExperimentName() {
		return experimentName;
	}
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getSnpName() {
		return snpName;
	}
	public void setSnpName(String snpName) {
		this.snpName = snpName;
	}
	public String getObserved1() {
		return observed1;
	}
	public void setObserved1(String observed1) {
		this.observed1 = observed1;
	}
	public String getReads1() {
		return reads1;
	}
	public void setReads1(String reads1) {
		this.reads1 = reads1;
	}
	public String getObserved2() {
		return observed2;
	}
	public String getReads2() {
		return reads2;
	}
	public void setReads2(String reads2) {
		this.reads2 = reads2;
	}
	public void setObserved2(String observed2) {
		this.observed2 = observed2;
	}
	public String getObserved3() {
		return observed3;
	}
	public void setObserved3(String observed3) {
		this.observed3 = observed3;
	}
	public String getReads3() {
		return reads3;
	}
	public void setReads3(String reads3) {
		this.reads3 = reads3;
	}
	public String getObserved4() {
		return observed4;
	}
	public void setObserved4(String observed4) {
		this.observed4 = observed4;
	}
	public String getReads4() {
		return reads4;
	}
	public void setReads4(String reads4) {
		this.reads4 = reads4;
	}
	
}
