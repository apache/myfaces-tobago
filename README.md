# Apache Tobago

## Building

You need Maven 3 (at least 3.0.4) and Java 8 or later to build Tobago.

In the project directory you can use:

```  
mvn install  

```

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

## Demo - Jetty

Switch to sub-directory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean jetty:run -Pjetty
```

Browse to the local URL http://localhost:8080/

## Demo - Tomcat in Docker

Switch to sub-directory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean install -Ptomcat
docker run -it --rm -p 8080:8080 -v `pwd`/target/tobago-example-demo.war:/usr/local/tomcat/webapps/tobago-example-demo.war --name tobago-example-demo tomcat:9-jre8
```

Browse to the local URL http://localhost:8080/tobago-example-demo/

## Demo - Quarkus

Switch to sub-directory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn clean install -Pquarkus
java -jar ./target/tobago-example-demo-runner.jar
```

Browse to the local URL http://localhost:8080/
