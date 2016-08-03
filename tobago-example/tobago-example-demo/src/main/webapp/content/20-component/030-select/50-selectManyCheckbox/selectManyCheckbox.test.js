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

QUnit.test("submit: select cat", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $animals = jQueryFrame("#page\\:mainForm\\:animals input");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $animals.eq(0).prop("checked", true);
  $animals.eq(1).prop("checked", false);
  $animals.eq(2).prop("checked", false);
  $animals.eq(3).prop("checked", false);
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:animalsOutput span");
    assert.equal($output.text(), "Cat ");
    done();
  });
});

QUnit.test("submit: select fox and rabbit", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $animals = jQueryFrame("#page\\:mainForm\\:animals input");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $animals.eq(0).prop("checked", false);
  $animals.eq(1).prop("checked", false);
  $animals.eq(2).prop("checked", true);
  $animals.eq(3).prop("checked", true);
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:animalsOutput span");
    assert.equal($output.text(), "Fox Rabbit ");
    done();
  });
});

QUnit.test("ajax: click 'Two'", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $number2 = jQueryFrame("#page\\:mainForm\\:numbers\\:\\:1");
  var $output = jQueryFrame("#page\\:mainForm\\:resultOutput span");

  var newOutputValue;

  if ($number2.attr("checked") === "checked") {
    $number2.prop("checked", false).trigger("change");
    newOutputValue = parseInt($output.text()) - 2;
  } else {
    $number2.prop("checked", true).trigger("change");
    newOutputValue = parseInt($output.text()) + 2;
  }

  waitForAjax(function () {
    $output = jQueryFrame($output.selector);
    return $output.text() == newOutputValue;
  }, function () {
    $output = jQueryFrame($output.selector);
    assert.equal($output.text(), newOutputValue);
    done();
  });
});

QUnit.test("ajax: click 'Three'", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $number3 = jQueryFrame("#page\\:mainForm\\:numbers\\:\\:2");
  var $output = jQueryFrame("#page\\:mainForm\\:resultOutput span");

  var newOutputValue;

  if ($number3.prop("checked")) {
    $number3.prop("checked", false).trigger("change");
    newOutputValue = parseInt($output.text()) - 3;
  } else {
    $number3.prop("checked", true).trigger("change");
    newOutputValue = parseInt($output.text()) + 3;
  }

  waitForAjax(function () {
    $output = jQueryFrame($output.selector);
    return $output.text() == newOutputValue;
  }, function () {
    $output = jQueryFrame($output.selector);
    assert.equal($output.text(), newOutputValue);
    done();
  });
});

QUnit.test("ajax: click 'Two'", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $number2 = jQueryFrame("#page\\:mainForm\\:numbers\\:\\:1");
  var $output = jQueryFrame("#page\\:mainForm\\:resultOutput span");

  var newOutputValue;

  if ($number2.prop("checked")) {
    $number2.prop("checked", false).trigger("change");
    newOutputValue = parseInt($output.text()) - 2;
  } else {
    $number2.prop("checked", true).trigger("change");
    newOutputValue = parseInt($output.text()) + 2;
  }

  waitForAjax(function () {
    $output = jQueryFrame($output.selector);
    return $output.text() == newOutputValue;
  }, function () {
    $output = jQueryFrame($output.selector);
    assert.equal($output.text(), newOutputValue);
    done();
  });
});
