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

QUnit.test("Open 'Client Popup' and press 'Cancel'.", function (assert) {
  assert.expect(3);

  let $popup = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  let $cancelButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  assert.equal($popup().attr("value"), "true");
  $openButton().click();
  assert.equal($popup().attr("value"), "false");
  $cancelButton().click();
  assert.equal($popup().attr("value"), "true");
});

QUnit.test("Open 'Client Popup', press 'Submit' while field is empty. Press 'Cancel'.", function (assert) {
  let $popup = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  let $output = jQueryFrameFn("#page\\:mainForm\\:form2\\:output span");
  let $messages = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let $inputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let $submitButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  let $cancelButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");
  let outputValue = $output().text();

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "true");
  });
  TTT.action(function () {
    $openButton().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "false");
  });
  TTT.action(function () {
    $inputField().val("");
    $submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.action(function () {
    $cancelButton().click();
  });
  TTT.asserts(2, function () {
    assert.equal($popup().attr("value"), "true");
    assert.equal($output().text(), outputValue);
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit' while field has content. Press 'Cancel'.", function (assert) {
  let $popup = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  let $output = jQueryFrameFn("#page\\:mainForm\\:form2\\:out span");
  let $messages = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let $inputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let $submitButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  let $cancelButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "true");
  });
  TTT.action(function () {
    $openButton().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "false");
  });
  TTT.action(function () {
    $inputField().val("test client popup - submit button");
    $submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.action(function () {
    $cancelButton().click();
  });
  TTT.asserts(2, function () {
    assert.equal($popup().attr("value"), "true");
    assert.equal($output().text(), "test client popup - submit button");
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field is empty.", function (assert) {
  let $popup = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  let $output = jQueryFrameFn("#page\\:mainForm\\:form2\\:output span");
  let $inputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let $submitCloseButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");
  let outputValue = $output().text();

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "true");
  });
  TTT.action(function () {
    $openButton().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "false");
  });
  TTT.action(function () {
    $inputField().val("");
    $submitCloseButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($popup().attr("value"), "true");
    assert.equal($output().text(), outputValue);
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field has content.", function (assert) {
  let $popup = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  let $output = jQueryFrameFn("#page\\:mainForm\\:form2\\:out span");
  let $inputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let $submitCloseButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "true");
  });
  TTT.action(function () {
    $openButton().click();
  });
  TTT.asserts(1, function () {
    assert.equal($popup().attr("value"), "false");
  });
  TTT.action(function () {
    $inputField().val("test client popup - submit and close button");
    $submitCloseButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($popup().attr("value"), "true");
    assert.equal($output().text(), "test client popup - submit and close button");
  });
  TTT.startTest();
});

QUnit.test("Open 'Large Popup'.", function (assert) {
  let $dropdownContainer = jQueryFrameFn("#page\\:mainForm\\:dropdownButton");
  let $dropdownButton = jQueryFrameFn("#page\\:mainForm\\:dropdownButton\\:\\:command");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:largePopupLink");
  let $popup = jQueryFrameFn("#page\\:mainForm\\:largePopup");
  let $closeButton = jQueryFrameFn("#page\\:mainForm\\:largePopup\\:closeLargePopup");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    if ($popup().hasClass("show")) {
      $closeButton().click();
    }
    if ($dropdownContainer().hasClass("show")) {
      $dropdownButton().click();
    }
  });
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), false);
    assert.equal($popup().hasClass("show"), false);
  });
  TTT.action(function () {
    $dropdownButton().click();
  });
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), true);
    assert.equal($popup().hasClass("show"), false);
  });
  TTT.action(function () {
    $openButton().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), false);
    assert.equal($popup().hasClass("show"), true);
  });
  TTT.action(function () {
    $closeButton().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), false);
    assert.equal($popup().hasClass("show"), false);
  });
  TTT.startTest();
});

QUnit.test("Open 'Small Popup'.", function (assert) {
  let $dropdownContainer = jQueryFrameFn("#page\\:mainForm\\:dropdownButton");
  let $dropdownButton = jQueryFrameFn("#page\\:mainForm\\:dropdownButton\\:\\:command");
  let $openButton = jQueryFrameFn("#page\\:mainForm\\:smallPopupLink");
  let $popup = jQueryFrameFn("#page\\:mainForm\\:smallPopup");
  let $closeButton = jQueryFrameFn("#page\\:mainForm\\:smallPopup\\:closeSmallPopup");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    if ($popup().hasClass("show")) {
      $closeButton().click();
    }
    if ($dropdownContainer().hasClass("show")) {
      $dropdownButton().click();
    }
  });
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), false);
    assert.equal($popup().hasClass("show"), false);
  });
  TTT.action(function () {
    $dropdownButton().click();
  });
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), true);
    assert.equal($popup().hasClass("show"), false);
  });
  TTT.action(function () {
    $openButton().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), false);
    assert.equal($popup().hasClass("show"), true);
  });
  TTT.action(function () {
    $closeButton().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal($dropdownContainer().hasClass("show"), false);
    assert.equal($popup().hasClass("show"), false);
  });
  TTT.startTest();
});
