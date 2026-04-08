FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY demo.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]