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

function rebuild_theme() {
  THEME=${1}
  echo "*** rebuild theme ${THEME} *********************************************************************** "
  mvn -P rebuild-theme -f tobago-theme-${THEME}/pom.xml | tee tobago-theme-${THEME}/rebuild-theme.log
}

# The rebuild-theme.log files are created, to protocol changes in the build.
# Is seems, that bootstrap.min.js output change, without changing anything, so we assume
# the build process is not time invariant.
# This can later be removed.

set -e

mvn -P all-modules clean

rebuild_theme charlotteville
rebuild_theme richmond
rebuild_theme scarborough
rebuild_theme speyside
rebuild_theme standard

mvn -P all-modules install

echo "DONE"
echo "Now you will find the bootstrap stuff inside the src trees. This might be committed."
echo "Use: svn status"
