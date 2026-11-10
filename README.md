# Building

You need Maven 3 and Java 21 or higher to build Tobago 2.

In the main directory you can use

```
mvn install
```

to run the "install" target on all submodules.

# Demo

Go to the demo example directory:

```
cd tobago-example/tobago-example-demo
```

## Jetty 9

Run with Java 8, JSF 2.0, CDI 1 and Jetty 9:
```
mvn clean jetty:run
```

Browse to http://localhost:8080/

## Jetty 10

Run with Java 11, JSF 2.3, CDI 2 and Jetty 10:
```
mvn clean jetty:run -Djsf=myfaces-2.3
```

Browse to http://localhost:8080/
