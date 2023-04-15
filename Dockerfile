FROM amazoncorretto:17-al2-jdk
# Define a variable with an optional default value that users can override at build-time when using docker build
ARG JAR_FILE=target/*.jar
# copy executable jar
COPY ${JAR_FILE} app.jar
# Configure this container for running as an executable. Run jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
# With profile
# ENTRYPOINT ["java", "-Dspring.profiles.active=test", "-jar", "/app.jar"]