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

QUnit.test("Required: Submit without content.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $in = jQueryFrame("#page\\:mainForm\\:required\\:in\\:\\:field");
  var $submit = jQueryFrame("#page\\:mainForm\\:required\\:submit");

  $in.val("");
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Required: Submit with content.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $in = jQueryFrame("#page\\:mainForm\\:required\\:in\\:\\:field");
  var $submit = jQueryFrame("#page\\:mainForm\\:required\\:submit");

  $in.val("some content");
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 0);
    done();
  });
});

QUnit.test("Length: Submit single character.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $in = jQueryFrame("#page\\:mainForm\\:length\\:in\\:\\:field");
  var $submit = jQueryFrame("#page\\:mainForm\\:length\\:submit");

  $in.val("a");
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Length: Submit three characters.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $in = jQueryFrame("#page\\:mainForm\\:length\\:in\\:\\:field");
  var $submit = jQueryFrame("#page\\:mainForm\\:length\\:submit");

  $in.val("abc");
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 0);
    done();
  });
});

QUnit.test("Length: Submit five characters.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $in = jQueryFrame("#page\\:mainForm\\:length\\:in\\:\\:field");
  var $submit = jQueryFrame("#page\\:mainForm\\:length\\:submit");

  $in.val("abcde");
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 1);
    done();
  });
});
