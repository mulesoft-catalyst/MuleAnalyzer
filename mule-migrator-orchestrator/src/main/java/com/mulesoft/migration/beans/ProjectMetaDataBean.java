package com.mulesoft.migration.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mule.application.ApplicationMetrics;

public class ProjectMetaDataBean implements Serializable {

	private String muleVersion = "";

	private Map<String, String> projectConfigMap = new HashMap<String, String>(); // unique config component name in
																					// mule config files

	private Map<String, FileMetaDataBean> fileMetaDataMap = new HashMap<String, FileMetaDataBean>();

	Map<String, Long> dwlLinesofCode;

	private double score;

	private long totalNoOfComponents;

	private long totalLinesOfDWLCode;

	private long numberOfMunits;

	private String projectName;

	private ApplicationMetrics mule4Metrics;

	public Map<String, Long> getDwlLinesofCode() {
		return dwlLinesofCode;
	}

	public void setDwlLinesofCode(Map<String, Long> dwlLinesofCode) {
		this.dwlLinesofCode = dwlLinesofCode;
	}

	public String getMuleVersion() {
		return muleVersion;
	}

	public void setMuleVersion(String muleVersion) {
		this.muleVersion = muleVersion;
	}

	public Map<String, String> getProjectConfigMap() {
		return projectConfigMap;
	}

	public void setProjectConfigMap(Map<String, String> projectConfigMap) {
		this.projectConfigMap = projectConfigMap;
	}

	public Map<String, FileMetaDataBean> getFileMetaDataMap() {
		return fileMetaDataMap;
	}

	public void setFileMetaDataMap(Map<String, FileMetaDataBean> fileMetaDataMap) {
		this.fileMetaDataMap = fileMetaDataMap;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public long getTotalNoOfComponents() {
		return totalNoOfComponents;
	}

	public void setTotalNoOfComponents(long totalNoOfComponents) {
		this.totalNoOfComponents = totalNoOfComponents;
	}

	public long getTotalLinesOfDWLCode() {
		return totalLinesOfDWLCode;
	}

	public void setTotalLinesOfDWLCode(long totalLinesOfDWLCode) {
		this.totalLinesOfDWLCode = totalLinesOfDWLCode;
	}

	public long getNumberOfMunits() {
		return numberOfMunits;
	}

	public void setNumberOfMunits(long numberOfMunits) {
		this.numberOfMunits = numberOfMunits;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ApplicationMetrics getMule4Metrics() {
		return mule4Metrics;
	}

	public void setMule4Metrics(ApplicationMetrics mule4Metrics) {
		this.mule4Metrics = mule4Metrics;
	}

}
