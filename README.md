[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Tobago CI](https://github.com/apache/myfaces-tobago/actions/workflows/tobago-ci.yml/badge.svg?branch=tobago-5.x)](https://github.com/apache/myfaces-tobago/actions/workflows/tobago-ci.yml)
[![CodeQL](https://github.com/apache/myfaces-tobago/actions/workflows/codeql-analysis.yml/badge.svg?branch=tobago-5.x)](https://github.com/apache/myfaces-tobago/actions/workflows/codeql-analysis.yml)
[![Build Status ASF](https://ci-builds.apache.org/buildStatus/icon?subject=ASF-Build&job=MyFaces%2FTobago+pipeline%2Ftobago-5.x)](https://ci-builds.apache.org/job/MyFaces/job/Tobago%20pipeline/job/tobago-5.x/)

# Apache Tobago


## Building

You need Maven 3 (at least 3.0.4) and Java 8 or later to build Tobago.

In the project directory you can use:

```
mvn clean install

```

to run the **install** target on all subprojects. This will
put all necessary artifacts into your local repository.

## Demo - Jetty

Switch to subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean jetty:run -Pjetty
```

Browse to the local URL http://localhost:8080/

## Demo - TomEE

Switch to subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean package -Ptomee tomee:run
```

Browse to the local URL http://localhost:8080/

## Demo - Open Liberty

Switch to subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean -Pliberty liberty:run
```

Browse to the local URL http://localhost:9080/

## Demo - Tomcat in Docker

Switch to subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean install -Ptomcat
docker run -it --rm -p 8080:8080 -v `pwd`/target/tobago-example-demo.war:/usr/local/tomcat/webapps/tobago-example-demo.war --name tobago-example-demo tomcat:9-jre8
```

Browse to the local URL http://localhost:8080/tobago-example-demo/

## Demo - Quarkus

Switch to subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean install -Pquarkus
java -jar ./target/tobago-example-demo-runner.jar
```

Browse to the local URL http://localhost:8080/

## Demo - Spring Boot

Switch to **special** subdirectory and call Maven to run the demo:

```
cd tobago-example/tobago-example-spring-boot
mvn clean spring-boot:run
```

Browse to the local URL http://localhost:8080/
