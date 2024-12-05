FROM gradle:8.5-jdk21-jammy  AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21.0.3_9-jdk-alpine

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/main.jar

COPY --from=build /home/gradle/src/src/main/resources /app/resources

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/main.jar"]