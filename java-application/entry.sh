#!/usr/bin/env bash

set -e

echo "Running in ${ENVIRONMENT} environment"

if [[ "${ENVIRONMENT}" = "development" ]]
then
    echo "Waiting for mongo database"
    while ! nc -z mongo 27017; do
        echo .
        sleep 0.5 # wait for 0.5 of the second before check again
    done
fi

if [[ "${ENVIRONMENT}" = "production" ]] || [[ "${ENVIRONMENT}" = "staging" ]]; then
  mkdir -p /config
  touch /config/application.properties
fi

echo "Setting mongodb url"
if [[ "${ENVIRONMENT}" = "production" ]]; then
    # add superprodmongo-0-pvt.infra.joveo.com:27017 if you want write capability
    echo "spring.data.mongodb.uri=mongodb://${MONGODB_USERNAME}:${MONGODB_PASSWORD}@superprodmongo-4-pvt.infra.joveo.com:27017,superprodmongo-5-pvt.infra.joveo.com:27017/mojo?authSource=admin&readPreference=secondaryPreferred" >> /config/application.properties
elif [[ "${ENVIRONMENT}" = "staging" ]]; then
    echo "spring.data.mongodb.uri=mongodb://${MONGODB_USERNAME}:${MONGODB_PASSWORD}@services-mongo.infra-dev.joveo.com:27000/mojo?authSource=admin&readPreference=primaryPreferred" >> /config/application.properties
fi

if [[ "${ENVIRONMENT}" = "production" ]] || [[ "${ENVIRONMENT}" = "staging" ]]; then
    java -jar target/java-application-0.0.1-SNAPSHOT.jar --spring.profiles.active="${ENVIRONMENT}" --spring.config.additional-location=file:/config/
elif [[ "${ENVIRONMENT}" = "development" ]]
then
    mvn clean spring-boot:run -Dspring-boot.run.profiles=development -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
else
    exit 1
fi
