# Mule 3 Migration Analyzer Tool

  The Mule 3 Migration Analyzer tool is build for the variety of teams to utilize from the customers to the integration team to be aware of the complexity of the APIs and amount of Development and testing effort required. The generated output in CSV file can be further fed to the _Mule Application Migration Effort Estimator Tool_ 

# Why Migration Analyzer Tool?
  
  The Mule Migration Analyzer tool can be used in following scenerios:
  1. **Pre Sales** - In this scenerio, this tool can be utilized to calculate overall migration effort that can be used by the Sales Team further helping frame the SOW.
    This tool minimizes the effort as all the APIs available for the particular customer can be scanned in one go and provide close to 80 percent accuracy in migration effort.
  2. **Project Discovery** - In this scenerio, this tool can be levaraged by the Solution Architects, designers and Consultants to identify the risks and complexity in individual APIs hence leading to a smoother transition from Mule 3 to Mule 4 in a multi-phased fashion. 

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Prerequisites
  1. Understand the functionality and steps to achieve the end result.
  2. Access to Customer's Mule 3 Source code repository.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Usage

This tool is intended to compile and build itself and MMA project. It provides the following two functionalities:

  1. *ANALYZER FULL VERSION* - Read the Mule 3 projects from a directory, run MMA tool and generate estimate for migration effort.
  2. *ANALYZER LITE VERSION* - Read the MMA generated report json files, using which estimation for the development effort. 

  The final output of this tool generated the following:
    1. **CSV file:** This file contains the metadata that is needed as input for the Mule Application Migration Effort Estimator Tool. Contact the concerned person for access to Estimator Tool.
    2. **Migrated Mule 4 Source Code:** The Analyzer Tool internally calls the Mule Migration Assistant Tool which migrates the Mule 3 API to a Mule 4 API and provides a report (in HTML and JSON formats). This migrated Mule 4 API is ready to be imported in Anypoint Studio by the developer and begin fixing the reported errors/warnings during the Implementation phase of the project.


## Steps to execute the runnable
  - Clone the repository and Navigate to **Runner** directory.
    ``` cd mule3Migrator/runner```
  - Unzip the **MuleAnalyzer.zip**.
  - Change directory to extracted **MuleAnalyzer** directory.
    ``` cd MuleAnalyzer ```
### Directory structure in *MuleAnalyzer* will be like 
      conf
        config.properties
      libs
        ... dependency jars
      mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar

  - Running the **Analyzer Lite** version.
    * *Lite Version:*
    
      `java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -mule4ReportsSourceBasePath <Mule4_MMA_Reports_path> -mule4ReportsDestinationBasePath <Mule4_Final_report_path> analyzerLite`
      
      **Required Parameters for running LITE version:**
      1. `mule4ReportsSourceBasePath <Mule4_MMA_Reports_path>`: Path for your directory containing the generated reports produced by MMA [Mule Migration Assistant](https://github.com/mulesoft/mule-migration-assistant) in **.json** format.
      2. `-mule4ReportsDestinationBasePath <Mule4_Final_report_path>`: Path to directory where you want to have the final out **estimate.csv** file. 
      3. `analyzerLite`: Mandatory parameter for executing LITE version.

      `e.g.: java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -mule4ReportsSourceBasePath /Users/AnubhavMangla/MuleAnalyzer/Mule4Reports -mule4ReportsDestinationBasePath /Users/AnubhavMangla/MuleAnalyzer/MigratedMule4Estimate analyzerLite`


    * *Full Version:*
    
      `java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -projectBasePath <Mule3_project_path> -destinationProjectBasePath <Mule4_project_path>`
      **Required Parameters for running LITE version:**
      1. `-projectBasePath <Mule3_project_path>`: This is the path of the directory having MULE 3 source code.
      2. `-destinationProjectBasePath <Mule4_project_path>`: This param should be provided with destination path of the output of this tool, i.e. Migrated code using MMA and **estimate.csv**. 
    
    `e.g.: java -jar mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar -projectBasePath /Users/AnubhavMangla/MuleAnalyzer/mule3projects -destinationProjectBasePath /Users/AnubhavMangla/MuleAnalyzer/MigratedMule4Estimate analyzerLite`

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Steps for build and deploy the Analyzer tool

  1. Just run below command to compile and build orchestrator and MMA project.

    `./runOrchestrator.sh`

    **N.B.: If you want to build the code from *development* branch use the below command on terminal**.
  
    `./runOrchestrator.sh development`

  2. Change directory to
 
      `cd MuleAnalyzer`
    
### Directory structure in **MuleAnalyzer** will be like 
      conf
        config.properties
      libs
        ... project dependency jars
      mule-migrator-orchestrator-0.0.1-SNAPSHOT.jar
    
### Key Properties
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

## Instructions for using the runnable Mule Analyzer after build
  
  - Navigate to [Steps to execute the runnable](#Steps-to-execute-the-runnable)

--------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Post completion of analysis
  -  Verify the file generated named ** estimate.csv ** in the destination path provided in the input parameters.
  -  Use the relavant information for calculation of development and testing effort for each of the required project.
