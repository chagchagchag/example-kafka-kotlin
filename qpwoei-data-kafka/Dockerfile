FROM openjdk:17-alpine AS jar-image
WORKDIR deploy
COPY build/libs/lezhin-data-kafka-0.0.1-SNAPSHOT.jar lezhin-data-kafka.jar
RUN java -jar -Djarmode=layertools lezhin-data-kafka.jar extract

FROM openjdk:17-alpine
WORKDIR deploy
COPY --from=jar-image deploy/dependencies/ ./
COPY --from=jar-image deploy/snapshot-dependencies/ ./
COPY --from=jar-image deploy/spring-boot-loader/ ./
COPY --from=jar-image deploy/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]