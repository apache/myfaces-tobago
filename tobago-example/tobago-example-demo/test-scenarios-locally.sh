#!/bin/bash

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

# Test if different combinations are starting successfully
# JDK
# ProductionMode
# Servers: Jetty, TomEE
# todo: Springboot, Tomcat, Liberty, ...

WORK=test-scenarios-locally/$(date -u +"%Y-%m-%dT%H:%M:%SZ")

${JAVA_HOME_17}/bin/java -version
if [ $? != 0 ]; then
  echo "Java 17 (LTS) not found!"
  exit 1
fi

${JAVA_HOME_21}/bin/java -version
if [ $? != 0 ]; then
  echo "Java 21 (LTS) not found!"
  exit 1
fi

isPortInUse() {
#  PORT=$1
  if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
    return 0
  else
    return 1
  fi
}

isPidRunning() {
#  PID=$1
  if kill -0 $1 >/dev/null 2>&1 ; then
    return 0
  else
    return 1
  fi
}

error() {
#  TEXT=$1
#  PID=$2
  echo $1 1>&2
  shutdown $2
  exit 1
}

shutdown() {
#  PID=$1
  echo "Killing PID $1"
  if [[ "$1" -ne "" ]]; then
    while isPidRunning $1; do
      kill $1
      echo -n "."
      sleep 1
    done
    echo
  fi
}

mkdir -p ${WORK}

check() {
#  JAVA_VERSION=$1
#  COMMAND_LINE=$2

  COUNTER=$((COUNTER+1))

  echo "+--------------------------------------------------------------------------------------------------+"
  echo "+ Java version: $1"
  echo "+ Command line: $2"
  echo "+ Label:        $3"
  echo "+ Mode:         $4"
  echo "+ Run:          #${COUNTER}"
  echo "+--------------------------------------------------------------------------------------------------+"

  PORT=8080

  if isPortInUse ${PORT} ; then
    error "Error: Port ${PORT} is already in use!"
  fi

  case "$1" in
  17)
    export JAVA_HOME=${JAVA_HOME_17}
    ;;
  21)
    export JAVA_HOME=${JAVA_HOME_21}
    ;;
  *)
    echo "Unknown java version ${JAVA_VERSION}"
    exit 1
  esac

  MAVEN_LOG="${WORK}/maven-${COUNTER}-$3.log"
#  CURL_LOG="${WORK}/curl-${COUNTER}-$3.log"

  # start process
  set -x
  $2 >"${MAVEN_LOG}" 2>&1 &
  set +x
  PID=$!

  # ensure process has been started and is listening the port
  while ! isPortInUse ${PORT}; do
    if isPidRunning ${PID}; then
      echo -n "."
    else
      echo "Process ${PID} not found!" 1>&2
      exit 1
    fi
    sleep 1
  done

  echo "Found a process ${PID} on port ${PORT} running!"
  echo "Now testing..."

  STATUS=$(curl -o ${WORK}/JSR_303.xhtml -w "%{http_code}" http://localhost:${PORT}/content/170-validation/01/JSR_303.xhtml)

  if [[ $? -gt 0 ]] ; then
    error "The curl command has failed!" ${PID}
  fi

  if [[ ${STATUS} -ne 200 ]] ; then
    error "The curl command has failed with status code ${STATUS}" ${PID}
  fi

  grep -q "JSR 303" ${WORK}/JSR_303.xhtml

  if [[ $? -gt 0 ]] ; then
    error "The HTML output is incorrect!" ${PID}
  fi

  shutdown ${PID}

  echo "Check successful!"
}

# run with Maven

# xxx -Pprod doesn't exist, but this is no problem
for MODE in "dev" "prod" ; do
  for JAVA_VERSION in 17 21; do
    check ${JAVA_VERSION} "mvn clean jetty:run -P${MODE} -Pjetty"              "Jetty 9 with MyFaces 2.3"
    check ${JAVA_VERSION} "mvn clean jetty:run -P${MODE} -Pjetty -Pjee8"     "Jetty 10 with MyFaces 2.3"
    check ${JAVA_VERSION} "mvn clean jetty:run -P${MODE} -Djsf=mojarra-2.3"    "Jetty 9 with Mojarra 2.3"
    check ${JAVA_VERSION} "mvn clean package tomee:run -P${MODE} -Ptomee"    "TomEE 8 with MyFaces 2.3"
  done
done

# run with Maven

mvn clean install -Ptomcat -Pdev && docker run -it --rm -p 8080:8080 -v `pwd`/target/tobago-example-demo.war:/usr/local/tomcat/webapps/demo.war tomcat:9-jdk8

echo "+--------------------------------------------------------------------------------------------------+"
echo "All checks successful!"
echo "+--------------------------------------------------------------------------------------------------+"
