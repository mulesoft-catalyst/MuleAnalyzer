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
					if (!infos.contains(info) && info.getLevel() != "INFO") {
						infos.add(info);
						System.out.println("info has -->" + info.getKey() + info.getLevel() + info.getComponent());
					}

				}
				double score = 0.0;

				Map<String, Integer> accumulator = new HashMap<String, Integer>();
				infos.forEach(t -> accumulator.merge(t.getId(), 1, Math::addExact));

				System.out.println(accumulator);

				for (String key : accumulator.keySet()) {

					System.out.println("key is" + key + "property value is" + prop.getProperty(key));
					score = score + (prop.getProperty(key) == null ? 1 : Double.parseDouble(prop.getProperty(key)))
							* accumulator.get(key);
				}
				/*only if component look up doesn't give any results*/
				if(accumulator.isEmpty()) {
					score = am.getTotalComponentsPendingMigration()* (prop.getProperty("mule4ComponentsWeigh") == null ? 1
							: Double.parseDouble(prop.getProperty("mule4ComponentsWeigh")));
				}

				System.out.println("score is -->" + score);
				am.setScoreOfComponents(score);
				am.setScoreOfMELExpressions(am.getTotalMELExpressionsPendingMigration()
						* (prop.getProperty("mule4MELExpressionWeigh") == null ? 1
								: Double.parseDouble(prop.getProperty("mule4MELExpressionWeigh"))));
				am.setScoreOfMELLineExpressions(am.getTotalDWLinesPendingMigration()
						* (prop.getProperty("mule4MELLineExpressionWeigh") == null ? 1
								: Double.parseDouble(prop.getProperty("mule4MELLineExpressionWeigh"))));

				am.setMule4Score(am.getScoreOfComponents() +
									am.getScoreOfMELExpressions() +
									am.getScoreOfMELLineExpressions());

				metaData.setMule4Metrics(am);
			}
		} catch (Exception e) {

		}

	}

}
