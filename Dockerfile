FROM openjdk:8-alpine
LABEL maintainer="a.osipov.it@gmail.com"
RUN apk update && apk upgrade && apk add bash
COPY target/otus-palevo-application /srv
WORKDIR /srv/otus-palevo
ENTRYPOINT ["java", "-jar", "palevo.jar"]
EXPOSE 8000
