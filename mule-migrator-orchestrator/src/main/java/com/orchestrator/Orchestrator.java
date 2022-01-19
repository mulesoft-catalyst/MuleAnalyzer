package com.orchestrator;

import com.mulesoft.migration.analyzer.PrepareProjectList;
import com.mulesoft.migration.beans.ProjectMetaDataBean;
import com.mulesoft.tools.migration.MigrationRunner;
import com.mulesoft.tools.migration.engine.exception.MigrationJobException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mule.application.ApplicationMetrics;
import com.mule.application.Scanner;
import com.mule.mma.MMAReport;
import com.mule.lookup.WeightLookup;

public class Orchestrator {
	
	private static String DESTINATION_CSV_FILE = "estimate.csv";
	
	private static String DESTINATION_REPORT_REL_PATH = "/report/report.json";
	
	private static String PROJECTS_BASE_PATH = "/Users/dsuneja/mule3/mule3Projects";
	private static String DESTINATION_PROJECTS_BASE_PATH = "/Users/dsuneja/mule3/mule4Projects";
	
	private static FileFilter directoryFileFilter = new FileFilter() {
	      //Override accept method
	      public boolean accept(File file) {
	         //if the file directory return true, else false
	         if (file.isDirectory()) {
	            return true;
	         }
	         return false;
	      }
	   };

	public static void main(String[] args) {
		
		PropsUtil.loadProperties("config/config.properties");
		
		
		
		
		File file = new File(PROJECTS_BASE_PATH);
		
		String PROJECT_BASE_PATH = "";
		String DESTINATION_PROJECT_BASE_PATH ="";
		
		File[] files = file.listFiles(directoryFileFilter);
		
		
		if(args.length > 0 && args.length == 4) {
			PROJECTS_BASE_PATH = args[1];
			DESTINATION_PROJECTS_BASE_PATH = args[3];
			System.out.println("args -1"+ args[1]);
		}else {
			System.out.println("Arguments not provided hence started with default one");
			System.out.println("Provide command line arguments in format : -projectBasePath <mule 3base path> -destinationProjectBasePath <mule 4 projects>");
			return;
		}
		
		
		for(File dir : files) {
			PROJECT_BASE_PATH = PROJECTS_BASE_PATH + "/" +dir.getName();
			DESTINATION_PROJECT_BASE_PATH = DESTINATION_PROJECTS_BASE_PATH + "/" + dir.getName();
			generateEstimate(PROJECT_BASE_PATH, DESTINATION_PROJECT_BASE_PATH, dir.getName());
		}
		
		
		// TODO Auto-generated method stub
		
		
	}

	private static void generateEstimate(String PROJECT_BASE_PATH, String DESTINATION_PROJECT_BASE_PATH, String applicationName) {
		boolean isErrorProject = false;
		
		ApplicationMetrics am = new ApplicationMetrics();
		//am.setApplicationName(applicationName);
		am.setBasePath(PROJECT_BASE_PATH);
		am.setDestinationPath(DESTINATION_PROJECT_BASE_PATH);
		am.setApplicationVersion("4.3.0");
		String[] inputArgs = new String[7]; // The inputArgs will have to be read dynamically from the folder path
											// instead of hard coded values
		inputArgs[0] = "-projectBasePath";
		inputArgs[1] = am.getBasePath();
		inputArgs[2] = "-destinationProjectBasePath";
		inputArgs[3] = am.getDestinationPath();
		inputArgs[4] = "-muleVersion";
		inputArgs[5] = am.getApplicationVersion();
		inputArgs[6] = "-jsonReport";
		
		/*
		 * Call the MMA tool and pass the ApplicationMetrics object
		 */
		ProjectMetaDataBean metaDataBean = new ProjectMetaDataBean();
		metaDataBean.setMule4Metrics(am);
		
		metaDataBean.setProjectName(getProjectName(PROJECT_BASE_PATH));
		
		/*
		 * this is only if project is non Mule 3 project
		 */
		metaDataBean.setMuleVersion(getMuleVersion(PROJECT_BASE_PATH));
		try {
			for(String arg: inputArgs) {
				System.out.println("MMA Execution Start"+ arg);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			// IMPORTANT: Save the old System.out!
			PrintStream old = System.out;
			// Tell Java to use your special stream
			System.setOut(ps);
			MigrationRunner.run(inputArgs);
			System.out.flush();
			System.setOut(old);
			System.out.println("baos out->"+baos.toString());
			if(baos.toString().contains("Cannot read mule project")) {
				isErrorProject = true;
				CSVUtil.writeToCSV(DESTINATION_PROJECTS_BASE_PATH+DESTINATION_CSV_FILE, metaDataBean, isErrorProject);
				return;
			}
		} 
		catch (Exception e) {
			System.out.println("Error while calling MMA " + e);
		}

		/*
		 * Read the JSON report and update ApplicationMetrics object
		 */
		
		MMAReport mr = new MMAReport();
		try {
			mr.parseMMAReport(DESTINATION_PROJECT_BASE_PATH + DESTINATION_REPORT_REL_PATH, metaDataBean);
		}catch(Exception e) {
			if("Report not generated".equalsIgnoreCase(e.getMessage())) {
				throw e;
			}
		}
		
		PrepareProjectList.analyzeProject(PROJECT_BASE_PATH, metaDataBean);
		
		CSVUtil.writeToCSV(DESTINATION_PROJECTS_BASE_PATH+DESTINATION_CSV_FILE, metaDataBean, isErrorProject);
		
		
	}

	private static String getMuleVersion(String PROJECT_BASE_PATH) {
		// TODO Auto-generated method stub
		
		String muleVersion= null;
		File dir = new File(PROJECT_BASE_PATH);
		
		try {
			
			File[] files = dir.listFiles();
			for(File file: files) {
				if(file.isFile() && file.getName().equalsIgnoreCase("mule-artifact.json")) {
					muleVersion = "Mule 4";
					break;
				}
			}
		}catch(Exception e) {
				
			}
		return muleVersion;
	}

	private static String getProjectName(String PROJECT_BASE_PATH) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		String projectName= null;
		File dir = new File(PROJECT_BASE_PATH);
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File[] files = dir.listFiles();
			for(File file: files) {
				if(file.isFile() && file.getName().equalsIgnoreCase("pom.xml")) {
					Document document = builder.parse(new File(file.getCanonicalPath()));
//      		      Document document = builder.parse(new File("/Users/mvijayvargia/Downloads/Mule3/Mule/acsessftpservice/src/main/app/globalconfig.xml"));

					// Normalize the XML Structure; It's just too important !!
					document.getDocumentElement().normalize();

					// Here comes the root node
					Element root = document.getDocumentElement();
					
					NodeList nodes = root.getChildNodes();
					
					for (int temp = 0; temp < nodes.getLength(); temp++) {

						Node node = nodes.item(temp);
						
						if(node.getNodeName().equalsIgnoreCase("artifactId")) {
							projectName = node.getTextContent();
							
							System.out.println("setting project name as "+ node.getTextContent());
							
							break;
						}
							
					}
					break;
				}
			}
		}catch(Exception e) {
			
		}
		return projectName;
	}

}
