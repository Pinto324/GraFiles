# Etapa de construcción
FROM maven:3-openjdk-17 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y la carpeta src
COPY pom.xml .
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Verificar que el archivo .war se haya generado
RUN ls -l /app/target

# Imagen base de Tomcat para ejecutar la aplicación
FROM tomcat:9.0

# Copiar el archivo .war generado al contenedor de Tomcat
COPY --from=build /app/target/GraFiles-1.0.war /usr/local/tomcat/webapps/

