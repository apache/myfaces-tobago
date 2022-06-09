# Building

You need Maven 3 and Java 8 or higher to build Tobago 2.

In the main directory you can use

```
mvn install
```

to run the "install" target on all sub-modules.

# Demo

Go to the demo example directory:

```
cd tobago-example/tobago-example-demo
```

Run with Java 8, JSF 2.0, CDI 1 and Jetty 9:
```
mvn jetty:run
```

Run with Java 11, JSF 2.3, CDI 2 and Jetty 10:
```
mvn jetty:run -Djsf=myfaces-2.3
```
