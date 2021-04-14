FROM maven:3.6.3-openjdk-11-slim

RUN apt-get update \
    && apt-get install -y \
        wget \
        unzip \
        netcat-openbsd \
        git \
        python3 \
        python3-pip \
        python3-setuptools \
    && rm -rf /var/lib/apt/lists/*

# [Terraform](https://www.terraform.io/downloads.html)
RUN wget --no-check-certificate https://releases.hashicorp.com/terraform/0.14.8/terraform_0.14.8_linux_amd64.zip
RUN unzip terraform_0.14.8_linux_amd64.zip
RUN mv terraform /usr/local/bin/

# [GO](https://golang.org/doc/install)
RUN wget --no-check-certificate https://golang.org/dl/go1.16.2.linux-amd64.tar.gz
RUN tar -C /usr/local -xzf go1.16.2.linux-amd64.tar.gz
ENV PATH="/usr/local/go/bin:/root/go/bin:/root/.local/bin:${PATH}"

RUN GO111MODULE=on go get github.com/zricethezav/gitleaks/v7
RUN pip3 install pre-commit==2.6.0

RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.37.2/install.sh | bash
RUN /bin/bash -c "source /root/.bashrc && nvm install 14.16.0 && npm install --global yarn"

WORKDIR /usr/src
COPY ./ /usr/src

CMD ["/usr/src/entry.sh"]
