# Apache Tobago

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Tobago CI](https://github.com/apache/myfaces-tobago/workflows/Tobago%20CI/badge.svg?branch=tobago-4.x)
[![Build Status ASF](https://ci-builds.apache.org/buildStatus/icon?subject=ASF-Build&job=MyFaces%2FTobago+pipeline%2Ftobago-4.x)](https://ci-builds.apache.org/job/MyFaces/job/Tobago%20pipeline/job/tobago-4.x/)

## Building

You need Maven 3 (at least 3.0.4) and Java 8 or later to build Tobago.

In the project directory you can use:

```  
mvn install  

```

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

## Demo

In the directory `tobago-example/tobago-example-demo` call:

```
mvn jetty:run
```
