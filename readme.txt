Building
--------
You need Maven2 (at least 2.0.6) and Java 5 to build Tobago.

In the main directory you can use

  mvn install

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

Building All
-----------
For building all the stuff you can use the profile all-modules

mvn install -Pall-modules

Unfortunately you need two artifacts

el-api.jar and el-ri.jar

from the facelet distribution

 http://facelets.dev.java.net

You have to download the facelets 1.1.12 distribution
(the el-api.jar and el-ri.jar has been renamed in the facelets 1.1.13 version,
which would require changes in the poms). 

Please install the missing public artifacts with

 mvn install:install-file -Dfile=el-api.jar -DgroupId=javax.el \
 -DartifactId=el-api -Dversion=1.0 -Dpackaging=jar

 mvn install:install-file -Dfile=el-ri.jar -DgroupId=com.sun.el \
 -DartifactId=el-ri -Dversion=1.0 -Dpackaging=jar
 
Demo
----
To deploy the demo on your local Tomcat 5.5 create a
~/.m2/settings.xml file based on settings-example.xml.

Make sure the manager user has the role manager; see
<tomcat>/conf/tomcat-users.xml.


  mvn tomcat:deploy
  or
  mvn tomcat:undeploy tomcat:deploy

Alternatively, you can run

  mvn package

and deploy the WAR from the target directory
manually.

It will create a log file in the app-server starting folder.
You may want to change that in the src/main/webapp/WEB-INF/classes/log4j.xml


