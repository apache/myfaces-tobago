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

QUnit.test("Deprecated: 'Ma'", function (assert) {
  let inputString = "Ma";
  let expectedLength = 4;

  let inFn = jQueryFrameFn("#page\\:mainForm\\:deprecated\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:deprecated");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val(inputString).trigger('input');
  });
  TTT.waitForResponse();
  TTT.asserts(expectedLength + 1, function () {
    assert.equal(suggestionsFn().length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok(suggestionsFn().eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }
  });
  TTT.startTest();
});

QUnit.test("Replacement: 'Ma'", function (assert) {
  let inputString = "Ma";
  let expectedLength = 4;

  let inFn = jQueryFrameFn("#page\\:mainForm\\:replacement\\:\\:field");
  let suggestionsFn = getSuggestions("#page\\:mainForm\\:replacement");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val(inputString).trigger('input');
  });
  TTT.waitForResponse();
  TTT.asserts(expectedLength + 1, function () {
    assert.equal(suggestionsFn().length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok(suggestionsFn().eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }
  });
  TTT.startTest();
});

function getSuggestions(id) {
  return jQueryFrameFn(Tobago.Utils.escapeClientId(
      jQueryFrame(id + " .tobago-suggest").attr("id") + "::popup") + " .tt-suggestion");
}
