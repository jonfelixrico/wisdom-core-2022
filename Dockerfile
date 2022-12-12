FROM maven:3.8.6-openjdk-18-slim AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package -Dmaven.test.skip

FROM openjdk:18.0.2.1-slim-buster

WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]