FROM maven:3.9-ibm-semeru-21-jammy AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package
RUN ls /app/target

FROM openjdk:21
EXPOSE 8080
RUN mkdir -p /app/arquivos && chmod 777 /app/arquivos
COPY .env .
COPY --from=builder /app/target/*.jar docker-demo-app-two.jar
ENTRYPOINT ["java", "-jar", "/docker-demo-app-two.jar"]