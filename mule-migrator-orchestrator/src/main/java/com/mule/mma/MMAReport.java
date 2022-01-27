package com.mule.mma;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;

import com.mule.application.ApplicationMetrics;
import com.mulesoft.migration.beans.MessageInfo;
import com.mulesoft.migration.beans.ProjectMetaDataBean;
import com.orchestrator.PropsUtil;

public class MMAReport {

	private static Properties prop = PropsUtil.getProps();

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
				am.setTotalComponents(mmaReport.get("numberOfMuleComponents").getAsInt());
				am.setTotalComponentsPendingMigration(mmaReport.get("numberOfMuleComponents").getAsInt()
						- mmaReport.get("numberOfMuleComponentsMigrated").getAsInt());
				am.setTotalMELExpressions(mmaReport.get("numberOfMELExpressions").getAsInt());
				am.setTotalMELExpressionsPendingMigration(mmaReport.get("numberOfMELExpressions").getAsInt()
						- mmaReport.get("numberOfMELExpressionsMigrated").getAsInt());
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
						System.out.println("info has -->" + info.getKey() + info.getLevel() + info.getComponent());
					}

				}
				double score = 0.0;

				Map<String, Integer> accumulator = new HashMap<String, Integer>();
				infos.forEach(t -> accumulator.merge(t.getId(), 1, Math::addExact));

				double tempScore = 0.0;

				for (String key : accumulator.keySet()) {
					
					
					if(prop.getProperty(key) == null) {
						
						String tempKey = findByProperty( infos, key);
						tempScore = Double.parseDouble(prop.getProperty(tempKey))*accumulator.get(key);
						
					}else {
						 tempScore = Double.parseDouble(prop.getProperty(key))*accumulator.get(key);
					}
					System.out.println("key is" + key + "property value is" + prop.getProperty(key));
					score = score + tempScore;
				}
				/*only if component look up doesn't give any results*/
				if(accumulator.isEmpty()) {
					score = am.getTotalComponentsPendingMigration()* (prop.getProperty("mule4.componentsWeight") == null ? 1
							: Double.parseDouble(prop.getProperty("mule4.componentsWeight")));
				}

				System.out.println("score is -->" + score);
				am.setScoreOfComponents(score);
				am.setScoreOfMELExpressions(am.getTotalMELExpressionsPendingMigration()
						* (prop.getProperty("mule4.MELExpressionWeigh") == null ? 1
								: Double.parseDouble(prop.getProperty("mule4.MELExpressionWeigh"))));
				am.setScoreOfMELLineExpressions(am.getTotalDWLinesPendingMigration()
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

		}

	}

	private String findByProperty(List<MessageInfo> infos, String key) {
		MessageInfo obj = infos.stream().filter(info -> key.equalsIgnoreCase(info.getId())).findFirst().orElse(null);
		
		if(null != obj && null != obj.getKey() && obj.getKey().equalsIgnoreCase("components.unsupported")) {
			System.out.println("Unsupported missing key is" + key );
			return "mule4.unsupportedComponentNotFoundWeight";
		}
			
		System.out.println("Supported missing key is" + key );
		return "mule4.supportComponentNotFoundWeight";
	}

}
