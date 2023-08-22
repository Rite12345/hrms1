# To build, run the following command from the top level project directory:
#
# docker build -t dizitiveit/cpqapp .

# Docker run:
# docker run -d --name CRM -p 8080:8080 dizitiveit/cpqapp
# Docker application will start with 8080 port.

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-ubuntu

#Test
#Tesr
EXPOSE 8080

RUN mkdir /app

COPY build/libs/*.jar /app/hrms-application.jar

ENTRYPOINT ["java","-jar","/app/hrms-application.jar"]
