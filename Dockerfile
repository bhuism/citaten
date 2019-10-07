FROM openjdk:11-jre

EXPOSE 8080/tcp

RUN cp -vf /usr/share/zoneinfo/Europe/Amsterdam /etc/localtime

COPY target/demo.jar /app/

ENTRYPOINT ["java", "-jar", "app/demo.jar"]
