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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("not implemented yet", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});
/*
import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Open 'Client Popup' and press 'Cancel'.", function (assert) {
  assert.expect(3);

  let popupFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:open");
  let cancelButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  assert.equal(popupFn().getAttribute("value"), "true");
  openButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  assert.equal(popupFn().getAttribute("value"), "false");
  cancelButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  assert.equal(popupFn().getAttribute("value"), "true");
});

QUnit.test("Open 'Client Popup', press 'Submit' while field is empty. Press 'Cancel'.", function (assert) {
  let popupFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out span");
  let messagesFn = querySelectorAllFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  let cancelButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");
  let outputValue = outputFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().value = "";
    submitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.action(function () {
    cancelButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(2, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
    assert.equal(outputFn().textContent, outputValue);
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit' while field has content. Press 'Cancel'.", function (assert) {
  let popupFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out span");
  let messagesFn = querySelectorAllFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  let cancelButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().value = "test client popup - submit button";
    submitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.action(function () {
    cancelButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(2, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
    assert.equal(outputFn().textContent, "test client popup - submit button");
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field is empty.", function (assert) {
  let popupFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out span");
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let submitCloseButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");
  let outputValue = outputFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().value = "";
    submitCloseButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
    assert.equal(outputFn().textContent, outputValue);
  });
  TTT.startTest();
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field has content.", function (assert) {
  let popupFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup input");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out span");
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  let submitCloseButtonFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
  });
  TTT.action(function () {
    openButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.equal(popupFn().getAttribute("value"), "false");
  });
  TTT.action(function () {
    inputFieldFn().value = "test client popup - submit and close button";
    submitCloseButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(popupFn().getAttribute("value"), "true");
    assert.equal(outputFn().textContent, "test client popup - submit and close button");
  });
  TTT.startTest();
});

QUnit.test("Open 'Large Popup'.", function (assert) {
  let dropdownContainerFn = querySelectorFn("#page\\:mainForm\\:dropdownButton");
  let dropdownButtonFn = querySelectorFn("#page\\:mainForm\\:dropdownButton\\:\\:command");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:largePopupLink");
  let popupFn = querySelectorFn("#page\\:mainForm\\:largePopup");
  let closeButtonFn = querySelectorFn("#page\\:mainForm\\:largePopup\\:closeLargePopup");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (popupFn().classList.contains("show")) {
      closeButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
    }
    if (dropdownContainerFn().classList.contains("show")) {
      dropdownButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
    }
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), false);
    assert.equal(popupFn().classList.contains("show"), false);
  });
  TTT.action(function () {
    dropdownButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), true);
    assert.equal(popupFn().classList.contains("show"), false);
  });
  TTT.action(function () {
    openButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), false);
    assert.equal(popupFn().classList.contains("show"), true);
  });
  TTT.action(function () {
    closeButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), false);
    assert.equal(popupFn().classList.contains("show"), false);
  });
  TTT.startTest();
});

QUnit.test("Open 'Small Popup'.", function (assert) {
  let dropdownContainerFn = querySelectorFn("#page\\:mainForm\\:dropdownButton");
  let dropdownButtonFn = querySelectorFn("#page\\:mainForm\\:dropdownButton\\:\\:command");
  let openButtonFn = querySelectorFn("#page\\:mainForm\\:smallPopupLink");
  let popupFn = querySelectorFn("#page\\:mainForm\\:smallPopup");
  let closeButtonFn = querySelectorFn("#page\\:mainForm\\:smallPopup\\:closeSmallPopup");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (popupFn().classList.contains("show")) {
      closeButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
    }
    if (dropdownContainerFn().classList.contains("show")) {
      dropdownButtonFn().click();
    }
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), false);
    assert.equal(popupFn().classList.contains("show"), false);
  });
  TTT.action(function () {
    dropdownButtonFn().click();
  });
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), true);
    assert.equal(popupFn().classList.contains("show"), false);
  });
  TTT.action(function () {
    openButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), false);
    assert.equal(popupFn().classList.contains("show"), true);
  });
  TTT.action(function () {
    closeButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(dropdownContainerFn().classList.contains("show"), false);
    assert.equal(popupFn().classList.contains("show"), false);
  });
  TTT.startTest();
});
*/
