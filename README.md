# Apache Tobago

## Building

You need Maven 3 (at least 3.0.4) and Java 8 or later to build Tobago.

In the project directory you can use:

```  
mvn install  

```

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

## Demo

Switch to sub-directory and call Maven to run the demo:

```
cd tobago-example/tobago-example-demo
mvn jetty:run
```

Browse to the local URL http://localhost:8080/tobago-example-demo/