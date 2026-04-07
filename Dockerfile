FROM openjdk:17

WORKDIR /app

COPY demo.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]