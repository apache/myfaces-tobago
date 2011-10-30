Building
--------
You need Maven2 (at least 2.2.1) and Java 5 (Tobago 1.0.x) or Java 6 (Tobago 1.5.x) to build Tobago.

In the main directory you can use

  mvn install

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

Building All
-----------
For building all the stuff you can use the profile all-modules

mvn install -Pall-modules

Demo
----

In the directory tobago-demo/tobago-example-demo

mvn jetty:run
