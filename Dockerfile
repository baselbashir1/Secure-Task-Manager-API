FROM openjdk:22-jdk-slim

COPY target/Task.war /task.war

CMD ["java", "-jar", "/task.war"]