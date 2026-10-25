# Etapa 1: Compilación del proyecto con Maven y Java 21
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

# Copiar archivos del wrapper de Maven y dependencias
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente y compilar el JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen ligera de ejecución con JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el artefacto compilado desde la etapa anterior
COPY --from=builder /workspace/target/viveu-alojamiento-1.0.0.jar app.jar

# Render asigna dinámicamente la variable de entorno PORT
ENV PORT=8080
EXPOSE 8080

# Ejecutar la aplicación Spring Boot
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
