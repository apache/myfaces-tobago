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

  var messagesSelector = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(messagesSelector);
  var $open = jQueryFrame("#page\\:mainForm\\:simple\\:open1");
  var submitOnPageSelector = "#page\\:mainForm\\:simple\\:submitOnPage1";
  var $submitOnPage = jQueryFrame(submitOnPageSelector);
  var inSelector = "#page\\:mainForm\\:simple\\:controllerPopup\\:in1\\:\\:field";
  var $in = jQueryFrame(inSelector);
  var submitOnPopupSelector = "#page\\:mainForm\\:simple\\:controllerPopup\\:submitOnPopup1";
  var $submitOnPopup = jQueryFrame(submitOnPopupSelector);
  var closeSelector = "#page\\:mainForm\\:simple\\:controllerPopup\\:close1";
  var $close = jQueryFrame(closeSelector);

  $open.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step == 1) {
      $in = jQueryFrame(inSelector);
      $submitOnPopup = jQueryFrame(submitOnPopupSelector);

      assert.equal($in.length, 1);
      $in.val("some text");
      $submitOnPopup.click();

      done();
    } else if (step == 2) {
      $messages = jQueryFrame(messagesSelector);
      $in = jQueryFrame(inSelector);
      $submitOnPopup = jQueryFrame(submitOnPopupSelector);

      assert.equal($messages.length, 0);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "some text");
      $in.val("");
      $submitOnPopup.click();

      done();
    } else if (step == 3) {
      $messages = jQueryFrame(messagesSelector);
      $in = jQueryFrame(inSelector);
      $close = jQueryFrame(closeSelector);

      assert.equal($messages.length, 1);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "");
      $close.click();

      done();
    } else if (step == 4) {
      $submitOnPage = jQueryFrame(submitOnPageSelector);
      $in = jQueryFrame(inSelector);

      assert.equal($in.length, 0);
      $submitOnPage.click();

      done();
    } else if (step == 5) {
      $messages = jQueryFrame(messagesSelector);

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

  var messagesSelector = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(messagesSelector);
  var $open = jQueryFrame("#page\\:mainForm\\:server\\:open2");
  var submitOnPageSelector = "#page\\:mainForm\\:server\\:submitOnPage2";
  var $submitOnPage = jQueryFrame(submitOnPageSelector);
  var inSelector = "#page\\:mainForm\\:server\\:fullServerRequestPopup\\:in2\\:\\:field";
  var $in = jQueryFrame(inSelector);
  var submitOnPopupSelector = "#page\\:mainForm\\:server\\:fullServerRequestPopup\\:submitOnPopup2";
  var $submitOnPopup = jQueryFrame(submitOnPopupSelector);
  var closeSelector = "#page\\:mainForm\\:server\\:fullServerRequestPopup\\:close2";
  var $close = jQueryFrame(closeSelector);

  $open.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step == 1) {
      $in = jQueryFrame(inSelector);
      $submitOnPopup = jQueryFrame(submitOnPopupSelector);

      assert.equal($in.length, 1);
      $in.val("some text");
      $submitOnPopup.click();

      done();
    } else if (step == 2) {
      $messages = jQueryFrame(messagesSelector);
      $in = jQueryFrame(inSelector);
      $submitOnPopup = jQueryFrame(submitOnPopupSelector);

      assert.equal($messages.length, 0);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "some text");
      $in.val("");
      $submitOnPopup.click();

      done();
    } else if (step == 3) {
      $messages = jQueryFrame(messagesSelector);
      $in = jQueryFrame(inSelector);
      $close = jQueryFrame(closeSelector);

      assert.equal($messages.length, 1);
      assert.equal($in.length, 1);
      assert.equal($in.val(), "");
      $close.click();

      done();
    } else if (step == 4) {
      $submitOnPage = jQueryFrame(submitOnPageSelector);
      $in = jQueryFrame(inSelector);

      assert.equal($in.length, 0);
      $submitOnPage.click();

      done();
    } else if (step == 5) {
      $messages = jQueryFrame(messagesSelector);

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

  var messagesSelector = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(messagesSelector);
  var openSelector = "#page\\:mainForm\\:client\\:open3";
  var $open = jQueryFrame(openSelector);
  var submitOnPageSelector = "#page\\:mainForm\\:client\\:submitOnPage3";
  var $submitOnPage = jQueryFrame(submitOnPageSelector);
  var popupCollapsedSelector = "#page\\:mainForm\\:client\\:clientPopup\\:\\:collapse";
  var $popupCollapsed = jQueryFrame(popupCollapsedSelector);
  var inSelector = "#page\\:mainForm\\:client\\:clientPopup\\:in3\\:\\:field";
  var $in = jQueryFrame(inSelector);
  var submitOnPopupSelector = "#page\\:mainForm\\:client\\:clientPopup\\:submitOnPopup3";
  var $submitOnPopup = jQueryFrame(submitOnPopupSelector);
  var $close = jQueryFrame("#page\\:mainForm\\:client\\:clientPopup\\:close3");

  $open.click();
  assert.equal($popupCollapsed.val(), "false");
  $close.click();
  assert.equal($popupCollapsed.val(), "true");
  $open.click();
  assert.equal($popupCollapsed.val(), "false");
  $in.val("some text");
  $submitOnPopup.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step == 1) {
      $messages = jQueryFrame(messagesSelector);
      $open = jQueryFrame(openSelector);
      $popupCollapsed = jQueryFrame(popupCollapsedSelector);
      $in = jQueryFrame(inSelector);
      $submitOnPopup = jQueryFrame(submitOnPopupSelector);

      assert.equal($messages.length, 0);
      assert.equal($popupCollapsed.val(), "true");
      $open.click();
      assert.equal($popupCollapsed.val(), "false");
      $in.val("");
      $submitOnPopup.click();

      done();
    } else if (step == 2) {
      $messages = jQueryFrame(messagesSelector);
      $submitOnPage = jQueryFrame(submitOnPageSelector);
      $popupCollapsed = jQueryFrame(popupCollapsedSelector);

      assert.equal($messages.length, 1);
      assert.equal($popupCollapsed.val(), "true");
      $submitOnPage.click();

      done();
    } else if (step == 3) {
      $messages = jQueryFrame(messagesSelector);

      assert.equal($messages.length, 1);

      done();
    }
    step++;
  });
});
