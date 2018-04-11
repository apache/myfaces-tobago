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

QUnit.test("Accordion: Box 1: 'hide' to 'show' to 'hide'", function (assert) {
  assert.expect(9);
  var done = assert.async(2);
  var step = 1;

  var $box = jQueryFrame("#page\\:mainForm\\:accordionBox1");
  var $showBox = jQueryFrame("#page\\:mainForm\\:showBox1");
  var $hideBox = jQueryFrame("#page\\:mainForm\\:hideBox1");
  var $boxBody = $box.find(".card-body");

  assert.equal($showBox.length, 1);
  assert.equal($hideBox.length, 0);
  assert.equal($boxBody.text().trim().length, 0);
  $showBox.click();

  jQuery("#page\\:testframe").load(function () {
    if (step === 1) {
      $showBox = jQueryFrame($showBox.selector);
      $hideBox = jQueryFrame($hideBox.selector);
      $boxBody = jQueryFrame($boxBody.selector);

      assert.equal($showBox.length, 0);
      assert.equal($hideBox.length, 1);
      assert.notEqual($boxBody.text().trim().length, 0);

      $hideBox.click();

      step++;
      done();
    } else if (step === 2) {
      $showBox = jQueryFrame($showBox.selector);
      $hideBox = jQueryFrame($hideBox.selector);
      $boxBody = jQueryFrame($boxBody.selector);

      assert.equal($showBox.length, 1);
      assert.equal($hideBox.length, 0);
      assert.equal($boxBody.text().trim().length, 0);

      step++;
      done();
    }
  });
});

QUnit.test("Accordion: Box 2: 'hide' to 'show' to 'hide'", function (assert) {
  assert.expect(9);
  var done = assert.async(2);
  var step = 1;

  var $box = jQueryFrame("#page\\:mainForm\\:accordionBox2");
  var $showBox = jQueryFrame("#page\\:mainForm\\:showBox2");
  var $hideBox = jQueryFrame("#page\\:mainForm\\:hideBox2");
  var $boxBody = $box.find(".card-body");

  assert.equal($showBox.length, 1);
  assert.equal($hideBox.length, 0);
  assert.equal($boxBody.text().trim().length, 0);
  $showBox.click();

  waitForAjax(function () {
    $showBox = jQueryFrame($showBox.selector);
    $hideBox = jQueryFrame($hideBox.selector);
    $boxBody = jQueryFrame($boxBody.selector);

    return step === 1
        && $showBox.length === 0
        && $hideBox.length === 1
        && $boxBody.text().trim().length !== 0;
  }, function () {
    $showBox = jQueryFrame($showBox.selector);
    $hideBox = jQueryFrame($hideBox.selector);
    $boxBody = jQueryFrame($boxBody.selector);

    assert.equal($showBox.length, 0);
    assert.equal($hideBox.length, 1);
    assert.notEqual($boxBody.text().trim().length, 0);

    $hideBox.click();

    waitForAjax(function () {
      $showBox = jQueryFrame($showBox.selector);
      $hideBox = jQueryFrame($hideBox.selector);
      $boxBody = jQueryFrame($boxBody.selector);

      return step === 2
          && $showBox.length === 1
          && $hideBox.length === 0
          && $boxBody.text().trim().length === 0;
    }, function () {
      $showBox = jQueryFrame($showBox.selector);
      $hideBox = jQueryFrame($hideBox.selector);
      $boxBody = jQueryFrame($boxBody.selector);

      assert.equal($showBox.length, 1);
      assert.equal($hideBox.length, 0);
      assert.equal($boxBody.text().trim().length, 0);

      step++;
      done();
    });
    step++;
    done();
  });
});

QUnit.test("Accordion: Box 3: 'hide' to 'show' to 'hide'", function (assert) {
  assert.expect(9);
  var done = assert.async(2);
  var step = 1;

  var $box = jQueryFrame("#page\\:mainForm\\:accordionBox3");
  var $showBox = jQueryFrame("#page\\:mainForm\\:showBox3");
  var $hideBox = jQueryFrame("#page\\:mainForm\\:hideBox3");
  var $boxBody = $box.find(".card-body");

  assert.ok($box.hasClass("tobago-collapsed"));
  assert.equal($showBox.length, 1);
  assert.equal($hideBox.length, 0);
  $showBox.click();

  waitForAjax(function () {
    $box = jQueryFrame($box.selector);
    $showBox = jQueryFrame($showBox.selector);
    $hideBox = jQueryFrame($hideBox.selector);

    return step === 1
        && !$box.hasClass("tobago-collapsed")
        && $showBox.length === 0
        && $hideBox.length === 1;
  }, function () {
    $box = jQueryFrame($box.selector);
    $showBox = jQueryFrame($showBox.selector);
    $hideBox = jQueryFrame($hideBox.selector);

    assert.notOk($box.hasClass("tobago-collapsed"));
    assert.equal($showBox.length, 0);
    assert.equal($hideBox.length, 1);

    $hideBox.click();

    waitForAjax(function () {
      $box = jQueryFrame($box.selector);
      $showBox = jQueryFrame($showBox.selector);
      $hideBox = jQueryFrame($hideBox.selector);

      return step === 2
          && $box.hasClass("tobago-collapsed")
          && $showBox.length === 1
          && $hideBox.length === 0;
    }, function () {
      $box = jQueryFrame($box.selector);
      $showBox = jQueryFrame($showBox.selector);
      $hideBox = jQueryFrame($hideBox.selector);

      assert.ok($box.hasClass("tobago-collapsed"));
      assert.equal($showBox.length, 1);
      assert.equal($hideBox.length, 0);

      step++;
      done();
    });
    step++;
    done();
  });
});
