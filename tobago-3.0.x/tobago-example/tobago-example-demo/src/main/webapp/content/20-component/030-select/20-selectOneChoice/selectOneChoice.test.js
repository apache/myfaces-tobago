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

QUnit.test("submit: Alice", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $alice = jQueryFrame("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  var $bob = jQueryFrame("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $alice.prop("selected", true);
  $bob.prop("selected", false);
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:outputPerson span");
    assert.equal($output.text(), "Alice Anderson");
    done();
  });
});

QUnit.test("submit: Bob", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $alice = jQueryFrame("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  var $bob = jQueryFrame("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  var $submit = jQueryFrame("#page\\:mainForm\\:submit");

  $alice.prop("selected", false);
  $bob.prop("selected", true);
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:outputPerson span");
    assert.equal($output.text(), "Bob Brunch");
    done();
  });
});

QUnit.test("ajax: select Mars", function (assert) {
  assert.expect(2);
  var done = assert.async();
  var $mars = jQueryFrame("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Mars')");
  var $jupiter = jQueryFrame("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Jupiter')");
  var $moons = jQueryFrame("#page\\:mainForm\\:moonbox\\:\\:field option");

  $jupiter.prop("selected", false);
  $mars.prop("selected", true).trigger("change");

  waitForAjax(function () {
    $moons = jQueryFrame($moons.selector);
    return $moons.eq(0).text() == "Phobos"
        && $moons.eq(1).text() == "Deimos";
  }, function () {
    $moons = jQueryFrame($moons.selector);
    assert.equal($moons.eq(0).text(), "Phobos");
    assert.equal($moons.eq(1).text(), "Deimos");
    done();
  });
});

QUnit.test("ajax: select Jupiter", function (assert) {
  assert.expect(4);
  var done = assert.async();
  var $mars = jQueryFrame("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Mars')");
  var $jupiter = jQueryFrame("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Jupiter')");
  var $moons = jQueryFrame("#page\\:mainForm\\:moonbox\\:\\:field option");

  $mars.prop("selected", false);
  $jupiter.prop("selected", true).trigger("change");

  waitForAjax(function () {
    $moons = jQueryFrame($moons.selector);
    return $moons.eq(0).text() == "Europa"
        && $moons.eq(1).text() == "Ganymed"
        && $moons.eq(2).text() == "Io"
        && $moons.eq(3).text() == "Kallisto";
  }, function () {
    $moons = jQueryFrame($moons.selector);
    assert.equal($moons.eq(0).text(), "Europa");
    assert.equal($moons.eq(1).text(), "Ganymed");
    assert.equal($moons.eq(2).text(), "Io");
    assert.equal($moons.eq(3).text(), "Kallisto");
    done();
  });
});
