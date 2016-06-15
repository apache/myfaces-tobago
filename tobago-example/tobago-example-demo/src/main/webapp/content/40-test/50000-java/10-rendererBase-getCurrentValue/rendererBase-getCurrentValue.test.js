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

function jQueryFrame(expression) {
  return document.getElementById("page:testframe").contentWindow.jQuery(expression);
}

function test(assert, idSuffix, expectedText) {
  var $out = jQueryFrame("#page\\:" + idSuffix);
  assert.equal($out.text().trim(), expectedText);
}

QUnit.test("formatted values: out string", function (assert) {
  test(assert, "outString", "simple string");
});

QUnit.test("formatted values: out string from method", function (assert) {
  test(assert, "outStringFromMethod", "HELLO WORLD!");
});

QUnit.test("formatted values: out date", function (assert) {
  test(assert, "outDate", "24.07.1969");
});

QUnit.test("formatted values: out date from method", function (assert) {
  test(assert, "outDateFromMethod", "24.07.2019");
});

QUnit.test("formatted values: out currency", function (assert) {
  test(assert, "outCurrency", "TTD");
});

QUnit.test("formatted values: out currency from method", function (assert) {
  test(assert, "outCurrencyFromMethod", "ISK");
});
