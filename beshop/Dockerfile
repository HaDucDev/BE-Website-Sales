# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8
# port
EXPOSE 8080
# copy application WAR (with libraries inside)
ADD target/beshop-springboot.jar beshop-springboot.jar
# specify default command
ENTRYPOINT ["java", "-jar", "beshop-springboot.jar"]