# Stage 1: Build the application
FROM maven:3.9.11-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy the POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application (skip tests for faster build, remove if tests are needed)
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21

# Set working directory
WORKDIR /app

# Copy the WAR file from the builder stage
#COPY --from=builder /app/target/microfinance-0.0.1-SNAPSHOT.war /app/microfinance.war

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/microfinance.war"]