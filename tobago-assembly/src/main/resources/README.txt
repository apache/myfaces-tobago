Thank you for using Apache Tobago.

The normal distribution of Apache Tobago requires Java 5.

If you like to use Tobago with JDK 1.4,
please, replace the following JARs

tobago-core.jar,
tobago-theme-speyside.jar and
tobago-theme-scarborough.jar

in the lib directory with the JDK 1.4
versions in the jdk14retro directory.

The JARs

tobago-theme-charlotteville.jar,
tobago-theme-richmond.jar and
tobago-theme-standard.jar

don't need a JDK 1.4 version, because they don't contain any classes.

Addtionally add the retrotranslator-runtime-1.0.8.jar from
http://www.ibiblio.org/maven2/net/sf/retrotranslator/retrotranslator-runtime/
to the WEB-INF/lib directory of your WAR.

NOTE: The JDK 1.4 version is work in progress.

The contrib directory contains the tobago-facelets.jar and a retrotranslated facelets.jar.