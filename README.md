# Pre-requisite

Include maven bin holder in PATH vriable
Use Java 11 for running the executable

# mule3Migrator

This project can compile and build MMA project and itself

Run below command to compile and build orchestrator and MMA project.

    ./runOrchestrator.sh
    
Optional parameter of development can be passed if analyzer code needs to be fetched from development branch

    ./runOrchestrator.sh development
  
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
    
 

Run  mule mirator orchestrator

    java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -projectBasePath <Mule3Project path> -destinationProjectBasePath <Mule 4 project path>
  
  
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
    
  
  
  
  
