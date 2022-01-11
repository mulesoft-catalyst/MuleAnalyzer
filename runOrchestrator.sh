curl -L https://github.com/mulesoft/mule-migration-assistant/archive/refs/heads/chore/mig-cost.zip -o mig-cost.zip | tee -a run.log
unzip mig-cost.zip | tee -a run.log
curl -L https://github.com/dsuneja/mule3Migrator/archive/refs/heads/main.zip -o mule3MigratorOrchestrator.zip | tee -a run.log
unzip mule3MigratorOrchestrator.zip | tee -a run.log
cp mule3Migrator-main/mma-parent-pom.xml mule-migration-assistant-chore-mig-cost/ | tee -a run.log
cp -R mule3Migrator-main/mule-migrator-orchestrator mule-migration-assistant-chore-mig-cost | tee -a run.log
cd mule-migration-assistant-chore-mig-cost
mvn clean install
cd mule-migrator-orchestrator
mvn clean package assembly:single
