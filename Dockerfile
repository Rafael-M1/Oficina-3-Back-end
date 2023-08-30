#
# Build stage
#
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build
COPY . .
RUN mvn clean package -Ptest -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17
COPY --from=build /target/ifmthub-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]