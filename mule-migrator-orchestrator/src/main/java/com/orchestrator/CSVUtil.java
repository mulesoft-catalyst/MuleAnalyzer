package com.orchestrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mulesoft.migration.beans.ProjectMetaDataBean;
import com.opencsv.CSVWriter;

public class CSVUtil {

	public static void writeToCSV(String filePath, ProjectMetaDataBean metaData, boolean errorRecord) {
		File csvFile = new File(filePath);
		try {
			csvFile.createNewFile();
			CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true));
			List<String[]> data = new ArrayList<String[]>();
			String[] ar = new String[9];
			ar[0]= metaData.getProjectName();
			ar[1]= "";
			ar[3] = "";
			ar[4] = "";
			ar[5] ="";
			ar[6]="A. As-Is";
			String muleVersion = metaData.getMuleVersion().contains("server.3")? "Mule 3": "Mule 4";
			ar[2] = muleVersion;
			if(!errorRecord) {
				ar[7] = String.valueOf(Math.round(metaData.getScore()));
				ar[8] = String.valueOf(Math.round(metaData.getMule4Metrics().getMule4Score()));
			}else {
				ar[7] = "0";
				ar[8] = "0";
			}
			
			writer.writeNext(ar);;
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
