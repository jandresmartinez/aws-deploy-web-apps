FROM openjdk:8-jdk-alpine
LABEL maintainer="Jorge Andres"

COPY *.jar /app/demo.jar

COPY entrypoint.sh /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/bin/sh", "/app/entrypoint.sh"]

VOLUME /tmp/logs/
