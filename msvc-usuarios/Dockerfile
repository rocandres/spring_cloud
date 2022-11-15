FROM openjdk:17-jdk-alpine as builder
ARG MSVC_NAME=msvc-usuarios
WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
#Copiar archivos para bajar dependencias una sola vez
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

#Compilar los poms para bajar las dependencias
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

#Copiar codigo fuente para compilar
COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests


#Dejar la imagen lo mas liviana posible, se utiliza una nueva para crear con solo el jar
FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs
ARG TARGET_FOLDER=/app/msvc-usuarios/target

COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .

ARG POT_APP=8001
#Variable de ambiente puerto
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]