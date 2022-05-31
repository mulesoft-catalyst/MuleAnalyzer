#!/bin/sh
​
if  [ $# -eq 0 ]
then 
	echo 'Provide destination path for mule 4 projects'
	exit
fi



REPORTS_LIST=`find $1 -type f -name 'report.json' -o -name 'summary.html'`
for i in $REPORTS_LIST
	do
		ASSET_DIR=`dirname ${i}`
		echo $ASSET_DIR
		echo ${i}
		cp -R ${ASSET_DIR}/assets $1/reports/
		PROJECT_NAMES=(${ASSET_DIR//// })
		echo ${#PROJECT_NAMES[@]}
		
		PROJECT_NAME=${PROJECT_NAMES[(${#PROJECT_NAMES[@]}-2)]}
		echo $PROJECT_NAME
		FILE_NAME=`basename ${i}`
		echo $FILE_NAME
	#	FILE_EXTENSION="${FILE_NAME##*.}"
	#	echo $FILE_EXTENSION
		cp $i $1/reports/$PROJECT_NAME-${FILE_NAME};
		
done;