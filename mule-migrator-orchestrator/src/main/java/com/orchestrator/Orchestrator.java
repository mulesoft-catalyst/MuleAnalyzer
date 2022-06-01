package com.orchestrator;

import com.mulesoft.migration.analyzer.PrepareProjectList;
import com.mulesoft.migration.beans.ProjectMetaDataBean;
import com.mulesoft.tools.migration.MigrationRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mule.application.ApplicationMetrics;
import com.mule.mma.MMAReport;

public class Orchestrator {
    
    private static String DESTINATION_CSV_FILE = "estimate.csv";
    
    private static String[] reportFiles = {"report", "report.json"};
    private static String[] DEPLOY_FILE_REL_PATH = {"src", "main", "app", "mule-deploy.properties"};
    private static String[] DOMAIN_FILE_REL_PATH = {"src","main","domain","mule-domain-config.xml"};
    private static String PROJECTS_BASE_PATH = "";
    private static String DESTINATION_PROJECTS_BASE_PATH = "";
    private static String LITE_ANALYZER = "analyzerLite";
    private static Properties prop ;
    private static Logger logger = LogManager.getLogger(Orchestrator.class);
    
    private static String filePathBuilder(String[] pathNames) {
        String path = String.join(File.separator, pathNames);
        return path;
    }
    
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
        
        logger.info("Analyzer Started");
        logger.debug("Loading configuration properties for the project");
        PropsUtil.loadProperties("config/config.properties");
        prop = PropsUtil.getProps();
        
        String runningLite = null;
        
        if(args.length > 0 && args.length == 4) {
        	System.out.println("Started running the Analyzer Full Version");
            PROJECTS_BASE_PATH = args[1];
            DESTINATION_PROJECTS_BASE_PATH = args[3];
            logger.debug("PROJECTS_BASE_PATH :: "+ args[1]);
        }else if(args.length > 0 && args.length == 5){
        	System.out.println("Started running the Analyzer Lite Version");
            PROJECTS_BASE_PATH = args[1];
            DESTINATION_PROJECTS_BASE_PATH = args[3];
            runningLite = args[4];
        }else {
            logger.info("Arguments not provided hence started with default one");
            logger.info("Provide command line arguments in format : -projectBasePath <mule 3base path> -destinationProjectBasePath <mule 4 projects>");
            return;
        }
        
        
        if(runningLite != null && runningLite.equalsIgnoreCase(LITE_ANALYZER)) {
        	logger.debug("------->Going for Analyzer Lite version<-------");
            generateLiteEstimate(PROJECTS_BASE_PATH, DESTINATION_PROJECTS_BASE_PATH);
        }else {
        	logger.debug("------->Going for Analyzer Full version<-------");
            generateCompleteEstimate();
        }
        
        
        
        
        // TODO Auto-generated method stub
        
        
    }
    
    private static void generateCompleteEstimate() {
        File file = new File(PROJECTS_BASE_PATH);
        
        String PROJECT_BASE_PATH = "";
        String DESTINATION_PROJECT_BASE_PATH ="";
        
        File[] files = file.listFiles(directoryFileFilter);
        
        List<String> dirs = new ArrayList<>();
        
        getDirectoriesWithMuleProjects(dirs, files);
        
        
        
        for(String dir: dirs) {
            PROJECT_BASE_PATH = dir;
            String projectName = dir.substring(dir.lastIndexOf(File.separatorChar)+1, dir.length());
        //  logger.info(projectName);
            DESTINATION_PROJECT_BASE_PATH = DESTINATION_PROJECTS_BASE_PATH + File.separator + projectName;
            try {
                generateEstimate(PROJECT_BASE_PATH, DESTINATION_PROJECT_BASE_PATH, projectName);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    private static void generateLiteEstimate(String sourcePath, String destinationPath) {
        // identinfy all reports file
        Path sourceDir = Paths.get(sourcePath);
        try {
            List<Path> files = Files.walk(sourceDir).filter(Files::isRegularFile).filter(p -> p.toFile().getName().contains(".json")).collect(Collectors.toList());
            
            for(Path file: files) {
                logger.info("Project file getting analysed :: "+ file.toFile().getCanonicalPath());
                ApplicationMetrics am = new ApplicationMetrics();
                ProjectMetaDataBean metaDataBean = new ProjectMetaDataBean();
                metaDataBean.setMule4Metrics(am);
                
                if(Files.lines(file).filter(s -> s.contains("numberOfMuleComponents")).count() == 0) {
                    logger.error("skipping file as it is not report.json "+ file.toFile().getCanonicalPath());
                    continue;
                }
                // set mule version
                metaDataBean.setMuleVersion("server.3");
                MMAReport mma = new MMAReport();
                
                // get mule 4 score
                System.out.println("Going to get the Mule 4 score for the file: "+file.toFile().getCanonicalPath());
                mma.parseMMAReport(file.toFile().getCanonicalPath(), metaDataBean);
                System.out.println("MMA report generation completed for :: "+file.toFile().getCanonicalPath());
                logger.info("Parsing of MMA generated report completed for :: "+file.toFile().getCanonicalPath());
                //get project name
                //get Mule 3 score
                mma.parseMMAReportForMule3Score(file.toFile().getCanonicalPath(), metaDataBean);
                
                CSVUtil.writeToCSV(DESTINATION_PROJECTS_BASE_PATH+File.separator+DESTINATION_CSV_FILE, metaDataBean, false);
                
                //am.setApplicationName(applicationName);
                //am.setBasePath(PROJECT_BASE_PATH);
                //am.setDestinationPath(DESTINATION_PROJECT_BASE_PATH);
                
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    private static void getDirectoriesWithMuleProjects(List<String> dirs, File[] files) {
        try {
            for(File dir: files) {
                String srcFolderPath = dir.getCanonicalPath()+File.separator+"src";
                if(new File(srcFolderPath).isDirectory()) {
                    dirs.add(dir.getCanonicalPath());
                //  logger.debug("Mule project getting added"+dir.getCanonicalPath());
                }else {
                    getDirectoriesWithMuleProjects(dirs, dir.listFiles(directoryFileFilter));
                }
            }
        }
        catch(Exception e) {
            logger.error("Error while identifying directories");
        }
        
    }

    private static void generateEstimate(String PROJECT_BASE_PATH, String DESTINATION_PROJECT_BASE_PATH, String applicationName)  throws Exception{
        boolean isErrorProject = false;
        logger.info("Project getting processed: "+ PROJECT_BASE_PATH);
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
        
        String domainProjectName = getDomainProjectName(PROJECT_BASE_PATH);
        
        if(domainProjectName!=null && !"default".equalsIgnoreCase(domainProjectName)) {
            Path directory = Paths.get(PROJECTS_BASE_PATH);
            File file = new File("");
            List<Path> directories = Files.walk(directory).filter(Files::isDirectory).collect(Collectors.toList());
            for(Path path: directories) {
                if(path.toFile().getCanonicalPath().endsWith(domainProjectName))
                    file =  new File(path.toFile().getCanonicalPath());
            }
            if(file != null && file.isDirectory()) {
                inputArgs = new String[9];
                inputArgs[0] = "-projectBasePath";
                inputArgs[1] = am.getBasePath();
                inputArgs[2] = "-destinationProjectBasePath";
                inputArgs[3] = am.getDestinationPath();
                inputArgs[4] = "-muleVersion";
                inputArgs[5] = am.getApplicationVersion();
                inputArgs[6] = "-jsonReport";
                inputArgs[7] = "-parentDomainBasePath";
                inputArgs[8] = file.getCanonicalPath();
                logger.info(file.getCanonicalPath());
                
            }
        }
        
        metaDataBean.setProjectName(getProjectName(PROJECT_BASE_PATH));
        
        /*
         * this is only if project is non Mule 3 project
         */
        
        metaDataBean.setMuleVersion(getMuleVersion(PROJECT_BASE_PATH));
        try {
            for(String arg: inputArgs) {
                logger.debug("MMA Execution Start"+ arg);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            // IMPORTANT: Save the old System.out!
            PrintStream old = System.out;
            // Tell Java to use your special stream
            System.setOut(ps);
            logger.info("Going to run MMA for "+ inputArgs[1]);
            MigrationRunner.run(inputArgs);
            System.out.flush();
            System.setOut(old);
            logger.info("BinaryArrayOutputStream output->"+baos.toString());
            if(baos.toString().contains("Cannot read mule project")) {
                isErrorProject = true;
                CSVUtil.writeToCSV(DESTINATION_PROJECTS_BASE_PATH+File.separator+DESTINATION_CSV_FILE, metaDataBean, isErrorProject);
                return;
            }
        } 
        catch (Exception e) {
            logger.error("Error while calling MMA " + e);
        }
    
        /*
         * Read the JSON report and update ApplicationMetrics object
         */
        
        MMAReport mr = new MMAReport();
        try {
        	System.out.println("Going to parse the MMA report for :: "+ DESTINATION_PROJECT_BASE_PATH);
            logger.info("Going to parse MMA report for "+ DESTINATION_PROJECT_BASE_PATH);
            mr.parseMMAReport(DESTINATION_PROJECT_BASE_PATH+File.separator + filePathBuilder(reportFiles), metaDataBean);
        }catch(Exception e) {
            if("Report not generated".equalsIgnoreCase(e.getMessage())) {
                throw e;
            }
        }
        logger.info("Going to read Mule 3 code for  "+ PROJECT_BASE_PATH);
        
        PrepareProjectList.analyzeProject(PROJECT_BASE_PATH, metaDataBean);
        if(!notDomainProject(PROJECT_BASE_PATH)) {
            metaDataBean.setScore(Double.parseDouble(prop.getProperty("mule3.scoreForDomainProject")));
        }
        
        logger.info("Writing final scores of project " + PROJECT_BASE_PATH + " to Estimate file");
        CSVUtil.writeToCSV(DESTINATION_PROJECTS_BASE_PATH+File.separator+DESTINATION_CSV_FILE, metaDataBean, isErrorProject);
        
        logger.debug("---------------------------------------------");
        logger.debug("Analyzer Full version Bean Info for Mule4 ::");
        logger.debug("am.getTotalComponents(): "+ am.getTotalComponents());
        logger.debug("metaDataBean.getTotalNoOfComponents(): "+ metaDataBean.getTotalNoOfComponents());
        logger.debug("am.getTotalComponentsPendingMigration(): "+ am.getTotalComponentsPendingMigration());
        logger.debug("am.getTotalDWLines(): "+ am.getTotalDWLines());
        logger.debug("metaDataBean.getTotalLinesOfDWLCode(): "+ metaDataBean.getTotalLinesOfDWLCode());
        logger.debug("am.getTotalDWLinesPendingMigration(): "+ am.getTotalDWLinesPendingMigration());
        logger.debug("am.getTotalMELExpressions()(): "+ am.getTotalMELExpressions());
        logger.debug("am.getTotalMELExpressionsPendingMigration(): "+ am.getTotalMELExpressionsPendingMigration());
        logger.debug("am.getTotalMELLineExpressions(): "+ am.getTotalMELLineExpressions());
        logger.debug("am.getTotalMELLineExpressionsPendingMigration(): "+ am.getTotalMELLineExpressionsPendingMigration());
        logger.debug("am.getTotalMUnits(): "+ am.getTotalMUnits());
        logger.debug("metaDataBean.getNumberOfMunits(): "+ metaDataBean.getNumberOfMunits());
        logger.debug("am.getTotalMUnitsPendingMigration(): "+ am.getTotalMUnitsPendingMigration());
        logger.debug("am.getTotalErrors(): "+ am.getTotalErrors());
        logger.debug("am.getTotalWarnings(): "+ am.getTotalWarnings());
        logger.debug("am.getMule4Score(): "+ am.getMule4Score());
        logger.debug("metaDataBean.getScore(): "+ metaDataBean.getScore());
        logger.debug("---------------------------------------------");


        
    }

    
    private static boolean notDomainProject(String pROJECT_BASE_PATH) {
        File file = new File(pROJECT_BASE_PATH+File.separator +filePathBuilder(DOMAIN_FILE_REL_PATH));
        if(!file.exists()) {
            return true;
        }
        return false;
    }

    private static String getDomainProjectName(String pROJECT_BASE_PATH) {
        Properties prop = new Properties();
        if(!(new File(pROJECT_BASE_PATH+File.separator+filePathBuilder(DEPLOY_FILE_REL_PATH)).exists())) {
            return null;
        }
        
        try (InputStream input = new FileInputStream(pROJECT_BASE_PATH+File.separator+filePathBuilder(DEPLOY_FILE_REL_PATH))) {
            
            // load a properties file
            prop.load(input);
            // get the property value and print it out

        } catch (IOException ex) {
            ex.printStackTrace();
        }       
        return prop.getProperty("domain");
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
//                    Document document = builder.parse(new File("/Users/mvijayvargia/Downloads/Mule3/Mule/acsessftpservice/src/main/app/globalconfig.xml"));

                    // Normalize the XML Structure; It's just too important !!
                    document.getDocumentElement().normalize();

                    // Here comes the root node
                    Element root = document.getDocumentElement();
                    
                    NodeList nodes = root.getChildNodes();
                    
                    for (int temp = 0; temp < nodes.getLength(); temp++) {

                        Node node = nodes.item(temp);
                        
                        if(node.getNodeName().equalsIgnoreCase("artifactId")) {
                            projectName = node.getTextContent();
                            
                            logger.debug("setting project name as "+ node.getTextContent());
                            
                            break;
                        }
                            
                    }
                    break;
                }
            }
        }catch(Exception e) {
            logger.error(e);
        }
        return projectName;
    }

}