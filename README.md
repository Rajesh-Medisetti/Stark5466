Table of Contents
=================
<!--ts-->
   * [Table of Contents](#table-of-contents)
   * [Application](#application)
      * [Components](#components)
         * [eqr-test-sdk](#eqr-test-sdk)
      * [Getting Started](#getting-started)
         * [Prerequisites](#prerequisites)
         * [Installing](#installing)
      * [Databases](#databases)
         * [Mongo](#mongo)
      * [Running the tests](#running-the-tests)
      * [Code style](#code-style)
      * [Hot reload](#hot-reload)
      * [Debugging](#debugging)
      * [Profile](#profile)
      * [Running locally without Docker](#running-locally-without-docker)
         * [eqr-test-sdk](#eqr-test-sdk-1)
      * [CI/CD](#cicd)
      * [Deployment](#deployment)
      * [Built With](#built-with)
      * [Contributing](#contributing)
         * [Control tower](#control-tower)
         * [CODEOWNERS](#codeowners)
      * [Authors](#authors)
      * [Acknowledgments](#acknowledgments)

<!-- Added by: prateek, at: Thu Apr  8 01:24:48 IST 2021 -->

<!--te-->


# Application

Short description about the application.

## Components

### eqr-test-sdk

Short description about the component.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need to install Docker and Docker Compose.

```
Docker: https://docs.docker.com/get-docker/
Docker Compose: https://docs.docker.com/compose/install/
```

### Installing

We have used docker-compose.yml to set up all the dependencies. So, you don't need to install anything on your local machine.

Start the application:

```
./run.sh
```

This script does following things:
* Build the docker images.
* Start the docker containers in background.
* Tail logs from all the containers.

If you exit from this running script by pressing `ctrl+c`, it won't stop the running containers. It will just stop tailing the logs. If you run the script again, it will:
* Skip docker build (assuming you didn't change anything to trigger a new build).
* Find that the docker containers are already running, so wouldn't do anything.
* Tail logs from all the containers.

This script is idempotent. To shut down all the containers you need to run:

```
./shutdown.sh
```

We store all the data in `data` folder. To remove the data and start afresh you can run below script. It will shutdown all the containers and delete `data` folder. Note that it won't delete the docker images.

```
./destroy.sh
```

We build the following docker images in development environment.

| Application  | Image  | Tag |
|----------|-----------|------|
| eqr-test-sdk | eqr-test-sdk/eqr-test-sdk | latest-dev |

The eqr-test-sdk runs on

```
http://localhost:8080
```

## Databases

### Mongo

Connect to mongo container and open mongo shell.

```bash
docker exec -it eqr-test-sdk_mongo mongo
```

## Running the tests

You can run tests locally.

```bash
./test_runner.sh development
```

We have enforced test coverage in CI. You can find the latest coverage report [here](https://docs.google.com/spreadsheets/d/1vK3YIJdkM89IfzjRQcutA-14CNvX0ux9XuuDzto0ovM/edit#gid=0).

Whenever you create a new pull request, [coveragelimits.py](eqr-test-sdk/coveragelimits.py) checks if the coverage ratio has reduced from the current value with some threshold. If that happens, then the CI doesn't let you merge the PR, and you need to add more unit tests. After the PR is merged, we calculate the new coverage ratio using the release branch, and update it in the Google spreadsheet. This process ensures that the coverage ratio keeps increasing with time without any superhuman effort.

## Code style

We have a strict set of code style guide enforced in CI. We use [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) along with few more [Checkstyle](https://checkstyle.org/) plugins. You can find all the Java related checks in checkstyle.xml file.

In addition to Java style check, we also have [pre-commit hooks](https://pre-commit.com/).

You can run the code style check locally. This will help you uncover code style issues before pushing code.

```bash
./lint.sh development
```

You should install [google-java-format](https://github.com/google/google-java-format#intellij-android-studio-and-other-jetbrains-ides) to ensure your IDE can format your code as per the Google Java code style guide.

You should install [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea) plugin and import [checkstyle.xml](eqr-test-sdk/checkstyle.xml) to ensure your IDEA follows the same Java code style guide as the CI.

We use Gitleaks to detect hardcoded secrets like passwords, api keys, and tokens - https://github.com/zricethezav/gitleaks

## Hot reload

You need to install Java 11 locally for this to work.

We use `spring-boot-devtools` to automatically restart whenever files on the classpath change. To fully setup your development environment to leverage hot reload, you need to follow these steps:

* You should configure [IntelliJ IDEA](https://www.jetbrains.com/help/idea/compiling-applications.html#auto-build) to build your project automatically, every time you make changes to it. As soon as the project is built, spring-boot-devtools will restart the server.
* Install [LiveReload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei?hl=en) Chrome extension. As soon as the server is restarted, LiveReload will refresh the page.

The above instructions only work when you change Java files. To refresh the page on resource change (HTML/JS/CSS), you need to manually build the project. This can be done from Build-->Build Project or using the shortcut ⌘F9. As soon as the build is complete, LiveReload will automatically refresh the page.

## Debugging

https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#43631bff

* First, we need to set up the run/debug configuration that will be used for attaching to the host application (https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#debugger_rc). Set host=localhost and port=5005.

* Run the configuration you just created (https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#2d0ddd5e).

* https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#108df9bd

## Profile
We have set up below profiles for the eqr-test-sdk.

| Profile  | Use case  |
|----------|-----------|
| ci           | Used by jenkins to run tests and in Dockerfile to build image |
| default      | Used by IntelliJ IDEA for hot reload |
| development  | Used in development environment when running inside docker |
| localhost    | Used in development environment when running on host |
| production   | Used in production environment |
| staging      | Used in staging environment |

## Running locally without Docker

### eqr-test-sdk
You need to install Java 11, Maven 3.6.3 to run the eqr-test-sdk locally without Docker.

```bash
# install sdkman (https://sdkman.io) - The Software Development Kit Manager
curl -s "https://get.sdkman.io" | bash

# install java
sdk install java 11.0.10-open

# install maven
sdk install maven

# run eqr-test-sdk
cd eqr-test-sdk
docker run -p 27017:27017 mongo:3.6.3
mvn clean spring-boot:run -Dspring-boot.run.profiles=localhost -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## CI/CD

We have setup CI/CD by following the guide - https://joveojira.atlassian.net/wiki/spaces/JB/pages/2243166251/CI+CD

Jenkins pipeline - https://jenkins.joveo.com/blue/organizations/jenkins/eqr-test-sdk-final/activity

We build the following docker images in the staging environment.

| Application  | Image  | Tag |
|----------|-----------|------|
| eqr-test-sdk | eqr-test-sdk/eqr-test-sdk | latest |

We build the following docker images in the production environment.

| Application  | Image  | Tag |
|----------|-----------|------|
| eqr-test-sdk | eqr-test-sdk/eqr-test-sdk | stable |

Both `latest` and `stable` are floating tags. They point to the most recent docker image available. In addition to it, we also create docker images with git revision as tag.

## Deployment

We have deployed this application to Kubernetes by following the guide - https://joveojira.atlassian.net/wiki/spaces/JB/pages/2296545431/How+to+deploy+a+new+application

| Staging  | URL  |
|----------|-----------|
| Terraform cloud workspace | https://app.terraform.io/app/joveo-staging/workspaces/k8s-eqr-test-sdk-staging-us-east-1-aws/runs |
| Kubernetes dashboard  | https://kubernetes-dashboard.staging.joveo.com/#/overview?namespace=eqr-test-sdk |
| eqr-test-sdk URL (accessible behind VPN) | https://eqr-test-sdk-api.staging.joveo.com |

| Production  | URL  |
|----------|-----------|
| Terraform cloud workspace | https://app.terraform.io/app/joveo-production/workspaces/k8s-eqr-test-sdk-production-us-east-1-aws/runs |
| Kubernetes dashboard  | https://kubernetes-dashboard.joveo.com/#/overview?namespace=eqr-test-sdk |
| eqr-test-sdk URL (accessible behind VPN) | https://eqr-test-sdk-api.joveo.com |

## Built With

* [Java 11](https://www.java.com/en/) - Java is a class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.
* [Spring Boot 2.4.3](https://spring.io/projects/spring-boot) - Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
* [Maven 3.6.3](https://maven.apache.org/) - Maven is a build automation tool used primarily for Java projects.
* [React 17.0.1](https://reactjs.org/) - A JavaScript library for building user interfaces.
* [Yarn 1.22.5](https://yarnpkg.com/) - Yarn is a package manager that doubles down as project manager.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

Install [github-markdown-toc](https://github.com/ekalinin/github-markdown-toc) if you want to make any changes to README.md file. Run `gh-md-toc --no-backup README.md` to automatically update table of contents.

### Control tower

We use control tower for merging pull requests - https://joveojira.atlassian.net/wiki/spaces/JB/pages/2457174030/Control+Tower

JIRA project - https://joveojira.atlassian.net/jira/software/projects/EQRTS/issues/

### CODEOWNERS

Code owners are automatically requested for review when someone opens a pull request that modifies code that they own. Please look at [CODEOWNERS](CODEOWNERS) file.

https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/about-code-owners

## Authors

* Prateek Mishra

See also the list of [contributors](https://github.com/joveo/eqr-test-sdk/contributors) who participated in this project.

## Acknowledgments

* Hat tip to anyone whose code was used.
