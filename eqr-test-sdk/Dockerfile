FROM maven:3.6.3-openjdk-11-slim as build
WORKDIR /usr/src/app
COPY eqr-test-sdk/pom.xml /usr/src/app/pom.xml
#RUN mvn dependency:go-offline
COPY eqr-test-sdk/src /usr/src/app/src
COPY .mvn/ /usr/src/app/.mvn/
COPY .git/ /usr/src/app/.git/
COPY eqr-test-sdk/settings.xml /root/.m2/settings.xml
RUN mvn clean deploy -DskipTests
