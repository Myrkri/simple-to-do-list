FROM maven:3.9.10-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM maven:3.9.10-amazoncorretto-21

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]