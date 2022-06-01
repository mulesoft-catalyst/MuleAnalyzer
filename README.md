# Mule 3 Migration Analyzer Tool

This project is intended to compile and build itself and MMA project. It provides the following two functionalities:
  1. ANALYZER FULL VERSION - Read the Mule 3 projects from a directory, run MMA tool and generate estimate for migration effort.
  2. ANALYZER LITE VERSION - Read the MMA generated report json files, using which estimation for the development effort. 

  The final output is a CSV file with the estimates.

# Instructions for build and deploy

Just run below command to compile and build orchestrator and MMA project

    ./runOrchestrator.sh
  
 Change directory to
 
        cd mule-migration-assistant-chore-mig-cost/mule-migrator-orchestrator/target
    
 Unzip mule-migrator-orchestrator-0.0.1-SNAPSHOT.zip
 
 Change directory mule-migrator-orchestrator-0.0.1-SNAPSHOT 
 ## Directory structure in mule-migrator-orchestrator-0.0.1-SNAPSHOT will be like 
      conf
        config.properties
      libs
        ... dependency jars
      mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar
    
## Key Properties
    Mule 3 Properties
      componentsWeighFactor
      dwlLinesOfCodeWeighFactor
      munitsWeighFactor
  
    Mule 4 Properties
      mule4MELExpressionWeigh
      mule4MELLineExpressionWeigh
      mule4NumberOfDWLTransformationLines
      mule4ComponentsWeigh
  
    Error and WARN weigh as per components sample
      api-platform-gw.ERROR
      api-platform-gw.WARN
      apikit-soap.ERROR
      apikit-soap.WARN
      
      
      

# How to Run  mule migrator orchestrator
  
  


# Instructions for using the runnable Mule Analyzer
  
  Excutable zip is available inside zip folder
  
  Unzip mule-migrator-orchestrator-0.0.1-SNAPSHOT.zip in target directory
  
  Change directory mule-migrator-orchestrator-0.0.1-SNAPSHOT 
  ## Directory structure in mule-migrator-orchestrator-0.0.1-SNAPSHOT will be like 
      conf
        config.properties
      libs
        ... dependency jars
      mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar
      

 
     After navigating to mule-migrator-orchestrator-0.0.1-SNAPSHOT directory. Follow the below 
    - Full Version:
      java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -projectBasePath <Mule3_project_path> -destinationProjectBasePath <Mule4_project_path>
    - Lite Version:
      java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -mule4ReportsSourceBasePath <Mule4_MMA_Reports_path> -mule4ReportsDestinationBasePath <Mule4_Final_report_path> analyzerLite

# Post completion of analysis
   -  Verify the file generated named ** estimate.csv ** in the destination path provided in the input parameters.
   -  Use the relavant information for calculation of development and testing effort for each of the required project.
