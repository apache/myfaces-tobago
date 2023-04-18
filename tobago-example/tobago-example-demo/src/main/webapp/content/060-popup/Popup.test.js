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
import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("Open 'Client Popup' and press 'Cancel'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:form2:clientPopup");
  let collapseFn = elementByIdFn("page:mainForm:form2:clientPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:form2:open");
  let cancelButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:cancel2");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", cancelButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.do(() => hiddenEventCount = 0);
  test.event("click", cancelButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.start();
});

it("Open 'Client Popup', press 'Submit' while field is empty. Press 'Cancel'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:form2:clientPopup");
  let collapseFn = elementByIdFn("page:mainForm:form2:clientPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:form2:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out input");
  let messagesFn = querySelectorAllFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let messageCloseFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages .btn-close");
  let inputFieldFn = elementByIdFn("page:mainForm:form2:clientPopup:in2::field");
  let submitButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:submit2");
  let cancelButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:cancel2");
  let outputValue = outputFn().value;

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", cancelButtonFn);
  test.setup(() => messagesFn().length === 0, null, "click", messageCloseFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(messagesFn().length).toEqual(0));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.do(() => inputFieldFn().value = "");
  test.event("click", submitButtonFn, () => messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toEqual(1));

  test.do(() => hiddenEventCount = 0);
  test.event("click", cancelButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(outputFn().value).toBe(outputValue));
  test.start();
});

it("Open 'Client Popup', press 'Submit' while field has content. Press 'Cancel'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:form2:clientPopup");
  let collapseFn = elementByIdFn("page:mainForm:form2:clientPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:form2:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out input");
  let messagesFn = querySelectorAllFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let inputFieldFn = elementByIdFn("page:mainForm:form2:clientPopup:in2::field");
  let submitButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:submit2");
  let cancelButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:cancel2");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", cancelButtonFn);
  test.setup(() => outputFn().value !== "Tobago",
    () => inputFieldFn().value = "Trinidad",
    "click", submitButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(outputFn().value).not.toBe("Tobago"));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.do(() => inputFieldFn().value = "Tobago");
  test.event("click", submitButtonFn, () => outputFn().value === "Tobago");
  test.do(() => expect(outputFn().value).toBe("Tobago"));

  test.do(() => hiddenEventCount = 0);
  test.event("click", cancelButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(messagesFn().length).toEqual(0));
  test.start();
});

it("Open 'Client Popup', press 'Submit & Close' while field is empty.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:form2:clientPopup");
  let collapseFn = elementByIdFn("page:mainForm:form2:clientPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:form2:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out input");
  let messagesFn = querySelectorAllFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let messageCloseFn = querySelectorFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages .btn-close");
  let inputFieldFn = elementByIdFn("page:mainForm:form2:clientPopup:in2::field");
  let submitCloseButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:submitClose2");
  let cancelButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:cancel2");
  let outputValue = outputFn().value;

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", cancelButtonFn);
  test.setup(() => messagesFn().length === 0, null, "click", messageCloseFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(messagesFn().length).toEqual(0));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.do(() => inputFieldFn().value = "");
  test.do(() => hiddenEventCount = 0);
  test.event("click", submitCloseButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(messagesFn().length).toEqual(1));
  test.do(() => expect(outputFn().value).toBe(outputValue));
  test.start();
});

it("Open 'Client Popup', press 'Submit & Close' while field has content.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:form2:clientPopup");
  let collapseFn = elementByIdFn("page:mainForm:form2:clientPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:form2:open");
  let outputFn = querySelectorFn("#page\\:mainForm\\:form2\\:out input");
  let messagesFn = querySelectorAllFn("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  let inputFieldFn = elementByIdFn("page:mainForm:form2:clientPopup:in2::field");
  let submitButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:submit2");
  let submitCloseButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:submitClose2");
  let cancelButtonFn = elementByIdFn("page:mainForm:form2:clientPopup:cancel2");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", cancelButtonFn);
  test.setup(() => outputFn().value !== "Little Tobago",
    () => inputFieldFn().value = "Charlotteville",
    "click", submitButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(outputFn().value).not.toBe("Little Tobago"));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.do(() => inputFieldFn().value = "Little Tobago");
  test.do(() => hiddenEventCount = 0);
  test.event("click", submitCloseButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(messagesFn().length).toEqual(0));
  test.do(() => expect(outputFn().value).toBe("Little Tobago"));
  test.start();
});

it("Open 'Large Popup'.", function (done) {
  let dropdownButtonFn = elementByIdFn("page:mainForm:dropdownButton::command");
  let dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name=page\\:mainForm\\:dropdownButton]");
  let openButtonFn = elementByIdFn("page:mainForm:largePopupLink");
  let popupFn = elementByIdFn("page:mainForm:largePopup");
  let collapseFn = elementByIdFn("page:mainForm:largePopup::collapse");
  let closeButtonFn = elementByIdFn("page:mainForm:largePopup:closeLargePopup");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", closeButtonFn);
  test.setup(() => !dropdownMenuFn().classList.contains("show"),
    null, "click", dropdownButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));

  test.event("click", dropdownButtonFn, () => dropdownMenuFn().classList.contains("show"));
  test.do(() => expect(dropdownMenuFn().classList).toContain("show"));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));

  test.do(() => hiddenEventCount = 0);
  test.event("click", closeButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.start();
});

it("Open 'Small Popup'.", function (done) {
  let dropdownButtonFn = elementByIdFn("page:mainForm:dropdownButton::command");
  let dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name=page\\:mainForm\\:dropdownButton]");
  let openButtonFn = elementByIdFn("page:mainForm:smallPopupLink");
  let popupFn = elementByIdFn("page:mainForm:smallPopup");
  let collapseFn = elementByIdFn("page:mainForm:smallPopup::collapse");
  let closeButtonFn = elementByIdFn("page:mainForm:smallPopup:closeSmallPopup");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", closeButtonFn);
  test.setup(() => !dropdownMenuFn().classList.contains("show"),
    null, "click", dropdownButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));

  test.event("click", dropdownButtonFn, () => dropdownMenuFn().classList.contains("show"));
  test.do(() => expect(dropdownMenuFn().classList).toContain("show"));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));

  test.do(() => hiddenEventCount = 0);
  test.event("click", closeButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.start();
});

it("open 'refresh content popup' and press 'close'", function (done) {
  const wrapperFn = elementByIdFn("page:mainForm:wrapperForIntegrationTest");
  const popupFn = elementByIdFn("page:mainForm:refreshPopup");
  const collapseFn = elementByIdFn("page:mainForm:refreshPopup::collapse");
  const openButtonFn = elementByIdFn("page:mainForm:openRefreshPopup");
  const closeButtonFn = elementByIdFn("page:mainForm:refreshPopup:closeRefreshPopup");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  wrapperFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  wrapperFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().getAttribute("value") === "true" && hiddenEventCount === 1,
    () => hiddenEventCount = 0,
    "click", closeButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));

  test.do(() => shownEventCount = 0);
  test.event("click", openButtonFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.do(() => hiddenEventCount = 0);
  test.event("click", closeButtonFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.start();
});
