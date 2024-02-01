FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-19-jdk -y

COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:19-jdk-slim

EXPOSE 8080

COPY --from=build /target/moonvs-0.2.0-beta.2024-02.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
