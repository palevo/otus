FROM openjdk:8-alpine
LABEL maintainer="a.osipov.it@gmail.com"
RUN apk update && apk upgrade
COPY target/otus-application.jar /srv
WORKDIR /srv
ENTRYPOINT ["java", "-jar", "otus-application.jar"]
EXPOSE 8000
