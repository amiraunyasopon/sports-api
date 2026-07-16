FROM eclipse-temurin:17-jre

LABEL maintainer="amiraunyasopon"

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]