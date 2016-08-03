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
  assert.expect(1);
  var done = assert.async();
  var $rivers = jQueryFrame("#page\\:mainForm\\:riverList option");
  var $submit = jQueryFrame("#page\\:mainForm\\:riverSubmit");

  $rivers.eq(0).prop("selected", true); // Nile
  $rivers.eq(1).prop("selected", false); // Amazon
  $rivers.eq(2).prop("selected", false); // Yangtze
  $rivers.eq(3).prop("selected", false); // Yellow River
  $rivers.eq(4).prop("selected", false); // Paraná River
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:riverOutput span");
    assert.equal($output.text(), "6853 km");
    done();
  });
});

QUnit.test("submit: select 'Yangtze'", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $rivers = jQueryFrame("#page\\:mainForm\\:riverList option");
  var $submit = jQueryFrame("#page\\:mainForm\\:riverSubmit");

  $rivers.eq(0).prop("selected", false); // Nile
  $rivers.eq(1).prop("selected", false); // Amazon
  $rivers.eq(2).prop("selected", true); // Yangtze
  $rivers.eq(3).prop("selected", false); // Yellow River
  $rivers.eq(4).prop("selected", false); // Paraná River
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:riverOutput span");
    assert.equal($output.text(), "6300 km");
    done();
  });
});

QUnit.test("ajax: select Everest", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $mountains = jQueryFrame("#page\\:mainForm\\:mountainList option");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedMountain span");

  $mountains.eq(1).prop("selected", false);
  $mountains.eq(2).prop("selected", false);
  $mountains.eq(3).prop("selected", false);
  $mountains.eq(4).prop("selected", false);
  $mountains.eq(0).prop("selected", true).trigger("change"); // Everest

  waitForAjax(function () {
    $output = jQueryFrame($output.selector);
    return $output.text() == "8848 m";
  }, function () {
    $output = jQueryFrame($output.selector);
    assert.equal($output.text(), "8848 m");
    done();
  });
});

QUnit.test("ajax: select Makalu", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $mountains = jQueryFrame("#page\\:mainForm\\:mountainList option");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedMountain span");

  $mountains.eq(0).prop("selected", false);
  $mountains.eq(1).prop("selected", false);
  $mountains.eq(2).prop("selected", false);
  $mountains.eq(3).prop("selected", false);
  $mountains.eq(4).prop("selected", true).trigger("change"); // Everest

  waitForAjax(function () {
    $output = jQueryFrame($output.selector);
    return $output.text() == "8481 m";
  }, function () {
    $output = jQueryFrame($output.selector);
    assert.equal($output.text(), "8481 m");
    done();
  });
});
