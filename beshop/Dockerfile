FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

FROM eclipse-temurin:8-jre
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/beshop-springboot.jar beshop-springboot.jar
ENTRYPOINT ["java", "-jar", "beshop-springboot.jar"]