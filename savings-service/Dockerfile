FROM maven:3.8.5-openjdk-17-slim as build

COPY pom.xml /usr/src/savings/
RUN mvn -f /usr/src/savings/pom.xml dependency:resolve dependency:resolve-plugins

COPY src /usr/src/savings/src
RUN mvn -f /usr/src/savings/pom.xml clean package -DskipTests -o

FROM openjdk:17-jdk-alpine
COPY --from=build /usr/src/savings/target/savings-service-0.0.1-SNAPSHOT.jar savings-service-0.0.1-SNAPSHOT.jar

RUN mkdir /opt/savings
VOLUME /opt/savings

EXPOSE 8080
ENTRYPOINT java -jar savings-service-0.0.1-SNAPSHOT.jar
