# Stage 1: Build
FROM gradle:8.7-jdk17 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build --no-daemon

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]