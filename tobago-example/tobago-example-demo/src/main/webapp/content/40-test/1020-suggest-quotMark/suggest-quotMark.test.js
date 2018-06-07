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

QUnit.test("Basics: 'M'", function (assert) {
  let inputString = "M";
  let expectedLength = 3;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Ma'", function (assert) {
  let inputString = "Ma";
  let expectedLength = 2;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Mar'", function (assert) {
  let inputString = "Mar";
  let expectedLength = 2;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Mars'", function (assert) {
  let inputString = "Mars";
  let expectedLength = 1;

  testMarsBasics(assert, inputString, expectedLength);
});

function testMarsBasics(assert, inputString, expectedLength) {
  let $in = jQueryFrameFn("#page\\:mainForm\\:input\\:\\:field");
  let $suggestions = getSuggestions("#page\\:mainForm\\:input");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val(inputString).trigger('input');
  });
  TTT.waitForResponse();
  TTT.asserts(expectedLength + 1, function () {
    assert.equal($suggestions().length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions().eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }
  });
  TTT.startTest();
}

function getSuggestions(id) {
  return jQueryFrameFn(Tobago.Utils.escapeClientId(
      jQueryFrame(id + " .tobago-suggest").attr("id") + "::popup") + " .tt-suggestion");
}
