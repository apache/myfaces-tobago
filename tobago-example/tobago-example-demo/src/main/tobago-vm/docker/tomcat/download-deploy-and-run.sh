#! /bin/bash

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

if [[ ${TOBAGO_VERSION} == *-SNAPSHOT ]]; then
   REPO=https://repository.apache.org/content/repositories/snapshots
elif [[ ${STAGING_NUMBER} ]]; then
   REPO=https://repository.apache.org/content/repositories/orgapachemyfaces-${STAGING_NUMBER}/
else
   REPO=https://repository.apache.org/content/repositories/releases
fi

TARGET=/usr/local/tomcat/webapps/${CONTEXT_PATH}
ARTIFACT=/opt/docker/artifacts/tobago-example-demo-${TOBAGO_VERSION}.war

ansible localhost -m maven_artifact -a "group_id=org.apache.myfaces.tobago artifact_id=tobago-example-demo version=${TOBAGO_VERSION} extension=war repository_url=${REPO} dest=${ARTIFACT}"
mkdir ${TARGET}
pushd ${TARGET} && jar xf ${ARTIFACT}

catalina.sh run
