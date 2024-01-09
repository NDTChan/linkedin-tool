#FROM maven:3.9.6-eclipse-temurin-17 AS build
## Google Chrome
#ARG CHROME_VERSION=120.0.6099.129-1
#RUN apt-get update -qqy \
#	&& apt-get -qqy install gpg unzip \
#	&& wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
#	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
#	&& apt-get update -qqy \
#	&& apt-get -qqy install google-chrome-stable=$CHROME_VERSION \
#	&& rm /etc/apt/sources.list.d/google-chrome.list \
#	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
#	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome
## ChromeDriver
#ARG CHROME_DRIVER_VERSION=120.0.6099.109
#RUN wget -q -O /tmp/chromedriver.zip https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$CHROME_DRIVER_VERSION/linux64/chromedriver-linux64.zip \
#	&& unzip /tmp/chromedriver.zip -d /opt \
#	&& rm /tmp/chromedriver.zip \
#	&& ln -s /opt/chromedriver-linux64/chromedriver /usr/bin/chromedriver
## Set the working directory in the container
FROM markhobson/maven-chrome:jdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean install
# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/linkedin-tool.jar .
# Set the command to run the application
CMD ["java", "-jar", "linkedin-tool.jar"]
