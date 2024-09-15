FROM maven:3.9.9-amazoncorretto-21 AS maven

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package

FROM amazoncorretto:21-alpine

COPY --from=maven target/*.jar app.jar

CMD ["java", "-jar", "/app.jar"]

EXPOSE 8080