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

QUnit.test("Deprecated: 'Chile'", function (assert) {
  var inputString = "Chile";
  var expectedLength = 7;

  assert.expect(expectedLength + 1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:deprecated\\:\\:field");
  var s = "#page\\:mainForm\\:deprecated";
  var $suggestions = getSuggestions(s);

  $in.val(inputString).trigger('input');

  waitForAjax(function () {
    $suggestions = getSuggestions(s);
    return $suggestions.length === expectedLength;
  }, function () {
    $suggestions = getSuggestions(s);

    assert.equal($suggestions.length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions.eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }

    done();
  });
});

QUnit.test("Replacement: 'Chile'", function (assert) {
  var inputString = "Chile";
  var expectedLength = 7;

  assert.expect(expectedLength + 1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:replacement\\:\\:field");
  var s = "#page\\:mainForm\\:replacement";
  var $suggestions = getSuggestions(s);

  $in.val(inputString).trigger('input');

  waitForAjax(function () {
    $suggestions = getSuggestions(s);
    return $suggestions.length === expectedLength;
  }, function () {
    $suggestions = getSuggestions(s);

    assert.equal($suggestions.length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions.eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }

    done();
  });
});

function getSuggestions(id) {
  return jQueryFrame(Tobago.Utils.escapeClientId(
          jQueryFrame(id + " .tobago-suggest").attr("id") + "::popup") + " .tt-suggestion");
}
