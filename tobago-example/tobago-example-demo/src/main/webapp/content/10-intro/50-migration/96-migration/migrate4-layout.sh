#! /bin/bash

# Script to replace the layout attribute syntax from Tobago 3.0 to 4.0
# (not very fast and might be optimized, but need only to run one time per project)
# usage: run this file "migrate-layout.sh" in the project parent directory.
# it will process all files in all subfolder with suffix .xhtml

function replace_segment_one {
 file=$1
 sed -i -E "s/(extraSmall|small|medium|large|extraLarge)=\"([^\"^ ]*)([0-9]+);([0-9]+)/\1=\"\2\3seg \4/g" $file
}

function replace_segment_last {
 file=$1
 sed -i -E "s/(extraSmall|small|medium|large|extraLarge)=\"([^\"]*)([0-9]+)\"/\1=\"\2\3seg\"/g" $file
}

function replace_columns_rows {
 file=$1
 sed -i -E "s/(columns|rows)=\"([^\"]*)([0-9]+)\*/\1=\"\2\3fr/g" $file
}

function replace_columns_rows_one {
 file=$1
 sed -i -E "s/(columns|rows)=\"([^\"]*)\*/\1=\"\21fr/g" $file
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

# only on Windows:
#   unix2dos -q $file
done
