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

import {jQueryFrameFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Open 'Client Popup' and press 'Cancel'.", function (assert) {
  assert.expect(3);

  var popupFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  var cancelButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  assert.equal(popupFn().attr("value"), "true");
  openButtonFn().click();
  assert.equal(popupFn().attr("value"), "false");
  cancelButtonFn().click();
  assert.equal(popupFn().attr("value"), "true");
});

QUnit.test("Open 'Client Popup', press 'Submit' while field is empty. Press 'Cancel'.", function (assert) {
  var popupFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:output span");
  var messagesFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  var inputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var submitButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  var cancelButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");
  var outputValue = outputFn().text();

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().val("");
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.action(function () {
    cancelButtonFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(popupFn().attr("value"), "true");
    assert.equal(outputFn().text(), outputValue);
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit' while field has content. Press 'Cancel'.", function (assert) {
  var popupFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:out span");
  var messagesFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  var inputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var submitButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  var cancelButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().val("test client popup - submit button");
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.action(function () {
    cancelButtonFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(popupFn().attr("value"), "true");
    assert.equal(outputFn().text(), "test client popup - submit button");
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field is empty.", function (assert) {
  var popupFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:output span");
  var inputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var submitCloseButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");
  var outputValue = outputFn().text();

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().val("");
    submitCloseButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(popupFn().attr("value"), "true");
    assert.equal(outputFn().text(), outputValue);
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field has content.", function (assert) {
  var popupFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup input");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:open");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:out span");
  var inputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var submitCloseButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().attr("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().val("test client popup - submit and close button");
    submitCloseButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(popupFn().attr("value"), "true");
    assert.equal(outputFn().text(), "test client popup - submit and close button");
  });
  TTT.startTest();
});

QUnit.test("Open 'Large Popup'.", function (assert) {
  var dropdownContainerFn = jQueryFrameFn("#page\\:mainForm\\:dropdownButton");
  var dropdownButtonFn = jQueryFrameFn("#page\\:mainForm\\:dropdownButton\\:\\:command");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:largePopupLink");
  var popupFn = jQueryFrameFn("#page\\:mainForm\\:largePopup");
  var closeButtonFn = jQueryFrameFn("#page\\:mainForm\\:largePopup\\:closeLargePopup");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (popupFn().hasClass("show")) {
      closeButtonFn().click();
    }
    if (dropdownContainerFn().hasClass("show")) {
      dropdownButtonFn().click();
    }
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), false);
    assert.equal(popupFn().hasClass("show"), false);
  });
  TTT.action(function () {
    dropdownButtonFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), true);
    assert.equal(popupFn().hasClass("show"), false);
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), false);
    assert.equal(popupFn().hasClass("show"), true);
  });
  TTT.action(function () {
    closeButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), false);
    assert.equal(popupFn().hasClass("show"), false);
  });
  TTT.startTest();
});

QUnit.test("Open 'Small Popup'.", function (assert) {
  var dropdownContainerFn = jQueryFrameFn("#page\\:mainForm\\:dropdownButton");
  var dropdownButtonFn = jQueryFrameFn("#page\\:mainForm\\:dropdownButton\\:\\:command");
  var openButtonFn = jQueryFrameFn("#page\\:mainForm\\:smallPopupLink");
  var popupFn = jQueryFrameFn("#page\\:mainForm\\:smallPopup");
  var closeButtonFn = jQueryFrameFn("#page\\:mainForm\\:smallPopup\\:closeSmallPopup");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (popupFn().hasClass("show")) {
      closeButtonFn().click();
    }
    if (dropdownContainerFn().hasClass("show")) {
      dropdownButtonFn().click();
    }
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), false);
    assert.equal(popupFn().hasClass("show"), false);
  });
  TTT.action(function () {
    dropdownButtonFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), true);
    assert.equal(popupFn().hasClass("show"), false);
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), false);
    assert.equal(popupFn().hasClass("show"), true);
  });
  TTT.action(function () {
    closeButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().hasClass("show"), false);
    assert.equal(popupFn().hasClass("show"), false);
  });
  TTT.startTest();
});
