FROM openjdk:17
LABEL maintainer="Akinyemi samuel"
WORKDIR /app
COPY target/course-hub.jar /app
ENTRYPOINT ["java", "-jar", "/app/course-hub.jar"]
EXPOSE 8000