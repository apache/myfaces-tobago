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

# Please call this script in its directory

# Click "Download" on this site https://github.com/trentrichardson/jQuery-Timepicker-Addon/archive/v1.4.5.zip
# Unpack the ZIP in a directory and set it here:
SOURCE=~/Downloads/jQuery-Timepicker-Addon-1.4.5
TARGET=../../main/resources/org/apache/myfaces/tobago/renderkit/html/standard/standard
VERSION=1.4.5

patchLocale () {

# TODO: you will need to fix the locale inside the 2 files manually

    if [ "${OLD_LOCALE}" = "sr_RS" ]; then
       echo "*1"
       NEW_LOCALE="_sr"
    elif [ "${OLD_LOCALE}" = "zh_CN" ]; then
       echo "*3"
       NEW_LOCALE="_zh"
    else
       NEW_LOCALE="_${OLD_LOCALE}"
    fi
    echo "${NEW_LOCALE}"
}

# Scripts

cp ${SOURCE}/dist/jquery-ui-timepicker-addon.js ${TARGET}/script/contrib/jquery-ui-timepicker-addon-${VERSION}.js

for FILE in $(find ${SOURCE}/dist/i18n -type file -name "jquery-ui-timepicker-*.js") ; do
  # echo ${FILE};
  # e.g. jquery-ui-timepicker-zh-TW.js -> jquery-ui-timepicker-$VERSION_zh_TW.js
  OLD_LOCALE=`basename ${FILE} | sed "s|jquery-ui-timepicker-||g" | sed "s|.js||g"| sed "s|-|_|g"`

  if [[ "${OLD_LOCALE}" == "addon_i18n" || "${OLD_LOCALE}" == "addon_i18n.min" ]]; then
    # echo "ignoring '${LOCALE}', because we need not the summerized version"
    continue;
  fi

  patchLocale

  NAME=jquery-ui-timepicker-addon-${VERSION}${NEW_LOCALE}.js
  cp ${FILE} ${TARGET}/script/contrib/${NAME}
done

# Styles

cp ${SOURCE}/dist/jquery-ui-timepicker-addon.css ${TARGET}/style/contrib/jquery-ui-timepicker-addon-${VERSION}.css

# Check the svn diff now!
