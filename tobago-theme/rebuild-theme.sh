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
REPO_REGEX=`echo ${REPO} | sed s/\\\\//\\\\\\\\\\\\//g`

echo "Home: ${HOME}"
HOME_REGEX=`echo ${HOME} | sed s/\\\\//\\\\\\\\\\\\//g`

function rebuild_theme() {
  THEME=${1}
  echo "*** rebuild theme ${THEME} *********************************************************************** "
  DIR=tobago-theme-${THEME}
  CURRENT=`pwd`
  echo "Current dir: ${CURRENT}"
  CURRENT_REGEX=`echo ${CURRENT} | sed s/\\\\//\\\\\\\\\\\\//g`
  mkdir -p ${DIR}/target

  date "+Build date: %Y-%m-%d %H:%M:%S" >${DIR}/target/temp.log

  mvn -P rebuild-theme -f ${DIR}/pom.xml | tee -a ${DIR}/target/temp.log

  # removing system dependent directories from the log file
  cat ${DIR}/target/temp.log | sed s/${CURRENT_REGEX}/__CURRENT__/g | sed s/${REPO_REGEX}/__REPO__/g | sed s/${HOME_REGEX}/__HOME__/g >${DIR}/rebuild-theme.log
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
