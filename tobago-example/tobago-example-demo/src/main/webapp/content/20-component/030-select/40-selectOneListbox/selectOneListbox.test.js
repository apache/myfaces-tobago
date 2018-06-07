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

QUnit.test("submit: select 'Nile'", function (assert) {
  let $rivers = jQueryFrameFn("#page\\:mainForm\\:riverList option");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:riverSubmit");
  let $output = jQueryFrameFn("#page\\:mainForm\\:riverOutput span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $rivers().eq(0).prop("selected", true); // Nile
    $rivers().eq(1).prop("selected", false); // Amazon
    $rivers().eq(2).prop("selected", false); // Yangtze
    $rivers().eq(3).prop("selected", false); // Yellow River
    $rivers().eq(4).prop("selected", false); // Paraná River
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "6853 km");
  });
  TTT.startTest();
});

QUnit.test("submit: select 'Yangtze'", function (assert) {
  let $rivers = jQueryFrameFn("#page\\:mainForm\\:riverList option");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:riverSubmit");
  let $output = jQueryFrameFn("#page\\:mainForm\\:riverOutput span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $rivers().eq(0).prop("selected", false); // Nile
    $rivers().eq(1).prop("selected", false); // Amazon
    $rivers().eq(2).prop("selected", true); // Yangtze
    $rivers().eq(3).prop("selected", false); // Yellow River
    $rivers().eq(4).prop("selected", false); // Paraná River
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "6300 km");

  });
  TTT.startTest();
});

QUnit.test("ajax: select Everest", function (assert) {
  let $mountains = jQueryFrameFn("#page\\:mainForm\\:mountainList option");
  let $output = jQueryFrameFn("#page\\:mainForm\\:selectedMountain span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $mountains().eq(1).prop("selected", false);
    $mountains().eq(2).prop("selected", false);
    $mountains().eq(3).prop("selected", false);
    $mountains().eq(4).prop("selected", false);
    $mountains().eq(0).prop("selected", true).trigger("change"); // Everest
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "8848 m");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Makalu", function (assert) {
  let $mountains = jQueryFrameFn("#page\\:mainForm\\:mountainList option");
  let $output = jQueryFrameFn("#page\\:mainForm\\:selectedMountain span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $mountains().eq(0).prop("selected", false);
    $mountains().eq(1).prop("selected", false);
    $mountains().eq(2).prop("selected", false);
    $mountains().eq(3).prop("selected", false);
    $mountains().eq(4).prop("selected", true).trigger("change"); // Everest
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "8481 m");
  });
  TTT.startTest();
});
