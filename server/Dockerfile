# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infra/pom.xml infra/
COPY api/pom.xml api/

RUN --mount=type=secret,id=maven-settings,target=/root/.m2/settings.xml \
    ./mvnw dependency:go-offline

COPY domain/src ./domain/src
COPY application/src ./application/src
COPY infra/src ./infra/src
COPY api/src ./api/src

RUN --mount=type=secret,id=maven-settings,target=/root/.m2/settings.xml \
    ./mvnw package -DskipTests


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]