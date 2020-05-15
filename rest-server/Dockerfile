FROM openjdk:8-jdk-alpine
WORKDIR /rest-server
ADD target/lego-store-1.0.jar lego-store-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "lego-store-1.0.jar"]