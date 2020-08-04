FROM amazoncorretto:11.0.5
MAINTAINER "Praveen Kamath <praveen.k@turtlemint.com>"
WORKDIR /usr/src/app
COPY target/newsfeed-0.0.1-SNAPSHOT.jar /usr/src/app/
VOLUME [ "./logs" ]
EXPOSE 8130
ENTRYPOINT java -XX:+HeapDumpOnOutOfMemoryError -jar newsfeed-0.0.1-SNAPSHOT.jar