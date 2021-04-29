/usr/bin/env bash

echo "Running in ${ENVIRONMENT} environment"
echo "Test runner so no need for mongo"
echo "While I thought that I was learning how to live, I have been learning how to die." > quote1.txt
ls

mvn test -s settings.xml

echo "completed"
sleep 100
