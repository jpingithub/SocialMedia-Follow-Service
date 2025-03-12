# Stage 1: Build
FROM openjdk:17-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

# Ensure gradlew is executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the source code and build the application
COPY src /app/src
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/SocialMedia-Follow-Service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]