curl -L https://github.com/mulesoft/mule-migration-assistant/archive/refs/heads/chore/mig-cost.zip -o mig-cost.zip | tee -a run.log
unzip mig-cost.zip | tee -a run.log
curl -L https://github.com/dsuneja/mule3Migrator/archive/refs/heads/development.zip -o mule3MigratorOrchestrator.zip | tee -a run.log
unzip mule3MigratorOrchestrator.zip | tee -a run.log
cp mule3Migrator-development/mma-parent-pom.xml mule-migration-assistant-chore-mig-cost/ | tee -a run.log
cp -R mule3Migrator-development/mule-migrator-orchestrator mule-migration-assistant-chore-mig-cost | tee -a run.log
cd mule-migration-assistant-chore-mig-cost
mvn clean install
cd mule-migrator-orchestrator
mvn clean package assembly:single
mv target/mule-migrator-orchestrator*.zip ../../
cd ../../
rm -rf mig-cost.zip mule3MigratorOrchestrator.zip mule-migration-assistant*
unzip mule-migrator-orchestrator*.zip -d MuleAnalyzer
