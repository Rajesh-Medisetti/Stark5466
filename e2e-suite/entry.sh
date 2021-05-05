/usr/bin/env bash

echo "Running in ${ENVIRONMENT} environment"
mvn test -s settings.xml
cp  -r /usr/src/app/target/surefire-reports /reports/
