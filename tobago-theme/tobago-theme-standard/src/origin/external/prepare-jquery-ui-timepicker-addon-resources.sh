#! /bin/bash

# Please call this script in its directory

# Click "Download" on this site https://github.com/trentrichardson/jQuery-Timepicker-Addon/archive/v1.4.5.zip
# Unpack the ZIP in a directory and set it here:
SOURCE=~/Downloads/jQuery-Timepicker-Addon-1.4.5
TARGET=../../main/resources/org/apache/myfaces/tobago/renderkit/html/standard/standard
VERSION=1.4.5

# Scripts

cp ${SOURCE}/dist/jquery-ui-timepicker-addon.js ${TARGET}/script/contrib/jquery-ui-timepicker-addon-${VERSION}.js

for FILE in $(find ${SOURCE}/dist/i18n -type file -name "jquery-ui-timepicker-*.js") ; do
  # echo ${FILE};
  # e.g. jquery-ui-timepicker-zh-TW.js -> jquery-ui-timepicker-$VERSION_zh_TW.js
  LOCALE=`basename ${FILE} | sed "s|jquery-ui-timepicker-||g" | sed "s|.js||g"| sed "s|-|_|g"`

  if [[ "${LOCALE}" == "addon_i18n" || "${LOCALE}" == "addon_i18n.min" ]]; then
    # echo "ignoring '${LOCALE}', because we need not the summerized version"
    continue;
  fi

  NAME=jquery-ui-timepicker-i18n-${VERSION}_${LOCALE}.js
  cp ${FILE} ${TARGET}/script/contrib/${NAME}
done

# Styles

cp ${SOURCE}/dist/jquery-ui-timepicker-addon.css ${TARGET}/style/contrib/jquery-ui-timepicker-addon-${VERSION}.css

# Check the svn diff now!
