FROM openjdk:8-alpine
LABEL maintainer="a.osipov.it@gmail.com"
RUN apk update && apk upgrade && apk add bash
COPY target/otus-palevo-application /srv
WORKDIR /srv/otus-palevo
RUN chmod 755 startup.sh
CMD ["startup.sh", "start"]
EXPOSE 8000
