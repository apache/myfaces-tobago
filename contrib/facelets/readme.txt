Building
--------

The el jars are not available on
a public maven repository.

You have to download the facelet distribution from:

http://facelets.dev.java.net

and install the missing artifact with

 mvn install:install-file -Dfile=el-api.jar -DgroupId=javax.el \
 -DartifactId=el-api -Dversion=1.0 -Dpackaging=jar

 mvn install:install-file -Dfile=el-ri.jar -DgroupId=com.sun.el \
 -DartifactId=el-ri -Dversion=1.0 -Dpackaging=jar


This arifact requires the tobago-core-sources.jar artifact.
The tobago-core-sources.jar is generated with the profile
attach-source in core (mvn -P attach-source) or
with the profile all-modules (mvn -P all-modules).
