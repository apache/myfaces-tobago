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

set -e
#set -x
tobago_repo="http://svn.apache.org/repos/asf/myfaces/tobago/trunk"

usage()
{
  echo "Usage: $0"
  exit 1
}

if [ -r ./settings ]
then
  source ./settings
fi

if [ -z "$NIGHTLY_ROOT" ]
then
  echo "NIGHTLY_ROOT must be set"
  exit 1
fi

root="$NIGHTLY_ROOT"

mkdir -p $root

##############################################################################
# Do some checks of the enviroment
##############################################################################

if [ -z "$JAVA_HOME" ]
then
  echo "JAVA_HOME must be set."
fi

if [ -z "$JAVA14_HOME" ]
then
  echo "JAVA14_HOME must be set."
fi


if [ -z "`which java`" ]
then
  echo "Could not find 'java' in the path."
  echo "PATH: $PATH"
  exit 1
fi


##############################################################################
# Check out the sources
##############################################################################

cd $root

#  tobago

echo "Updating the tobago checkout."

svn co $tobago_repo tobago


##############################################################################
# Build
##############################################################################


echo "Building tobago ."
cd tobago
mvn -Djava14.home=$JAVA14_HOME -Dnon-default-modules -Pgenerate-assembly,jdk14retro -U clean install
cd tobago-assembly
mvn -Pgenerate-assembly clean package org.apache.myfaces.maven:wagon-maven-plugin:deploy
cd ../tobago-example/tobago-example-assembly
mvn -Pgenerate-assembly clean package org.apache.myfaces.maven:wagon-maven-plugin:deploy

cd ../../..
