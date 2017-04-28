#!/bin/sh
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
