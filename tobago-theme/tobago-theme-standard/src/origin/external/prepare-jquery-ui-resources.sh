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

# Click "Download" on this site https://jqueryui.com/download/#!version=1.13.1&components=111000110100100000000010010010000000000000000000
# Unpack the ZIP in a directory and set it here:
VERSION=1.13.1
SOURCE=~/Downloads/jquery-ui-${VERSION}.custom
TARGET=../../main/resources/org/apache/myfaces/tobago/renderkit/html/standard/standard

patchLocale () {

# TODO: you will need to fix the locale inside the 3 files manually

    if [ "${OLD_LOCALE}" = "cy_GB" ]; then
       NEW_LOCALE="_cy"
#    elif [ "${OLD_LOCALE}" = "en_GB" ]; then
#       NEW_LOCALE="_en"
    elif [ "${OLD_LOCALE}" = "zh_CN" ]; then
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
  NAME=jquery-ui-${VERSION}.custom${NEW_LOCALE}.js
  cp ${FILE} ${TARGET}/script/contrib/${NAME}
done

# TBD: so small files... do we need a minified version? If yes, fix the i18n with "min"
#for FILE in $(find ${SOURCE}/development-bundle/ui/minified/i18n -type file -name "jquery.ui.datepicker-*.min.js") ; do
#  # echo ${FILE};
#  # e.g. jquery.ui.datepicker-zh-TW.js -> jquery-ui-datepicker-$VERSION_zh_TW.js
#  OLD_LOCALE=`basename ${FILE} | sed "s|jquery.ui.datepicker-||g" | sed "s|.min.js||g"| sed "s|-|_|g"`
#  patchLocale
#  NAME=jquery-ui-${VERSION}.custom${NEW_LOCALE}.min.js
#  cp ${FILE} ${TARGET}/script/contrib/${NAME}
#done

# Styles

cp -r ${SOURCE}/css/ui-lightness ${TARGET}/style/contrib

# Check the svn diff now!
