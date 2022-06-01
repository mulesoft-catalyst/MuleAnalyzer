package com.mulesoft.migration.beans;

import java.util.List;

public class FileMetaDataBean {

	private String fileName;
	private Long dataWeaveCodeLength = 0L;
	private String fileType = "muleFlow";
	private Long totalNoOfLines = 0L;

	private List<String> componentList;

	private Long numberOfComponents = 0L;

	private Long numberOfTests = 0L;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getDataWeaveCodeLength() {
		return dataWeaveCodeLength;
	}

	public void setDataWeaveCodeLength(Long dataWeaveCodeLength) {
		this.dataWeaveCodeLength = dataWeaveCodeLength;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getTotalNoOfLines() {
		return totalNoOfLines;
	}

	public void setTotalNoOfLines(Long totalNoOfLines) {
		this.totalNoOfLines = totalNoOfLines;
	}

	public Long getNumberOfComponents() {
		return numberOfComponents;
	}

	public void setNumberOfComponents(Long numberOfComponents) {
		this.numberOfComponents = numberOfComponents;
	}

	public Long getNumberOfTests() {
		return numberOfTests;
	}

	public void setNumberOfTests(Long numberOfTests) {
		this.numberOfTests = numberOfTests;
	}

}
