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

QUnit.test("test patterns", function (assert) {

  assert.ok(true, "work in progress");
  /*
  assert.ok(false);
  TestDateTime.testDate();
  TestDateTime.testTime();
  TestDateTime.testBoth();
  */
});

TestDateTime = {};

TestDateTime.testDate = function () {

  let date;
  let format = "dd.mm.yy";
  let initial = "13.05.2014";
  let result;

  date = moment(initial, format);
  result = moment(date).format(format);

  if (result !== initial) {
    console.error("Error!");
  } else {
    console.info("Okay!");
  }

};

TestDateTime.testTime = function () {

  let date;
  let format = "HH:mm:ss";
  let initial = "14:06:55";
  let result;

  date = moment(initial, format);
  result = moment(date).format(format);

  if (result !== initial) {
    console.error("Error!");
  } else {
    console.info("Okay!");
  }

};

TestDateTime.testBoth = function () {

  let date, dateT;
  let formatD = "dd.mm.yy";
  let formatT = "HH:mm:ss";
  let initial = "13.05.2014 13:05:55";
  let result;
  let separator = " ";

  date = moment(initial, formatD + ' ' +  formatT);
  dateT = {
    hour: date.getHours(),
    minute: date.getMinutes(),
    second: date.getSeconds(),
    millisec: date.getMilliseconds(),
    microsec: date.getMicroseconds()
  };
  result = moment(date).format(formatD) + separator + jQuery.datepicker.formatTime(formatT, dateT);

  if (result !== initial) {
    console.error("Error!");
  } else {
    console.info("Okay!");
  }

};
