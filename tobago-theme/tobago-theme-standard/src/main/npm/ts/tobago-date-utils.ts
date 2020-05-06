/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// XXX it might be nice, if this util was in tobago-date.ts, but in that case there are problems
// XXX with Jest (UnitTesting)

export class DateUtils {

  /*
  Get the pattern from the "Java world",
  see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html
  and convert it to 'vanillajs-datepicker', see https://mymth.github.io/vanillajs-datepicker/#/date-string+format
  Attention: Not every pattern char is supported.
  */
  public static convertPattern(originalPattern: string): string {
    let pattern;
    if (!originalPattern || originalPattern.length > 100) {
      console.warn("Pattern not supported: " + originalPattern);
      pattern = "";
    } else {
      pattern = originalPattern;
    }

    let analyzedPattern = "";
    let nextSegment = "";
    let escMode = false;
    for (let i = 0; i < pattern.length; i++) {
      const currentChar = pattern.charAt(i);
      if (currentChar == "'" && escMode == false) {
        escMode = true;
        analyzedPattern += DateUtils.convertPatternPart(nextSegment);
        nextSegment = "";
      } else if (currentChar == "'" && pattern.charAt(i + 1) == "'") {
        if (escMode) {
          nextSegment += "\\";
        }
        nextSegment += "'";
        i++;
      } else if (currentChar == "'" && escMode == true) {
        escMode = false;
        analyzedPattern += nextSegment;
        nextSegment = "";
      } else {
        if (escMode) {
          nextSegment += "\\";
        }
        nextSegment += currentChar;
      }
    }
    if (nextSegment != "") {
      if (escMode) {
        analyzedPattern += nextSegment;
      } else {
        analyzedPattern += this.convertPatternPart(nextSegment);
      }
    }

    return analyzedPattern;
  }

  static convertPatternPart(originalPattern: string): string {

    let pattern = originalPattern;

    if (pattern.search("G") > -1 || pattern.search("W") > -1 || pattern.search("F") > -1
        || pattern.search("K") > -1 || pattern.search("z") > -1 || pattern.search("X") > -1) {
      console.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern);
      pattern = "";
    }

    if (pattern.search("y") > -1) {
      pattern = pattern.replace(/y/g, "y");
    }

    if (pattern.search("M") > -1) {
      pattern = pattern.replace(/M/g, "m");
    }

    if (pattern.search("d") > -1) {
      pattern = pattern.replace(/dd+/g, "dd");
      pattern = pattern.replace(/\bd\b/g, "d");
    }

    return pattern;
  }
}
