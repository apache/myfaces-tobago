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

usage()
{
  echo "Usage: $0"
  exit 1
}

if [ -r ./settings ]
then
  source ./settings
fi

##############################################################################
# Do some checks of the enviroment
##############################################################################

if [ -z "$JAVA_HOME" ]
then
  echo "JAVA_HOME must be set."
fi

if [ -z "`which java`" ]
then
  echo "Could not find 'java' in the path."
  echo "PATH: $PATH"
  exit 1
fi


##############################################################################
# Build
##############################################################################


echo "Building tobago site."
mvn clean
mvn install javancss:report jxr:jxr jxr:test-jxr clirr:clirr -Daggregate=false --batch-mode
mvn site:site -Daggregate=true --batch-mode
mvn dashboard:dashboard
mvn site:deploy -e -Daggregate=true --batch-mode
