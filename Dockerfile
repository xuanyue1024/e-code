FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY e-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]