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

# Click "Download" on this site http://jqueryui.com/download/#!version=1.10.4&components=1111100000001000100000000000000000
# Unpack the ZIP in a directory and set it here:
SOURCE=~/Downloads/jquery-ui-1.10.4.custom
TARGET=../../main/resources/org/apache/myfaces/tobago/renderkit/html/standard/standard
VERSION=1.10.4

patchLocale () {

# TODO: you will need to fix the locale inside the 3 files manually

    if [ "${OLD_LOCALE}" = "cy_GB" ]; then
       echo "*1"
       NEW_LOCALE="_cy"
#    elif [ "${OLD_LOCALE}" = "en_GB" ]; then
#       echo "*2"
#       NEW_LOCALE="_en"
    elif [ "${OLD_LOCALE}" = "zh_CN" ]; then
       echo "*3"
       NEW_LOCALE="_zh"
    else
       NEW_LOCALE="_${OLD_LOCALE}"
    fi
    echo "${NEW_LOCALE}"
}

# Scripts

cp ${SOURCE}/js/jquery-ui-${VERSION}.custom.js ${TARGET}/script/contrib
cp ${SOURCE}/js/jquery-ui-${VERSION}.custom.min.js ${TARGET}/script/contrib

for FILE in $(find ${SOURCE}/development-bundle/ui/i18n -type file -name "jquery.ui.datepicker-*.js") ; do
  # echo ${FILE};
  # e.g. jquery.ui.datepicker-zh-TW.js -> jquery-ui-datepicker-$VERSION_zh_TW.js
  OLD_LOCALE=`basename ${FILE} | sed "s|jquery.ui.datepicker-||g" | sed "s|.js||g"| sed "s|-|_|g"`
  patchLocale
  NAME=jquery-ui-datepicker-i18n-${VERSION}${NEW_LOCALE}.js
  cp ${FILE} ${TARGET}/script/contrib/${NAME}
done

for FILE in $(find ${SOURCE}/development-bundle/ui/minified/i18n -type file -name "jquery.ui.datepicker-*.min.js") ; do
  # echo ${FILE};
  # e.g. jquery.ui.datepicker-zh-TW.js -> jquery-ui-datepicker-$VERSION_zh_TW.js
  OLD_LOCALE=`basename ${FILE} | sed "s|jquery.ui.datepicker-||g" | sed "s|.min.js||g"| sed "s|-|_|g"`
  patchLocale
  NAME=jquery-ui-datepicker-i18n-${VERSION}${NEW_LOCALE}.min.js
  cp ${FILE} ${TARGET}/script/contrib/${NAME}
done

# Styles

cp -r ${SOURCE}/css/ui-lightness ${TARGET}/style/contrib

# Check the svn diff now!
