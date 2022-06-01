# Mule 3 Migration Analyzer Tool

# Instructions for build and deploy

This project can compile and build itself and MMA project. It provides the following two functionalities:
  1. Read the Mule 3 projects from a directory, run MMA tool and generate estimate for migration effort.
  2. Read the MMA generated report json files, using which estimation for the development effort. 

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
    
    

## How to Run  mule migrator orchestrator
  
  - Full Version:
      java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -projectBasePath <Mule3Project path> -destinationProjectBasePath <Mule 4 project path>
  - Lite Version:
      java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -mule4ReportsSourceBasePath <Mule4 MMA Reports path> -mule4ReportsDestinationBasePath <Mule 4 Final report path> analyzerLite
  
  
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
      
      
      
# Instructions for using the runnable
  
  Excutable zip is available inside zip folder
  
  Unzip mule-migrator-orchestrator-0.0.1-SNAPSHOT.zip in target directory
  
  Change directory mule-migrator-orchestrator-0.0.1-SNAPSHOT 
  ## Directory structure in mule-migrator-orchestrator-0.0.1-SNAPSHOT will be like 
      conf
        config.properties
      libs
        ... dependency jars
      mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar
      
# Post completion of analysis
   -  Verify the file generated named ** estimate.csv ** in the destination path provided in the input parameters.
   -  Use the relavant information for calculation of development and testing effort for each of the required project.
