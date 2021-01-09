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

FROM tomcat:8.5-jdk8-adoptopenjdk-hotspot
MAINTAINER dev@myfaces.apache.org

# ansible is needed for downloading from Nexus, todo: may be changed to mvn
RUN apt-get update \
 && apt-get install -y less vim procps ansible python3-lxml\
 && apt-get clean

RUN rm -r /usr/local/tomcat/webapps.dist

COPY server.xml /usr/local/tomcat/conf
COPY download-deploy-and-run.sh /usr/local/tomcat/bin
RUN chmod +x /usr/local/tomcat/bin/download-deploy-and-run.sh

#ENV TOBAGO_VERSION undefined
#ENV CONTEXT_PATH ROOT

CMD ["download-deploy-and-run.sh"]
