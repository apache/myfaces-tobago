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

QUnit.test("submit: Addition (2 + 4)", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $number1 = jQueryFrame("#page\\:mainForm\\:selectNum1 input");
  var $number2 = jQueryFrame("#page\\:mainForm\\:selectNum2 input");
  var $submitAdd = jQueryFrame("#page\\:mainForm\\:submitAdd");

  $number1.eq(0).removeAttr("checked");
  $number1.eq(1).attr("checked", "checked"); // Select 2
  $number1.eq(2).removeAttr("checked");
  $number2.eq(0).removeAttr("checked");
  $number2.eq(1).removeAttr("checked");
  $number2.eq(2).attr("checked", "checked"); // Select 4
  $submitAdd.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:resultOutput span");
    assert.equal($output.text(), "6");
    done();
  });
});

QUnit.test("submit: Subtraction (4 - 1)", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $number1 = jQueryFrame("#page\\:mainForm\\:selectNum1 input");
  var $number2 = jQueryFrame("#page\\:mainForm\\:selectNum2 input");
  var $submitSub = jQueryFrame("#page\\:mainForm\\:submitSub");

  $number1.eq(0).removeAttr("checked");
  $number1.eq(1).removeAttr("checked");
  $number1.eq(2).attr("checked", "checked"); // Select 4
  $number2.eq(0).attr("checked", "checked"); // Select 1
  $number2.eq(1).removeAttr("checked");
  $number2.eq(2).removeAttr("checked");
  $submitSub.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:resultOutput span");
    assert.equal($output.text(), "3");
    done();
  });
});

QUnit.test("ajax: select Mars", function (assert) {
  assert.expect(2);
  var done = assert.async();
  var $planet = jQueryFrame("#page\\:mainForm\\:selectPlanet input");

  $planet.eq(0).removeAttr("checked");
  $planet.eq(2).removeAttr("checked");
  $planet.eq(1).attr("checked", "checked").trigger("change"); // Mars.

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/30-selectOneRadio/selectOneRadio.xhtml'
  }).done(function () {
    var $moons = jQueryFrame("#page\\:mainForm\\:moonradio li label");
    assert.equal($moons.eq(0).text(), "Phobos");
    assert.equal($moons.eq(1).text(), "Deimos");
    done();
  });
});

QUnit.test("ajax: select Jupiter", function (assert) {
  assert.expect(4);
  var done = assert.async();
  var $planet = jQueryFrame("#page\\:mainForm\\:selectPlanet input");

  $planet.eq(0).removeAttr("checked");
  $planet.eq(1).removeAttr("checked");
  $planet.eq(2).attr("checked", "checked").trigger("change"); // Jupiter.

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/30-selectOneRadio/selectOneRadio.xhtml'
  }).done(function () {
    var $moons = jQueryFrame("#page\\:mainForm\\:moonradio li label");
    assert.equal($moons.eq(0).text(), "Europa");
    assert.equal($moons.eq(1).text(), "Ganymed");
    assert.equal($moons.eq(2).text(), "Io");
    assert.equal($moons.eq(3).text(), "Kallisto");
    done();
  });
});
