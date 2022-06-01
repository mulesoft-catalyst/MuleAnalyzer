package com.mulesoft.migration.analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mulesoft.migration.beans.ProjectMetaDataBean;

public class DWLAnalyzer {
    private static Logger logger = LogManager.getLogger(DWLAnalyzer.class);

	//public static String projectName = "/Users/dsuneja/AnypointStudio/architectWS/jobapplicationapi/src/main/resources/dwl";

	public static void main(String args[]) throws FileNotFoundException, IOException {

		ProjectMetaDataBean projectMetaDataBean = new ProjectMetaDataBean();
	//	analyzeDwls(projectName, projectMetaDataBean);
		//System.out.println(projectMetaDataBean.getDwlLinesofCode());
	}

	public static void analyzeDwls(String dwlFolder, ProjectMetaDataBean projectMetaDataBean)
			throws IOException, FileNotFoundException {
		File currentDir = new File(dwlFolder);
//	     Map<String, String> projectMap =  displayDirectoryContents(currentDir);

//	     System.out.println("# of Projects::" +  projectMap.size());
		File file = new File(dwlFolder);
		logger.info("file/directory passed: "+file.getName());
			if (file.isFile() &&  file.getName().contains(".dwl")) {
				try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {
					long linesCount = stream.filter(lines -> !(lines.startsWith("%dw") || lines.startsWith("%output")))
							.count();
					
					projectMetaDataBean.getDwlLinesofCode().put(file.getName(), new Long(linesCount));
				}
			}

			

		
	}

}
