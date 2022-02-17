# Apache Tobago

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Tobago CI](https://github.com/apache/myfaces-tobago/workflows/Tobago%20CI/badge.svg)
![CodeQL](https://github.com/apache/myfaces-tobago/workflows/CodeQL/badge.svg)
[![Build Status ASF](https://ci-builds.apache.org/buildStatus/icon?subject=ASF-Build&job=MyFaces%2FTobago+pipeline%2Fmaster)](https://ci-builds.apache.org/job/MyFaces/job/Tobago%20pipeline/job/master/)

## Building

You need Maven 3 and Java 8 or later to build Tobago.

In the project directory you can use:

```
mvn clean install
```

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

## Demo - Jetty - MyFaces - OWB
### ⚠ currently broken ⚠
* Problem with CDI

Switch to sub-directory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean jetty:run -Pjetty
```

Browse to the local URL http://localhost:8080/

## Demo - Jetty - Mojarra - Weld
### ⚠ some problems ⚠
* AJAX is not working

Switch to sub-directory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean jetty:run -Pjetty -Djsf=mojarra-3.0
```

Browse to the local URL http://localhost:8080/

## Demo - Tomcat - Mojarra - Weld (in Docker)
### ⚠ some problems ⚠
* AJAX is not working

Switch to sub-directory and call Maven to build and Docker to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean install -Djsf=mojarra-3.0
docker run -it --rm -p 8080:8080 -v `pwd`/target/tobago-example-demo.war:/usr/local/tomcat/webapps/tobago-example-demo.war --name tobago-example-demo tomcat:10.0-jre11
```

Browse to the local URL http://localhost:8080/tobago-example-demo/
