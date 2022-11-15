FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-cursos

COPY ./pom.xml /app
#Copiar archivos para bajar dependencias una sola vez
COPY ./msvc-cursos/.mvn ./.mvn
COPY ./msvc-cursos/mvnw .
COPY ./msvc-cursos/pom.xml .

#Compilar los poms para bajar las dependencias
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

#Copiar codigo fuente para compilar
COPY ./msvc-cursos/src ./src

RUN ./mvnw clean package -DskipTests


#Dejar la imagen lo mas liviana posible, se utiliza una nueva para crear con solo el jar
FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .
EXPOSE 8002

CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]