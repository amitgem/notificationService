FROM tomcat:9.0-jdk8-temurin

ARG WAR_FILE=target/notification-service.war

RUN rm -rf /usr/local/tomcat/webapps/*
COPY ${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
