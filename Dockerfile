# Build: compila o jar dentro de uma imagem com Maven + JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run: imagem final so com o JRE (menor) + o jar ja pronto
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/service-management-0.0.1-SNAPSHOT.jar app.jar

# o Render injeta a variavel PORT; application.properties ja le server.port=${PORT:8080}
ENTRYPOINT ["java", "-jar", "app.jar"]