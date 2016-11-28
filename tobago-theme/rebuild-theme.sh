#!/bin/bash

# XXX temporary help script. How to call this like:  mvn -P rebuild-theme without the parent?

set -e

# all _tobago.css must be the same. Later we delete the duplicates...

cmp tobago-theme-standard/src/main/scss/_tobago.scss tobago-theme-charlotteville/src/main/scss/_tobago.scss || exit 1
cmp tobago-theme-standard/src/main/scss/_tobago.scss tobago-theme-richmond/src/main/scss/_tobago.scss || exit 1
cmp tobago-theme-standard/src/main/scss/_tobago.scss tobago-theme-scarborough/src/main/scss/_tobago.scss || exit 1
cmp tobago-theme-standard/src/main/scss/_tobago.scss tobago-theme-speyside/src/main/scss/_tobago.scss || exit 1

echo check ok, building now...

mvn clean

mvn -P rebuild-theme -f tobago-theme-charlotteville/pom.xml
rm tobago-theme-charlotteville/src/main/resources/META-INF/resources/org/apache/myfaces/tobago/renderkit/html/charlotteville/standard/bootstrap/4.0.0-alpha.5/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-richmond/pom.xml
rm tobago-theme-richmond/src/main/resources/META-INF/resources/org/apache/myfaces/tobago/renderkit/html/richmond/standard/bootstrap/4.0.0-alpha.5/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-scarborough/pom.xml
rm tobago-theme-scarborough/src/main/resources/META-INF/resources/org/apache/myfaces/tobago/renderkit/html/scarborough/standard/bootstrap/4.0.0-alpha.5/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-speyside/pom.xml
rm tobago-theme-speyside/src/main/resources/META-INF/resources/org/apache/myfaces/tobago/renderkit/html/speyside/standard/bootstrap/4.0.0-alpha.5/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-standard/pom.xml
rm tobago-theme-standard/src/main/resources/META-INF/resources/org/apache/myfaces/tobago/renderkit/html/standard/standard/bootstrap/4.0.0-alpha.5/css/bootstrap-*

mvn install

echo DONE
echo Now you will find the bootstrap stuff inside the src trees
echo use: svn status
