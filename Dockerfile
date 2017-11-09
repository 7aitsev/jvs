FROM openjdk:8u131-jdk-alpine

MAINTAINER Maxim Zaitsev

RUN apk --update add git && \
    rm -rf /var/lib/apt/lists/* && \
    rm /var/cache/apk/* && \
    mkdir /git

WORKDIR /git
ENV PATH $PATH:/usr/local/bin/
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh
ENTRYPOINT ["docker-entrypoint.sh"]

EXPOSE 80
CMD ["https://github.com/MaksimZaitsev/SPbPU_TRPO_2017.git"]
