FROM openjdk:8-alpine
LABEL maintainer="a.osipov.it@gmail.com"
RUN apk update && apk upgrade && apk add bash
COPY target/otus-application /srv
WORKDIR /srv/otus-service
ENTRYPOINT ["java", "-jar", "service-api.jar"]
EXPOSE 8000
