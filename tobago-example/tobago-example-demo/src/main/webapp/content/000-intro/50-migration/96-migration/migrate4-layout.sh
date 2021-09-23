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

# Script to replace the layout attribute syntax from Tobago 3.0 to 4.0
# (not very fast and might be optimized, but need only to run one time per project)
# usage: run this file "migrate-layout.sh" in the project parent directory.
# it will process all files in all subfolder with suffix .xhtml

# The sed command syntax works with macOS.
# For Windows (MinGW) or Linux you'll need to remove the "" behind all sed -i commands.
# For Windows (MinGW) you may need to call unix2dos at the end.

function replace_segment_one {
  sed -i "" -E "s/(extraSmall|small|medium|large|extraLarge)=\"([^\"^ ]*)([0-9]+);([0-9]+)/\1=\"\2\3seg \4/g" $1
}

function replace_segment_last {
 sed -i "" -E "s/(extraSmall|small|medium|large|extraLarge)=\"([^\"]*)([0-9]+)\"/\1=\"\2\3seg\"/g" $1
}

function replace_columns_rows {
 sed -i "" -E "s/(columns|rows)=\"([^\"]*)([0-9]+)\*/\1=\"\2\3fr/g" $1
}

function replace_columns_rows_one {
 sed -i "" -E "s/(columns|rows)=\"([^\"]*)\*/\1=\"\21fr/g" $1
}

function replace_columns_rows_fixed {
 sed -i "" -E "s/(columns|rows)=\"([^\"]*)fixed/\1=\"\2auto/g" $1
}

function replace_columns_rows_semicolon {
 sed -i "" -E "s/(columns|rows)=\"([^\"]*);/\1=\"\2 /g" $1
}

find . -name "*.xhtml" | while read file; do

  echo "Processing file $file"

  for i in `seq 1 11`;
  do
    replace_segment_one $file
  done

  replace_segment_last $file

  for i in `seq 1 20`;
  do
    replace_columns_rows $file
  done

  for i in `seq 1 20`;
  do
    replace_columns_rows_one $file
  done

  for i in `seq 1 20`;
  do
    replace_columns_rows_fixed $file
  done

  for i in `seq 1 40`;
  do
    replace_columns_rows_semicolon $file
  done

  # Windows only
  # unix2dos -q $file

done
