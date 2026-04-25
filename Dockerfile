FROM eclipse-temurin:21-jdk

WORKDIR /registration-service

COPY target/registration-service.jar registration-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "registration-service.jar"]