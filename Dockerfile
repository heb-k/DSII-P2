# Etapa 1: build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests=true

# Etapa 2: runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.war app.war

ENTRYPOINT ["java", "-jar", "app.war"]
