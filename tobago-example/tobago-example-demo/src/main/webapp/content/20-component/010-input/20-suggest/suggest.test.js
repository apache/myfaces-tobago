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

  assert.expect(expectedLength + 1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  $in.val(inputString).trigger('input');

  waitForAjax(function () {
    $suggestions = jQueryFrame($suggestions.selector);
    return $suggestions.length === expectedLength;
  }, function () {
    $suggestions = jQueryFrame($suggestions.selector);

    assert.equal($suggestions.length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions.eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }

    done();
  });
});


QUnit.test("Basics: 'Ma'", function (assert) {
  var inputString = "Ma";
  var expectedLength = 4;

  assert.expect(expectedLength + 1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  $in.val(inputString).trigger('input');

  waitForAjax(function () {
    $suggestions = jQueryFrame($suggestions.selector);
    return $suggestions.length === expectedLength;
  }, function () {
    $suggestions = jQueryFrame($suggestions.selector);

    assert.equal($suggestions.length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions.eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }

    done();
  });
});

QUnit.test("Basics: 'Mar'", function (assert) {
  var inputString = "Mar";
  var expectedLength = 1;

  assert.expect(expectedLength + 1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  $in.val(inputString).trigger('input');

  waitForAjax(function () {
    $suggestions = jQueryFrame($suggestions.selector);
    return $suggestions.length === expectedLength;
  }, function () {
    $suggestions = jQueryFrame($suggestions.selector);

    assert.equal($suggestions.length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions.eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }

    done();
  });
});

QUnit.test("Basics: 'Mars'", function (assert) {
  var inputString = "Mars";
  var expectedLength = 1;

  assert.expect(expectedLength + 1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  $in.val(inputString).trigger('input');

  waitForAjax(function () {
    $suggestions = jQueryFrame($suggestions.selector);
    return $suggestions.length === expectedLength;
  }, function () {
    $suggestions = jQueryFrame($suggestions.selector);

    assert.equal($suggestions.length, expectedLength);
    for (i = 0; i < expectedLength; i++) {
      assert.ok($suggestions.eq(i).find("strong").text().toUpperCase().indexOf(inputString.toUpperCase()) >= 0);
    }

    done();
  });
});

QUnit.test("Basics: Add 'eus' and click first entry.", function (assert) {
  assert.expect(7);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:inBasic\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inBasic");

  $in.val("eus").trigger('input');
  assert.equal($in.val(), "eus");

  waitForAjax(function () {
    $suggestions = jQueryFrame($suggestions.selector);
    return $suggestions.length === 3;
  }, function () {
    $in = jQueryFrame($in.selector);
    $suggestions = jQueryFrame($suggestions.selector);

    assert.equal($suggestions.length, 3);
    assert.equal($suggestions.eq(0).find("strong").text(), "eus");
    assert.equal($suggestions.eq(1).find("strong").text(), "eus");
    assert.equal($suggestions.eq(2).find("strong").text(), "eus");

    // click first entry
    assert.equal($in.val(), "eus");
    $suggestions.eq(0).click();
    assert.equal($in.val(), "Prometheus");

    done();
  });
});

QUnit.test("Advanced: 'C'", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $in = jQueryFrame("#page\\:mainForm\\:inAdvanced\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inAdvanced");
  var suggestionDelay = 2000;

  $in.val("C").trigger('input');

  setTimeout(function () {
    waitForAjax(function () {
      $suggestions = jQueryFrame($suggestions.selector);
      return $suggestions.length === 0;
    }, function () {
      $suggestions = jQueryFrame($suggestions.selector);
      assert.equal($suggestions.length, 0);
      done();
    });
  }, suggestionDelay);
});

QUnit.test("Advanced: 'Ca'", function (assert) {
  assert.expect(4);
  var done = assert.async(2);

  var $in = jQueryFrame("#page\\:mainForm\\:inAdvanced\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inAdvanced");
  var suggestionDelay = 2000;

  $in.val("Ca").trigger('input');

  setTimeout(function () {
    $suggestions = jQueryFrame($suggestions.selector);

    // Nothing happen, because the delay is greater than the default delay.
    assert.equal($suggestions.length, 0);

    done();
  }, 200); // default suggestion delay

  setTimeout(function () {
    waitForAjax(function () {
      $in = jQueryFrame($in.selector);
      $suggestions = jQueryFrame($suggestions.selector);

      return $suggestions.length === 2
          && $suggestions.eq(0).find("strong").text() === "Ca"
          && $suggestions.eq(1).find("strong").text() === "Ca";
    }, function () {
      $in = jQueryFrame($in.selector);
      $suggestions = jQueryFrame($suggestions.selector);

      assert.equal($suggestions.length, 2);
      assert.equal($suggestions.eq(0).find("strong").text(), "Ca");
      assert.equal($suggestions.eq(1).find("strong").text(), "Ca");

      done();
    });
  }, suggestionDelay);
});

QUnit.test("Client side: 'Ju'", function (assert) {
  assert.expect(3);

  var $in = jQueryFrame("#page\\:mainForm\\:inClient\\:\\:field");
  var $suggestions = getSuggestions("#page\\:mainForm\\:inClient");

  $in.val("Ju").trigger('input');

  $suggestions = jQueryFrame($suggestions.selector);
  assert.equal($suggestions.length, 2);
  assert.equal($suggestions.eq(0).find("strong").text(), "Ju");
  assert.equal($suggestions.eq(1).find("strong").text(), "Ju");
});

function getSuggestions(id) {
  return jQueryFrame(Tobago.Utils.escapeClientId(
      jQueryFrame(id + " .tobago-suggest").attr("id") + "::popup") + " .tt-suggestion");
}
