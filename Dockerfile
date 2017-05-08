FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD demo.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java  -jar /app.jar" ]