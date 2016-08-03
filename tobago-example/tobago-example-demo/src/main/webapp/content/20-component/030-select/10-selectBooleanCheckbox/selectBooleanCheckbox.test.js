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

QUnit.test("submit: select A", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectA = jQueryFrame("#page\\:mainForm\\:selectA input");
  var $selectB = jQueryFrame("#page\\:mainForm\\:selectB input");
  var $selectC = jQueryFrame("#page\\:mainForm\\:selectC input");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $selectA.prop("checked", true);
  $selectB.prop("checked", false);
  $selectC.prop("checked", false);
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:submitOutput span");
    assert.equal($output.text(), "A ");
    done();
  });
});

QUnit.test("submit: select B and C", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectA = jQueryFrame("#page\\:mainForm\\:selectA input");
  var $selectB = jQueryFrame("#page\\:mainForm\\:selectB input");
  var $selectC = jQueryFrame("#page\\:mainForm\\:selectC input");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $selectA.prop("checked", false);
  $selectB.prop("checked", true);
  $selectC.prop("checked", true);
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:submitOutput span");
    assert.equal($output.text(), "B C ");
    done();
  });
});

QUnit.test("ajax: select D", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectD = jQueryFrame("#page\\:mainForm\\:selectD input");
  var $outputD = jQueryFrame("#page\\:mainForm\\:outputD span");

  $selectD.prop("checked", true).trigger("change");

  waitForAjax(function () {
    $outputD = jQueryFrame($outputD.selector);
    return $outputD.text() == "true";
  }, function () {
    $outputD = jQueryFrame($outputD.selector);
    assert.equal($outputD.text(), "true");
    done();
  });
});

QUnit.test("ajax: deselect D", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectD = jQueryFrame("#page\\:mainForm\\:selectD input");
  var $outputD = jQueryFrame("#page\\:mainForm\\:outputD span");

  $selectD.prop("checked", false).trigger("change");

  waitForAjax(function () {
    $outputD = jQueryFrame($outputD.selector);
    return $outputD.text() == "false";
  }, function () {
    $outputD = jQueryFrame($outputD.selector);
    assert.equal($outputD.text(), "false");
    done();
  });
});

QUnit.test("ajax: select E", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectE = jQueryFrame("#page\\:mainForm\\:selectE input");
  var $outputE = jQueryFrame("#page\\:mainForm\\:outputE span");

  $selectE.prop("checked", true).trigger("change");

  waitForAjax(function () {
    $outputE = jQueryFrame($outputE.selector);
    return $outputE.text() == "true";
  }, function () {
    $outputE = jQueryFrame($outputE.selector);
    assert.equal($outputE.text(), "true");
    done();
  });
});

QUnit.test("ajax: deselect E", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectE = jQueryFrame("#page\\:mainForm\\:selectE input");
  var $outputE = jQueryFrame("#page\\:mainForm\\:outputE span");

  $selectE.prop("checked", false).trigger("change");

  waitForAjax(function () {
    $outputE = jQueryFrame($outputE.selector);
    return $outputE.text() == "false";
  }, function () {
    $outputE = jQueryFrame($outputE.selector);
    assert.equal($outputE.text(), "false");
    done();
  });
});

QUnit.test("ajax: select F", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectF = jQueryFrame("#page\\:mainForm\\:selectF input");
  var $outputF = jQueryFrame("#page\\:mainForm\\:outputF span");

  $selectF.prop("checked", true).trigger("change");

  waitForAjax(function () {
    $outputF = jQueryFrame($outputF.selector);
    return $outputF.text() == "true";
  }, function () {
    $outputF = jQueryFrame($outputF.selector);
    assert.equal($outputF.text(), "true");
    done();
  });
});

QUnit.test("ajax: deselect F", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectF = jQueryFrame("#page\\:mainForm\\:selectF input");
  var $outputF = jQueryFrame("#page\\:mainForm\\:outputF span");

  $selectF.prop("checked", false).trigger("change");

  waitForAjax(function () {
    $outputF = jQueryFrame($outputF.selector);
    return $outputF.text() == "false";
  }, function () {
    $outputF = jQueryFrame($outputF.selector);
    assert.equal($outputF.text(), "false");
    done();
  });
});
