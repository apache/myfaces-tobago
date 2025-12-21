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
import {elementByIdFn} from "/script/tobago-test.js";

it("Open 'Client Popup' and press 'Close'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:clientPopup");
  let collapseFn = elementByIdFn("page:mainForm:clientPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:openClientPopup");
  let closeButtonFn = elementByIdFn("page:mainForm:clientPopup:closeClientPopup");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

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

it("Open 'Ajax Popup' and press 'Close'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:ajaxPopup");
  let collapseFn = elementByIdFn("page:mainForm:ajaxPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:openAjaxPopup");
  let closeButtonFn = elementByIdFn("page:mainForm:ajaxPopup:closeAjaxPopup");

  const test = new JasmineTestTool(done);
  test.setup(() => collapseFn().getAttribute("value") === "true", null, "click", closeButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));

  test.event("click", openButtonFn, () => popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.event("click", closeButtonFn, () => !popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.start();
});

it("Open 'Server Popup' and press 'Close'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:serverPopup");
  let collapseFn = elementByIdFn("page:mainForm:serverPopup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:openServerPopup");
  let closeButtonFn = elementByIdFn("page:mainForm:serverPopup:closeServerPopup");

  const test = new JasmineTestTool(done);
  test.setup(() => collapseFn().getAttribute("value") === "true", null, "click", closeButtonFn);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));

  test.event("click", openButtonFn, () => popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("false"));

  test.event("click", closeButtonFn, () => !popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().getAttribute("value")).toBe("true"));
  test.start();
});
