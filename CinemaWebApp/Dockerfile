# build
FROM maven:3.9.2-amazoncorretto-17 AS build

WORKDIR /app
COPY . /app

EXPOSE 3306

RUN mvn -f pom.xml clean package


# compile
FROM amazoncorretto:17

COPY --from=build /app/target/*.jar app.jar

EXPOSE 3306
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
