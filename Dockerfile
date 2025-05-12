FROM openjdk:22-jdk-slim

COPY target/Task_Manager.war /task-anager.war

CMD ["java", "-jar", "/task-anager"]