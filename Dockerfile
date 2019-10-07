FROM openjdk:11-jre

EXPOSE 8080/tcp

COPY target/demo.jar /app/

ENTRYPOINT ["java", "-Duser.timezone=Europe/Amsterdam", "-jar", "app/demo.jar"]
