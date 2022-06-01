package com.mule.mma;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mule.application.ApplicationMetrics;
import com.mulesoft.migration.beans.MessageInfo;
import com.mulesoft.migration.beans.ProjectMetaDataBean;
import com.orchestrator.PropsUtil;

public class MMAReport {

	private static Properties prop = PropsUtil.getProps();
	private static Logger logger = LogManager.getLogger(MMAReport.class);

	public void parseMMAReport(String reportPath, ProjectMetaDataBean metaData) {

		try {
			Path fileName = Path.of(reportPath);

			if(!fileName.toFile().isFile()) {
				throw new Exception("Report not generated");
			}



			ApplicationMetrics am = metaData.getMule4Metrics();
			String jsonReport = Files.readString(fileName);
			JsonElement element = JsonParser.parseString(jsonReport);
			if (element.isJsonObject()) {
				JsonObject mmaReport = element.getAsJsonObject();
				logger.debug("Going to set Application Metrics Bean for MMA generated file");
				am.setTotalComponents(mmaReport.get("numberOfMuleComponents").getAsInt());
				am.setTotalComponentsPendingMigration(mmaReport.get("numberOfMuleComponents").getAsInt()
						- mmaReport.get("numberOfMuleComponentsMigrated").getAsInt());
				am.setTotalMELExpressions(mmaReport.get("numberOfMELExpressions").getAsInt());
				am.setTotalMELExpressionsPendingMigration(mmaReport.get("numberOfMELExpressions").getAsInt()
						- mmaReport.get("numberOfMELExpressionsMigrated").getAsInt());
				am.setTotalMELLineExpressions(mmaReport.get("numberOfMELExpressionLines").getAsInt());
				am.setTotalMELLineExpressionsPendingMigration(mmaReport.get("numberOfMELExpressionLines").getAsInt()
						- mmaReport.get("numberOfMELExpressionLinesMigrated").getAsInt());
				am.setTotalDWLines(mmaReport.get("numberOfDWTransformationLines").getAsInt());
				am.setTotalDWLinesPendingMigration(mmaReport.get("numberOfDWTransformationLines").getAsInt()
						- mmaReport.get("numberOfDWTransformationLinesMigrated").getAsInt());

				//metaData.setProjectName(mmaReport.get("projectName").getAsString());

				JsonArray messages = mmaReport.get("detailedMessages").getAsJsonArray();
				List<MessageInfo> infos = new ArrayList<>();

				for (int i = 0; i < messages.size(); i++) {
					Gson gson = new Gson();
					MessageInfo info = gson.fromJson(messages.get(i), MessageInfo.class);
					if (!info.getLevel().equalsIgnoreCase("INFO") && !infos.contains(info) ) {
						infos.add(info);
						logger.debug("Parsed detailedMessages Array --> " + info.getKey() + " " +info.getLevel() + " for : " +info.getComponent());
					}

				}
				double score = 0.0;
				double tempScore = 0.0;

				for (MessageInfo info : infos) {
					if(prop.getProperty(info.getId()) == null) {
						String tempKey = findByProperty( info);
						tempScore = Double.parseDouble(prop.getProperty(tempKey));
						
					}else {
						 tempScore = Double.parseDouble(prop.getProperty(info.getId()));
					}
					logger.debug("Components to be given default score from props, ComponentName : " + info.getId() + " :: Calculated value from properties : " + prop.getProperty(info.getId()));
					score = score + tempScore;
				}
				/*only if component look up doesn't give any results*/
				if(infos.isEmpty()) {
					logger.debug("Setting score on the basis of property");
					score = am.getTotalComponentsPendingMigration()* (prop.getProperty("mule4.componentsWeight") == null ? 1
							: Double.parseDouble(prop.getProperty("mule4.componentsWeight")));
				}

				logger.info("Final score from MMA json file --> " + score);
				am.setScoreOfComponents(score);
				am.setScoreOfMELExpressions(am.getTotalMELExpressionsPendingMigration()
						* (prop.getProperty("mule4.MELExpressionWeigh") == null ? 1
								: Double.parseDouble(prop.getProperty("mule4.MELExpressionWeigh"))));
				am.setScoreOfMELLineExpressions(am.getTotalMELLineExpressionsPendingMigration()
						* (prop.getProperty("mule4.MELLineExpressionWeigh") == null ? 1
								: Double.parseDouble(prop.getProperty("mule4.MELLineExpressionWeigh"))));
				
				am.setScoreOfDWLLines(am.getTotalDWLinesPendingMigration()
						*(prop.getProperty("mule4.NumberOfDWLTransformationLines") == null ? 1
						: Double.parseDouble(prop.getProperty("mule4.NumberOfDWLTransformationLines"))));

				am.setMule4Score(am.getScoreOfComponents() +
									am.getScoreOfMELExpressions() +
									am.getScoreOfMELLineExpressions()+
									am.getScoreOfDWLLines());

				metaData.setMule4Metrics(am);
			}
		} catch (Exception e) {
			logger.error("File not in JSON format");
		}

	}
	
	
	public void parseMMAReportForMule3Score(String reportPath, ProjectMetaDataBean metaData) {
		try {
			Path fileName = Path.of(reportPath);

			if(!fileName.toFile().isFile()) {
				throw new Exception("Report not generated");
			}

			logger.debug("--->Getting metaData bean<---");
			ApplicationMetrics am = metaData.getMule4Metrics();
			String jsonReport = Files.readString(fileName);
			JsonElement element = JsonParser.parseString(jsonReport);
			if (element.isJsonObject()) {
				JsonObject mmaReport = element.getAsJsonObject();
				metaData.setProjectName(mmaReport.get("projectName").getAsString());
				double score = am.getTotalComponents()*Double.parseDouble(prop.getProperty("mule3.componentsWeightFactor"))
							+ am.getTotalMELLineExpressions()* Double.parseDouble(prop.getProperty("mule3.dwlLinesOfCodeWeightFactor"))
							+ am.getTotalDWLines()* Double.parseDouble(prop.getProperty("mule3.dwlLinesOfCodeWeightFactor"));
				metaData.setScore(score);
				
			}
		} catch (Exception e) {

		}
	}

	private String findByProperty(MessageInfo obj) {
		
		if(null != obj && null != obj.getKey() && obj.getKey().equalsIgnoreCase("components.unsupported")) {
			//System.out.println("Unsupported missing key is" + obj.getId() );
			logger.info("Unsupported missing key is" + obj.getId() );
			return "mule4.unsupportedComponentNotFoundWeight";
		}
			
		//System.out.println("Supported missing key is" + obj.getId()  );
		logger.info("Supported missing key is" + obj.getId() );
		return "mule4.supportComponentNotFoundWeight";
	}

}