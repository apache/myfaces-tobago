Building
--------
You need Maven2, Ant 1.6.5 and Java 5 to build Tobago.

In the main directory you can use

  mvn install

to run the install target on all sub projects. This will
put all necessary artifacts into your local repository.

Demo
----
To deploy the demo on your local Tomcat 5.5 create a
~/.m2/settings.xml file based on settings-example.xml.

Make sure the manager user has the role manager; see
<tomcat>/conf/tomcat-users.xml.


  mvn tomcat:deploy
  or
  mvn tomcat:redeploy

Alternatively, you can run

  mvn package

and deploy the WAR from the target directory
manually.

It will create a log file in the app-server starting folder.
You may want to change that in the src/main/webapp/WEB-INF/classes/log4j.xml

Known Bugs
----------
Until we have a public issue tracker see known-bugs.txt.