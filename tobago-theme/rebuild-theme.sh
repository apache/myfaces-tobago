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

# helper script for faster rebuilding the themes, because it runs in parallel

set -e

START=$(date +%s)

echo "Home: ${HOME}"

function rebuild_theme() {
  THEME=${1}
  echo "*** rebuild theme ${THEME} *********************************************************************** "
  DIR=tobago-theme-${THEME}

  mvn clean install -Prebuild-theme -f ${DIR}/pom.xml
}

rebuild_theme charlotteville &
rebuild_theme roxborough &
rebuild_theme scarborough &
rebuild_theme speyside &
rebuild_theme standard &

wait

END=$(date +%s)

echo "DONE in $(($END - $START)) seconds"
