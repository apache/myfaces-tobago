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

REPO=`mvn help:evaluate -Dexpression=settings.localRepository | grep -v '\[INFO\]'`
echo "Maven repo: ${REPO}"
REPO=`echo ${REPO} | sed s/\\\\//\\\\\\\\\\\\//g`

function rebuild_theme() {
  THEME=${1}
  echo "*** rebuild theme ${THEME} *********************************************************************** "
  DIR=tobago-theme-${THEME}
  CURRENT=`pwd`
  echo "Current dir: ${CURRENT}"
  CURRENT=`echo ${CURRENT} | sed s/\\\\//\\\\\\\\\\\\//g`
  mvn -P rebuild-theme -f ${DIR}/pom.xml | tee ${DIR}/temp.log
  # removing system dependent directories from the log file
  cat ${DIR}/temp.log | sed s/${CURRENT}/__CURRENT__/g | sed s/${REPO}/__REPO__/g >${DIR}/rebuild-theme.log
  rm ${DIR}/temp.log
}

# The rebuild-theme.log files are created, to protocol changes in the build.
# Is seems, that bootstrap.min.js output change, without changing anything, so we assume
# the build process is not time invariant.
# This can later be removed.

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
