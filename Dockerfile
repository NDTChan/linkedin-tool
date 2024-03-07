# Use an official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package -DskipTests

# Use an official OpenJDK image as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/linkedin-tool.jar .

# Expose port 8081 to the outside world. This does not actually publish the port, but it serves as documentation
EXPOSE 8081

# Define the command to run when the container starts
# This runs the Spring Boot application with an active Kubernetes profile
ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "linkedin-tool.jar"]