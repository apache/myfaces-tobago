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

QUnit.test("Simple Popup", function (assert) {
  assert.expect(9);
  var done = assert.async(5);
  var step = 1;

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $open = jQueryFrame("#page\\:mainForm\\:simple\\:open");
  var $submitOnPage = jQueryFrame("#page\\:mainForm\\:simple\\:submitOnPage");
  var $in = jQueryFrame("#page\\:mainForm\\:simple\\:controllerPopup\\:in\\:\\:field");
  var $submitOnPopup = jQueryFrame("#page\\:mainForm\\:simple\\:controllerPopup\\:submitOnPopup");
  var $close = jQueryFrame("#page\\:mainForm\\:simple\\:controllerPopup\\:close");

  $open.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $in = jQueryFrame($in.selector);
      $submitOnPopup = jQueryFrame($submitOnPopup.selector);

      assert.equal($in.length, 1);
      $in.val("some text");
      $submitOnPopup.click();

      done();
    } else if (step == 2) {
      $messages = jQueryFrame($messages.selector);
      $in = jQueryFrame($in.selector);
      $submitOnPopup = jQueryFrame($submitOnPopup.selector);

      assert.equal($messages.length, 0);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "some text");
      $in.val("");
      $submitOnPopup.click();

      done();
    } else if (step == 3) {
      $messages = jQueryFrame($messages.selector);
      $in = jQueryFrame($in.selector);
      $close = jQueryFrame($close.selector);

      assert.equal($messages.length, 1);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "");
      $close.click();

      done();
    } else if (step == 4) {
      $submitOnPage = jQueryFrame($submitOnPage.selector);
      $in = jQueryFrame($in.selector);

      assert.equal($in.length, 0);
      $submitOnPage.click();

      done();
    } else if (step == 5) {
      $messages = jQueryFrame($messages.selector);

      assert.equal($messages.length, 0);

      done();
    }
    step++;
  });
});

QUnit.test("Full Server Request", function (assert) {
  assert.expect(9);
  var done = assert.async(5);
  var step = 1;

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $open = jQueryFrame("#page\\:mainForm\\:server\\:open");
  var $submitOnPage = jQueryFrame("#page\\:mainForm\\:server\\:submitOnPage");
  var $in = jQueryFrame("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:in\\:\\:field");
  var $submitOnPopup = jQueryFrame("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:submitOnPopup");
  var $close = jQueryFrame("#page\\:mainForm\\:server\\:fullServerRequestPopup\\:close");

  $open.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $in = jQueryFrame($in.selector);
      $submitOnPopup = jQueryFrame($submitOnPopup.selector);

      assert.equal($in.length, 1);
      $in.val("some text");
      $submitOnPopup.click();

      done();
    } else if (step == 2) {
      $messages = jQueryFrame($messages.selector);
      $in = jQueryFrame($in.selector);
      $submitOnPopup = jQueryFrame($submitOnPopup.selector);

      assert.equal($messages.length, 0);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "some text");
      $in.val("");
      $submitOnPopup.click();

      done();
    } else if (step == 3) {
      $messages = jQueryFrame($messages.selector);
      $in = jQueryFrame($in.selector);
      $close = jQueryFrame($close.selector);

      assert.equal($messages.length, 1);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "");
      $close.click();

      done();
    } else if (step == 4) {
      $submitOnPage = jQueryFrame($submitOnPage.selector);
      $in = jQueryFrame($in.selector);

      assert.equal($in.length, 0);
      $submitOnPage.click();

      done();
    } else if (step == 5) {
      $messages = jQueryFrame($messages.selector);

      assert.equal($messages.length, 0);

      done();
    }
    step++;
  });
});

QUnit.test("Client Sided", function (assert) {
  assert.expect(9);
  var done = assert.async(3);
  var step = 1;

  var $messages = jQueryFrame("#page\\:messages .tobago-messages");
  var $open = jQueryFrame("#page\\:mainForm\\:client\\:open");
  var $submitOnPage = jQueryFrame("#page\\:mainForm\\:client\\:submitOnPage");
  var $popupCollapsed = jQueryFrame("#page\\:mainForm\\:client\\:clientPopup\\:\\:collapse");
  var $in = jQueryFrame("#page\\:mainForm\\:client\\:clientPopup\\:in\\:\\:field");
  var $submitOnPopup = jQueryFrame("#page\\:mainForm\\:client\\:clientPopup\\:submitOnPopup");
  var $close = jQueryFrame("#page\\:mainForm\\:client\\:clientPopup\\:close");

  $open.click();
  assert.equal($popupCollapsed.val(), "false");
  $close.click();
  assert.equal($popupCollapsed.val(), "true");
  $open.click();
  assert.equal($popupCollapsed.val(), "false");
  $in.val("some text");
  $submitOnPopup.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $messages = jQueryFrame($messages.selector);
      $open = jQueryFrame($open.selector);
      $popupCollapsed = jQueryFrame($popupCollapsed.selector);
      $in = jQueryFrame($in.selector);
      $submitOnPopup = jQueryFrame($submitOnPopup.selector);

      assert.equal($messages.length, 0);
      assert.equal($popupCollapsed.val(), "true");
      $open.click();
      assert.equal($popupCollapsed.val(), "false");
      $in.val("");
      $submitOnPopup.click();

      done();
    } else if (step == 2) {
      $messages = jQueryFrame($messages.selector);
      $submitOnPage = jQueryFrame($submitOnPage.selector);
      $popupCollapsed = jQueryFrame($popupCollapsed.selector);

      assert.equal($messages.length, 1);
      assert.equal($popupCollapsed.val(), "true");
      $submitOnPage.click();

      done();
    } else if (step == 3) {
      $messages = jQueryFrame($messages.selector);

      assert.equal($messages.length, 1);

      done();
    }
    step++;
  });
});
