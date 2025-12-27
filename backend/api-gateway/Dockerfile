FROM eclipse-temurin:21-jdk AS build
LABEL authors="gopikrishnanrmg"
WORKDIR /app
COPY . .
RUN ./mvnw clean package -Pprod -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
