FROM maven:3.8.5-openjdk-17 AS build

COPY . .

RUN mvn package spring-boot:repackage -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=build /target/muevetic-0.0.1-SNAPSHOT.jar /muevetic.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "muevetic.jar" ]
