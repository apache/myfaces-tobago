#!/usr/bin/env bash

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# XXX temporary help script. How to call this like:  mvn -P rebuild-theme only for the children?

set -e

TOBAGO_VERSION=3.1.0
BOOTSTRAP_VERSION=4.0.0-alpha.5

mvn -P all-modules clean

mvn -P rebuild-theme -f tobago-theme-charlotteville/pom.xml
rm tobago-theme-charlotteville/src/main/resources/META-INF/resources/tobago/charlotteville/bootstrap/${BOOTSTRAP_VERSION}-${TOBAGO_VERSION}/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-richmond/pom.xml
rm tobago-theme-richmond/src/main/resources/META-INF/resources/tobago/richmond/bootstrap/${BOOTSTRAP_VERSION}-${TOBAGO_VERSION}/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-scarborough/pom.xml
rm tobago-theme-scarborough/src/main/resources/META-INF/resources/tobago/scarborough/bootstrap/${BOOTSTRAP_VERSION}-${TOBAGO_VERSION}/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-speyside/pom.xml
rm tobago-theme-speyside/src/main/resources/META-INF/resources/tobago/speyside/bootstrap/${BOOTSTRAP_VERSION}-${TOBAGO_VERSION}/css/bootstrap-*

mvn -P rebuild-theme -f tobago-theme-standard/pom.xml
rm tobago-theme-standard/src/main/resources/META-INF/resources/tobago/standard/bootstrap/${BOOTSTRAP_VERSION}-${TOBAGO_VERSION}/css/bootstrap-*

mvn -P all-modules install

echo DONE
echo Now you will find the bootstrap stuff inside the src trees. This might be committed.
echo use: svn status
