FROM openjdk:11-jre-slim

ENV JAVA_OPTS=""

ADD @build.finalName@.@packaging@ /app.jar

EXPOSE 8080
EXPOSE 8081
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]