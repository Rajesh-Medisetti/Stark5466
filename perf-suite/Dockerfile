FROM maven:3.6.3-openjdk-11-slim as build
WORKDIR /usr/src/app
COPY pom.xml /usr/src/app/pom.xml
RUN mvn dependency:go-offline
COPY src /usr/src/app/src
RUN mvn clean package -Dspring.profiles.active=ci -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /usr/src/app
COPY entry.sh /usr/src/app
COPY test.sh /usr/src/app
COPY coveragelimits.py /usr/src/app
COPY --from=build /usr/src/app/target /usr/src/app/target
CMD ["/usr/src/app/entry.sh"]
