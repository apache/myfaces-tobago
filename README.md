[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Tobago CI](https://github.com/apache/myfaces-tobago/actions/workflows/tobago-ci.yml/badge.svg?branch=tobago-5.x)](https://github.com/apache/myfaces-tobago/actions/workflows/tobago-ci.yml)
[![CodeQL](https://github.com/apache/myfaces-tobago/actions/workflows/codeql-analysis.yml/badge.svg?branch=tobago-5.x)](https://github.com/apache/myfaces-tobago/actions/workflows/codeql-analysis.yml)
[![Build Status ASF](https://ci-builds.apache.org/buildStatus/icon?subject=ASF-Build&job=MyFaces%2FTobago+pipeline%2Ftobago-5.x)](https://ci-builds.apache.org/job/MyFaces/job/Tobago%20pipeline/job/tobago-5.x/)

# Apache Tobago

Apache MyFaces Tobago is a JSF component framework that pays special attention to security and is optimized for
business applications.

Tobago supports custom themes based on the popular Bootstrap framework and frees the user from having to develop
complex CSS and JavaScript components.

Compared to many other JSF frameworks, Tobago is still being actively developed and is used in many projects.

# Getting Started

## Prerequisites

* A Git client to check out this project (may part of your IDE)
* [JDK17]( https://www.oracle.com/technetwork/java/javase/downloads) or higher
* [Maven 3](https://maven.apache.org/download.cgi)

## Building

In the project directory you can use:

```
mvn clean install
```

to build the project with all submodules.
This will put all necessary artifacts into your local repository.

If there are changes in the `tobago-theme` module in TypeScript or other sources,
you need to build with **frontend** profile:

```
mvn clean install -Pfrontend
```

## Live Demo

You can find a live demo of all components at [https://tobago-vm.apache.org/](https://tobago-vm.apache.org/).

## Examples

See `tobago-example` directory for some simple examples on how to use Tobago:

* [tobago-example-blank](tobago-example/tobago-example-blank) a minimal hello world application,
* [tobago-example-demo](tobago-example/tobago-example-demo) a demo and documentation application,
* [tobago-example-spring-boot](tobago-example/tobago-example-spring-boot) same demo using spring-boot.

**Demo**

```shell
mvn clean install
cd tobago-example/tobago-example-demo
```

***Jetty, MyFaces and OpenWebBeans***

If you want to run the demo locally you need to run the following command:

```shell
mvn jetty:run -Pjetty -Pdev
```

Browse to the local URL http://localhost:8080/

You can also run the demo with different servers and JSF implementations:

***Jetty, Mojarra and OpenWebBeans***

```shell
mvn jetty:run -Pjetty -Pdev -Djsf=mojarra-2.3
```

Browse to the local URL http://localhost:8080/

***Tomcat***

```shell
mvn clean package -Ptomcat
mvn cargo:run -Ptomcat
```

Browse to the local URL http://localhost:8080/tobago-example-demo/

***TomEE***

```shell
mvn tomee:run -Ptomee
```

Browse to the local URL http://localhost:8080/

***Open Liberty***

```shell
mvn liberty:run -Pliberty
```

Browse to the local URL http://localhost:9080/

***Tomcat in Docker***

```
mvn clean package -Pdocker
docker run -p 8080:8080 myfaces/tobago-example-demo:latest
```

Browse to the local URL http://localhost:8080/

***Spring Boot***

Switch to **different** subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-spring-boot
mvn clean -Pspring-boot-module spring-boot:run
```

Browse to the local URL http://localhost:8080/

**Note:** The dependencies for Spring Boot are outdated, because there is no public support for
Spring Boot 2 anymore.
Please use Tobago 6 with Spring Boot 3.

# Issue Tracking

If you find any issues regarding MyFaces Tobago you can use the [Apache Issue Tracker](https://issues.apache.org/jira/projects/TOBAGO) to report them.

# Contributing

[Pull requests](https://help.github.com/articles/creating-a-pull-request) are welcome; see the [contributor guidelines](CONTRIBUTING.md) for details.

# Licensing

The terms for software licensing are detailed in the [LICENSE.txt](LICENSE.txt) file.
