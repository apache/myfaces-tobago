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
  var inputString = "M";
  var expectedLength = 10;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Ma'", function (assert) {
  var inputString = "Ma";
  var expectedLength = 4;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Mar'", function (assert) {
  var inputString = "Mar";
  var expectedLength = 1;

  testMarsBasics(assert, inputString, expectedLength);
});

QUnit.test("Basics: 'Mars'", function (assert) {
  var inputString = "Mars";
  var expectedLength = 1;

  testMarsBasics(assert, inputString, expectedLength);
});

function testMarsBasics(assert, inputString, expectedLength) {
  var $in = jQueryFrameFn("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  var TTT = new TobagoTestTools(assert);
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

QUnit.test("Basics: Add 'eus' and click first entry.", function (assert) {
  var $in = jQueryFrameFn("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("eus").trigger('input');
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal($in().val(), "eus");
    assert.equal($suggestions().length, 3);
    assert.equal($suggestions().eq(0).find("strong").text(), "eus");
    assert.equal($suggestions().eq(1).find("strong").text(), "eus");
    assert.equal($suggestions().eq(2).find("strong").text(), "eus");
  });
  TTT.action(function () {
    $suggestions().eq(0).click();
  });
  TTT.asserts(1, function () {
    assert.equal($in().val(), "Prometheus");

  });
  TTT.startTest();
});

QUnit.test("Advanced: 'C'", function (assert) {
  var $in = jQueryFrameFn("#page\\:mainForm\\:inAdvanced\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inAdvanced");
  var suggestionDelay = 2000;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("C").trigger('input');
  });
  TTT.waitMs(suggestionDelay);
  TTT.asserts(1, function () {
    assert.equal($suggestions().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Advanced: 'Ca'", function (assert) {
  var $in = jQueryFrameFn("#page\\:mainForm\\:inAdvanced\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inAdvanced");
  var suggestionDelay = 2000;
  var startTime = Date.now();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("Ca").trigger('input');
  });
  TTT.waitMs(200); // default suggestion delay
  TTT.asserts(1, function () {
    // Nothing happen, because the delay is greater than the default delay.
    assert.equal($suggestions().length, 0);
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($suggestions().length, 2);
    assert.equal($suggestions().eq(0).find("strong").text(), "Ca");
    assert.equal($suggestions().eq(1).find("strong").text(), "Ca");
    assert.ok(Date.now() - startTime >= suggestionDelay, "Delay for suggest popup must be greater/equal "
        + suggestionDelay);
  });
  TTT.startTest();
});

QUnit.test("Client side: 'Ju'", function (assert) {
  var $in = jQueryFrameFn("#page\\:mainForm\\:inClient\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inClient");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("Ju").trigger('input');
  });
  TTT.asserts(3, function () {
    assert.equal($suggestions().length, 2);
    assert.equal($suggestions().eq(0).find("strong").text(), "Ju");
    assert.equal($suggestions().eq(1).find("strong").text(), "Ju");
  });
  TTT.startTest();
});

function getSuggestions(id) {
  return jQueryFrameFn(Tobago.Utils.escapeClientId(
      jQueryFrame(id + " .tobago-suggest").attr("id") + "::popup") + " .tt-suggestion");
}
