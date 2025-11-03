# syntax=docker/dockerfile:1

# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Cache dependencies
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

# Create non-root user
RUN useradd -ms /bin/sh spring
USER spring

WORKDIR /app

# Copy the Spring Boot fat jar from the build stage
COPY --from=build /workspace/target/*.jar /app/app.jar

# Expose application port
EXPOSE 8090

# Allow passing custom JVM args
ENV JAVA_OPTS=""

ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
