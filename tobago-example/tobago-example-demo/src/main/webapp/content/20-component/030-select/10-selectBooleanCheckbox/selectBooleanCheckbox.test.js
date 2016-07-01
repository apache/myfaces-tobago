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

QUnit.test("submit: select A", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectA = jQueryFrame("#page\\:mainForm\\:selectA input");
  var $selectB = jQueryFrame("#page\\:mainForm\\:selectB input");
  var $selectC = jQueryFrame("#page\\:mainForm\\:selectC input");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $selectA.attr("checked", "checked");
  $selectB.removeAttr("checked");
  $selectC.removeAttr("checked");
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

  $selectA.removeAttr("checked");
  $selectB.attr("checked", "checked");
  $selectC.attr("checked", "checked");
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
  $selectD.attr("checked", "checked").trigger("change");

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml'
  }).done(function () {
    var $outputD = jQueryFrame("#page\\:mainForm\\:outputD span");
    assert.equal($outputD.text(), "true");
    done();
  });
});

QUnit.test("ajax: deselect D", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectD = jQueryFrame("#page\\:mainForm\\:selectD input");
  $selectD.removeAttr("checked").trigger("change");

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml'
  }).done(function () {
    var $outputD = jQueryFrame("#page\\:mainForm\\:outputD span");
    assert.equal($outputD.text(), "false");
    done();
  });
});

QUnit.test("ajax: select E", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectE = jQueryFrame("#page\\:mainForm\\:selectE input");
  $selectE.attr("checked", "checked").trigger("change");

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml'
  }).done(function () {
    var $outputE = jQueryFrame("#page\\:mainForm\\:outputE span");
    assert.equal($outputE.text(), "true");
    done();
  });
});

QUnit.test("ajax: deselect E", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectE = jQueryFrame("#page\\:mainForm\\:selectE input");
  $selectE.removeAttr("checked").trigger("change");

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml'
  }).done(function () {
    var $outputE = jQueryFrame("#page\\:mainForm\\:outputE span");
    assert.equal($outputE.text(), "false");
    done();
  });
});

QUnit.test("ajax: select F", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectF = jQueryFrame("#page\\:mainForm\\:selectF input");
  $selectF.attr("checked", "checked").trigger("change");

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml'
  }).done(function () {
    var $outputF = jQueryFrame("#page\\:mainForm\\:outputF span");
    assert.equal($outputF.text(), "true");
    done();
  });
});

QUnit.test("ajax: deselect F", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $selectF = jQueryFrame("#page\\:mainForm\\:selectF input");
  $selectF.removeAttr("checked").trigger("change");

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml'
  }).done(function () {
    var $outputF = jQueryFrame("#page\\:mainForm\\:outputF span");
    assert.equal($outputF.text(), "false");
    done();
  });
});
