FROM openjdk:17-jdk-slim
WORKDIR /app
COPY e-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]