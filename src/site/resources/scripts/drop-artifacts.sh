#!/bin/sh

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

echo "Removing old download artifacts from the site."

if [ $# -ne 1 ]; then
    echo "Usage: $0 <version>"
    exit 1
fi

VERSION=$1

DIST_REPOSITORY=https://dist.apache.org/repos/dist/release/myfaces

svn rm -m "Cleaning up old release artifact ${VERSION} from dist server" \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.tar.gz \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.tar.gz.asc \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.tar.gz.md5 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.tar.gz.sha1 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.tar.gz.sha256 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.zip \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.zip.asc \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.zip.md5 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.zip.sha1 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-dist.zip.sha256 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.tar.gz \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.tar.gz.asc \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.tar.gz.md5 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.tar.gz.sha1 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.tar.gz.sha256 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.zip \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.zip.asc \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.zip.md5 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.zip.sha1 \
${DIST_REPOSITORY}/binaries/myfaces-tobago-${VERSION}-example.zip.sha256 \
${DIST_REPOSITORY}/source/myfaces-tobago-${VERSION}-source-release.zip \
${DIST_REPOSITORY}/source/myfaces-tobago-${VERSION}-source-release.zip.asc \
${DIST_REPOSITORY}/source/myfaces-tobago-${VERSION}-source-release.zip.md5 \
${DIST_REPOSITORY}/source/myfaces-tobago-${VERSION}-source-release.zip.sha1 \
${DIST_REPOSITORY}/source/myfaces-tobago-${VERSION}-source-release.zip.sha256
