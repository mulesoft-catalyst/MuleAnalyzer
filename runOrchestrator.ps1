echo "downloading mma"
curl https://github.com/mulesoft/mule-migration-assistant/archive/refs/heads/chore/mig-cost.zip -o mig-cost.zip >> run.log
Expand-Archive -Path mig-cost.zip 
echo "downloading migration estimator"
$orchestratorPath="mule3MigratorOrchestrator/mule3Migrator-main/mule-migrator-orchestrator"
$pomPath="mule3MigratorOrchestrator/mule3Migrator-main/mma-parent-pom.xml"
IF ($args[0] -eq "development"){
	curl https://github.com/dsuneja/mule3Migrator/archive/refs/heads/development.zip -o mule3MigratorOrchestrator.zip >> run.log
	$orchestratorPath="mule3MigratorOrchestrator/mule3Migrator-development/mule-migrator-orchestrator"
	$pomPath="mule3MigratorOrchestrator/mule3Migrator-development/mma-parent-pom.xml"
}ELSE{
	curl https://github.com/dsuneja/mule3Migrator/archive/refs/heads/main.zip -o mule3MigratorOrchestrator.zip >> run.log
}
Expand-Archive -Path mule3MigratorOrchestrator.zip

echo "setting up env to build"
Copy-Item -Path $orchestratorPath -Destination "mig-cost/mule-migration-assistant-chore-mig-cost" -Recurse >> run.log
Copy-Item -Path $pomPath -Destination "mig-cost/mule-migration-assistant-chore-mig-cost" -Recurse >> run.log
cd mig-cost/mule-migration-assistant-chore-mig-cost
Remove-Item pom.xml
Rename-Item -Path mma-parent-pom.xml -NewName pom.xml >> run.log
echo "building executable"
mvn clean install
cd mule-migrator-orchestrator >> run.log
mvn clean package assembly:single
Copy-Item -Path target/mule-migrator-orchestrator*.zip ../../../ >> run.log
cd ../../../
echo "clean source and temp files"
Remove-Item mig-cost.zip
Remove-Item mule3MigratorOrchestrator.zip
Remove-Item mule-migration-assistant* -Recurse
Remove-Item mule-cost* -Recurse
echo "Unzipping the mule migrator in MuleAnalyzer"
Expand-Archive -Path mule-migrator-orchestrator*.zip -DestinationPath MuleAnalyzer >> run.log
<#COPY  mule3Migrator-main/mma-parent-pom.xml mule-migration-assistant-chore-mig-cost/  >> run.log
COPY  mule3Migrator-main/mule-migrator-orchestrator mule-migration-assistant-chore-mig-cost  >> run.log
cd mule-migration-assistant-chore-mig-cost  >> run.log
mvn clean install  >> run.log
cd mule-migrator-orchestrator  >> run.log
mvn clean package assembly:single  >> run.log
mv target/mule-migrator-orchestrator*.zip ../../ >> run.log
cd ../../ >> run.log
rm -rf mig-cost.zip mule3MigratorOrchestrator.zip mule-migration-assistant* >> run.log
unzip mule-migrator-orchestrator*.zip -d MuleAnalyzer >> run.log#>