[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Tobago CI](https://github.com/apache/myfaces-tobago/actions/workflows/tobago-ci.yml/badge.svg)](https://github.com/apache/myfaces-tobago/actions/workflows/tobago-ci.yml)
[![CodeQL](https://github.com/apache/myfaces-tobago/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/apache/myfaces-tobago/actions/workflows/codeql-analysis.yml)
[![Build Status ASF](https://ci-builds.apache.org/buildStatus/icon?subject=ASF-Build&job=MyFaces%2FTobago+pipeline%2Fmain)](https://ci-builds.apache.org/job/MyFaces/job/Tobago%20pipeline/job/main/)

# Apache Tobago

Apache MyFaces Tobago is a JSF component framework that pays special attention to security and is optimized for business applications.

Tobago supports custom themes based on the popular Bootstrap framework and frees the user from having to develop complex CSS and JavaScript components.

Compared to many other JSF frameworks, Tobago is still being actively developed and is used in many projects.

# Table of Contents

- [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Building](#building)
- [Live Demo](#live-demo)
- [Examples](#examples)
- [Issue Tracking](#issue-tracking)
- [Contributing](#contributing)
- [Licensing](#licensing)

# Getting Started

## Prerequisites

[Git](https://help.github.com/set-up-git-redirect), [Maven 3](https://maven.apache.org/download.cgi) and at least [JDK8]( https://www.oracle.com/technetwork/java/javase/downloads).

## Building

In the project directory you can use:

```
mvn clean install
```

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

## Live Demo

You can find a live demo of all components at [https://tobago-vm.apache.org/](https://tobago-vm.apache.org/).

## Examples

See `tobago-examples` directory for some simple examples on how to use Tobago.

**Demo**

If you want to run the demo locally you need to run the following command:

```shell
mvn -f tobago-example/tobago-example-demo/pom.xml clean package tomee:run -Ptomee -Pdev
```

* Mode 1: old | compatible | classic | useShowAttributes
* Mode 2: auto
* Mode 3: custom

Browse to the local URL http://localhost:8080/

You can also run the demo with different servers and JSF implementations:

**Jetty, MyFaces and Weld**

```shell
mvn -f tobago-example/tobago-example-demo/pom.xml clean package jetty:run -Pjetty -Pdev
```

**Jetty, Mojarra and Weld**

```shell
mvn -f tobago-example/tobago-example-demo/pom.xml clean package jetty:run -Pjetty -Pdev -Djsf=mojarra-4.0
```

**Tomcat**

```shell
mvn -f tobago-example/tobago-example-demo/pom.xml package -Ptomcat cargo:run
```

**TomEE (currently broken)**

```shell
mvn -f tobago-example/tobago-example-demo/pom.xml clean package -Ptomee tomee:run
```

**Open Liberty**

```shell
mvn -f tobago-example/tobago-example-demo/pom.xml clean -Pliberty liberty:run
```

Browse to the local URL http://localhost:9080/

**Tomcat in Docker**

```
mvn -f tobago-example/tobago-example-demo/pom.xml clean package -Pdocker
docker run -p 8080:8080 myfaces/tobago-example-demo:latest
```

Browse to the local URL http://localhost:8080/

# Issue Tracking

If you find any issues regarding MyFaces Tobago you can use the [Apache Issue Tracker](https://issues.apache.org/jira/projects/TOBAGO) to report them.

# Contributing

[Pull requests](https://help.github.com/articles/creating-a-pull-request) are welcome; see the [contributor guidelines](CONTRIBUTING.md) for details.

# Licensing

The terms for software licensing are detailed in the [LICENSE.txt](LICENSE.txt) file.
