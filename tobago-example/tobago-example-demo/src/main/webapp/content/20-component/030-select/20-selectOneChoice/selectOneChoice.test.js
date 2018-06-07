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
  let $alice = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  let $bob = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:submit");
  let $output = jQueryFrameFn("#page\\:mainForm\\:outputPerson span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $alice().prop("selected", true);
    $bob().prop("selected", false);
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Alice Anderson");

  });
  TTT.startTest();
});

QUnit.test("submit: Bob", function (assert) {
  let $alice = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Alice')");
  let $bob = jQueryFrameFn("#page\\:mainForm\\:selectPerson\\:\\:field option:contains('Bob')");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:submit");
  let $output = jQueryFrameFn("#page\\:mainForm\\:outputPerson span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $alice().prop("selected", false);
    $bob().prop("selected", true);
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Bob Brunch");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Mars", function (assert) {
  let $mars = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Mars')");
  let $jupiter = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Jupiter')");
  let $moons = jQueryFrameFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $jupiter().prop("selected", false);
    $mars().prop("selected", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($moons().eq(0).text().trim(), "Phobos");
    assert.equal($moons().eq(1).text().trim(), "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  let $mars = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Mars')");
  let $jupiter = jQueryFrameFn("#page\\:mainForm\\:selectPlanet\\:\\:field option:contains('Jupiter')");
  let $moons = jQueryFrameFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $mars().prop("selected", false);
    $jupiter().prop("selected", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($moons().eq(0).text().trim(), "Europa");
    assert.equal($moons().eq(1).text().trim(), "Ganymed");
    assert.equal($moons().eq(2).text().trim(), "Io");
    assert.equal($moons().eq(3).text().trim(), "Kallisto");
  });
  TTT.startTest();
});
