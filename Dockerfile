FROM openjdk:21
EXPOSE 9090
ADD target/docker-demo-app-two.jar docker-demo-app-two.jar
ENTRYPOINT ["java", "-jar", "/docker-demo-app-two.jar"]