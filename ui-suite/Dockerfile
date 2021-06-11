FROM maven:3.6.3-openjdk-11-slim

RUN apt-get update \
    && apt-get install -y \
        netcat-openbsd \
        git \
        python3 \
        python3-pip \
        python3-setuptools \
        jq \
    && rm -rf /var/lib/apt/lists/*

RUN apt-get install -y tzdata

WORKDIR /usr/src/app

COPY settings.xml /usr/src/app/
COPY test.sh /usr/src/app
COPY pom.xml /usr/src/app/pom.xml
COPY src /usr/src/app/src
WORKDIR /usr/src/app
COPY entry.sh /usr/src/app
COPY suite.xml /usr/src/app
COPY coveragelimits.py /usr/src/app
CMD ["/usr/src/app/entry.sh"]
