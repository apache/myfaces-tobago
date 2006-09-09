Thank you for using Apache Tobago.

The normal distribution of Apache Tobago requires jdk 1.5.

If you like to use tobago with a jdk 1.4,
you should replace following jars

tobago-core.jar,
tobago-theme-speyside.jar,
tobago-theme-scarborough.jar and
tobago-theme-standard.jar

in the lib directory with the jdk 1.4
version in jdk14retro directory, please.

The jars

tobago-theme-charlotteville.jar and
tobago-theme-richmond.jar

doesn't need a jdk 1.4 version, because they doesn't contain any classes.

And add the retrotranslator-runtime-1.0.8.jar from
http://www.ibiblio.org/maven2/net/sf/retrotranslator/retrotranslator-runtime/
to the WEB-INF/lib dir of your war.

NOTE: The jdk 1.4 version is a work in progress.

