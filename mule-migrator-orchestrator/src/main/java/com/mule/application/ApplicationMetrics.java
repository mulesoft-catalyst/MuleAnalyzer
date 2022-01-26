package com.mule.application;

public class ApplicationMetrics {
	private String applicationName;
	private String basePath;
	private String destinationPath;
	private String applicationVersion;
	private double mule4Score;
	private int totalComponents;
	private int totalComponentsPendingMigration;
	private int totalMUnits;
	private int totalMUnitsPendingMigration;
	private int totalMELExpressions;
	private int totalMELExpressionsPendingMigration;
	private int totalDWLines;
	private int totalDWLinesPendingMigration;
	private int totalWarnings;
	private int totalErrors;
	
	private double scoreOfDWLLines;

	private double scoreOfComponents;

	public double getScoreOfComponents() {
		return scoreOfComponents;
	}

	public void setScoreOfComponents(double scoreOfComponents) {
		this.scoreOfComponents = scoreOfComponents;
	}

	public double getScoreOfMELExpressions() {
		return scoreOfMELExpressions;
	}

	public void setScoreOfMELExpressions(double scoreOfMELExpressions) {
		this.scoreOfMELExpressions = scoreOfMELExpressions;
	}

	public double getScoreOfMELLineExpressions() {
		return scoreOfMELLineExpressions;
	}

	public void setScoreOfMELLineExpressions(double scoreOfMELLineExpressions) {
		this.scoreOfMELLineExpressions = scoreOfMELLineExpressions;
	}

	private double scoreOfMELExpressions;

	private double scoreOfMELLineExpressions;

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * @return the destinationPath
	 */
	public String getDestinationPath() {
		return destinationPath;
	}

	/**
	 * @param destinationPath the destinationPath to set
	 */
	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	/**
	 * @return the applicationVersion
	 */
	public String getApplicationVersion() {
		return applicationVersion;
	}

	/**
	 * @param applicationVersion the applicationVersion to set
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	/**
	 * @return the mule4Score
	 */
	public double getMule4Score() {
		return mule4Score;
	}

	/**
	 * @param mule4Score the mule4Score to set
	 */
	public void setMule4Score(double mule4Score) {
		this.mule4Score = mule4Score;
	}

	/**
	 * @return the totalComponents
	 */
	public int getTotalComponents() {
		return totalComponents;
	}

	/**
	 * @param totalComponents the totalComponents to set
	 */
	public void setTotalComponents(int totalComponents) {
		this.totalComponents = totalComponents;
	}

	/**
	 * @return the totalComponentsPendingMigration
	 */
	public int getTotalComponentsPendingMigration() {
		return totalComponentsPendingMigration;
	}

	/**
	 * @param totalComponentsPendingMigration the totalComponentsPendingMigration to
	 *                                        set
	 */
	public void setTotalComponentsPendingMigration(int totalComponentsPendingMigration) {
		this.totalComponentsPendingMigration = totalComponentsPendingMigration;
	}

	/**
	 * @return the totalMUnits
	 */
	public int getTotalMUnits() {
		return totalMUnits;
	}

	/**
	 * @param totalMUnits the totalMUnits to set
	 */
	public void setTotalMUnits(int totalMUnits) {
		this.totalMUnits = totalMUnits;
	}

	/**
	 * @return the totalMUnitsPendingMigration
	 */
	public int getTotalMUnitsPendingMigration() {
		return totalMUnitsPendingMigration;
	}

	/**
	 * @param totalMUnitsPendingMigration the totalMUnitsPendingMigration to set
	 */
	public void setTotalMUnitsPendingMigration(int totalMUnitsPendingMigration) {
		this.totalMUnitsPendingMigration = totalMUnitsPendingMigration;
	}

	/**
	 * @return the totalMELExpressions
	 */
	public int getTotalMELExpressions() {
		return totalMELExpressions;
	}

	/**
	 * @param totalMELExpressions the totalMELExpressions to set
	 */
	public void setTotalMELExpressions(int totalMELExpressions) {
		this.totalMELExpressions = totalMELExpressions;
	}

	/**
	 * @return the totalMELExpressionsPendingMigration
	 */
	public int getTotalMELExpressionsPendingMigration() {
		return totalMELExpressionsPendingMigration;
	}

	/**
	 * @param totalMELExpressionsPendingMigration the
	 *                                            totalMELExpressionsPendingMigration
	 *                                            to set
	 */
	public void setTotalMELExpressionsPendingMigration(int totalMELExpressionsPendingMigration) {
		this.totalMELExpressionsPendingMigration = totalMELExpressionsPendingMigration;
	}

	/**
	 * @return the totalDWLines
	 */
	public int getTotalDWLines() {
		return totalDWLines;
	}

	/**
	 * @param totalDWLines the totalDWLines to set
	 */
	public void setTotalDWLines(int totalDWLines) {
		this.totalDWLines = totalDWLines;
	}

	/**
	 * @return the totalDWLinesPendingMigration
	 */
	public int getTotalDWLinesPendingMigration() {
		return totalDWLinesPendingMigration;
	}

	/**
	 * @param totalDWLinesPendingMigration the totalDWLinesPendingMigration to set
	 */
	public void setTotalDWLinesPendingMigration(int totalDWLinesPendingMigration) {
		this.totalDWLinesPendingMigration = totalDWLinesPendingMigration;
	}

	/**
	 * @return the totalWarnings
	 */
	public int getTotalWarnings() {
		return totalWarnings;
	}

	/**
	 * @param totalWarnings the totalWarnings to set
	 */
	public void setTotalWarnings(int totalWarnings) {
		this.totalWarnings = totalWarnings;
	}

	/**
	 * @return the totalErrors
	 */
	public int getTotalErrors() {
		return totalErrors;
	}

	/**
	 * @param totalErrors the totalErrors to set
	 */
	public void setTotalErrors(int totalErrors) {
		this.totalErrors = totalErrors;
	}

	public double getScoreOfDWLLines() {
		return scoreOfDWLLines;
	}

	public void setScoreOfDWLLines(double scoreOfDWLLines) {
		this.scoreOfDWLLines = scoreOfDWLLines;
	}
	
	

}
