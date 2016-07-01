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

QUnit.test("submit: select 'Nile'", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $rivers = jQueryFrame("#page\\:mainForm\\:riverList option");
  var $submit = jQueryFrame("#page\\:mainForm\\:riverSubmit");

  $rivers.eq(0).attr("selected", "selected"); // Nile
  $rivers.eq(1).removeAttr("selected"); // Amazon
  $rivers.eq(2).removeAttr("selected"); // Yangtze
  $rivers.eq(3).removeAttr("selected"); // Yellow River
  $rivers.eq(4).removeAttr("selected"); // Paraná River
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

  $rivers.eq(0).removeAttr("selected"); // Nile
  $rivers.eq(1).removeAttr("selected"); // Amazon
  $rivers.eq(2).attr("selected", "selected"); // Yangtze
  $rivers.eq(3).removeAttr("selected"); // Yellow River
  $rivers.eq(4).removeAttr("selected"); // Paraná River
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

  $mountains.eq(1).removeAttr("selected");
  $mountains.eq(2).removeAttr("selected");
  $mountains.eq(3).removeAttr("selected");
  $mountains.eq(4).removeAttr("selected");
  $mountains.eq(0).attr("selected", "selected").trigger("change"); // Everest

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/40-selectOneListbox/selectOneListbox.xhtml'
  }).done(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:selectedMountain span");
    assert.equal($output.text(), "8848 m");
    done();
  });
});

QUnit.test("ajax: select Makalu", function (assert) {
  assert.expect(1);
  var done = assert.async();
  var $mountains = jQueryFrame("#page\\:mainForm\\:mountainList option");

  $mountains.eq(0).removeAttr("selected");
  $mountains.eq(1).removeAttr("selected");
  $mountains.eq(2).removeAttr("selected");
  $mountains.eq(3).removeAttr("selected");
  $mountains.eq(4).attr("selected", "selected").trigger("change"); // Everest

  $.ajax({
    type: 'GET',
    url: 'content/20-component/030-select/40-selectOneListbox/selectOneListbox.xhtml'
  }).done(function () {
    var $output = jQueryFrame("#page\\:mainForm\\:selectedMountain span");
    assert.equal($output.text(), "8481 m");
    done();
  });
});
