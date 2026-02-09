# Multi-stage Dockerfile: build with Maven + JDK17 and run with a lightweight JRE

# --- Build stage ---
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
# Only copy pom first to leverage Docker cache for dependencies
COPY pom.xml ./
# Copy source
COPY src ./src

# Build the project (skip tests to speed up builds locally; remove -DskipTests=false to run tests)
RUN mvn -B -DskipTests package

# --- Run stage ---
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the packaged jar from the builder stage
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]

