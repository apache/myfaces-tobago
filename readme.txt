Building
--------
You need Maven 2 or 3 (at least 2.2.1) and Java 6, 7 or 8 to build Tobago.

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
