FROM openjdk:11-jre
COPY target/demo.jar /app/
ENTRYPOINT ["java", "-jar", "app/demo.jar"]
