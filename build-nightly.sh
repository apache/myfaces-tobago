#!/usr/bin/env bash

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
mvn -Djava14.home=$JAVA14_HOME -Pgenerate-assembly,jdk14retro -U clean install
cd tobago-assembly
mvn -Pgenerate-assembly clean assembly:assembly org.apache.myfaces.maven:wagon-maven-plugin:deploy
cd ../example/tobago-example-assembly
mvn -Pgenerate-assembly clean assembly:assembly org.apache.myfaces.maven:wagon-maven-plugin:deploy

cd ../../..
