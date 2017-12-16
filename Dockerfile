FROM openjdk:8u131-jdk-alpine

MAINTAINER Maxim Zaitsev

RUN apk --update add git && \
    rm -rf /var/lib/apt/lists/* && \
    rm /var/cache/apk/* && \
    mkdir /git

WORKDIR /git
ENV PATH $PATH:/usr/local/bin/

ENTRYPOINT git clone $0 && dir=$(ls) && cd "$dir" && \
           javac -sourcepath src/ src/Main.java -cp lib/gson-2.8.2.jar -d . && \
           java -classpath lib/gson-2.8.2.jar:.:service \
                   -Djava.util.logging.config.file=logging.properties Main

EXPOSE 80
CMD ["https://github.com/MaksimZaitsev/jvs.git"]
