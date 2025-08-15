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
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("Open 'modal=false'-Popup, close it, press 'Submit'", function (done) {
  const timestampOutput = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const openButton = elementByIdFn("page:mainForm:showModalFalse");
  const submit = elementByIdFn("page:mainForm:submit");
  const wrapper = elementByIdFn("page:mainForm:popupWrapper");
  const popup = elementByIdFn("page:mainForm:modalFalse");
  const collapse = elementByIdFn("page:mainForm:modalFalse::collapse");
  const backdropClick = elementByIdFn("backdropClick");

  let timestamp;
  let shownEventCount = 0;
  let hiddenEventCount = 0;
  wrapper().addEventListener("shown.bs.modal", () => shownEventCount++);
  wrapper().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.event("click", openButton, () => shownEventCount === 1);
  test.do(() => expect(popup().classList).toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("false"));

  test.event("click", backdropClick, () => hiddenEventCount === 1);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.do(() => timestamp = Number(timestampOutput().textContent));
  test.event("click", submit, () => Number(timestampOutput().textContent) > timestamp);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.start();
});

it("Open 'modal=false'-Popup, close it via ESC, press 'Submit'", function (done) {
  const timestampOutput = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const openButton = elementByIdFn("page:mainForm:showModalFalse");
  const submit = elementByIdFn("page:mainForm:submit");
  const wrapper = elementByIdFn("page:mainForm:popupWrapper");
  const popup = elementByIdFn("page:mainForm:modalFalse");
  const collapse = elementByIdFn("page:mainForm:modalFalse::collapse");

  let timestamp;
  let shownEventCount = 0;
  let hiddenEventCount = 0;
  wrapper().addEventListener("shown.bs.modal", () => shownEventCount++);
  wrapper().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.event("click", openButton, () => shownEventCount === 1);
  test.do(() => expect(popup().classList).toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("false"));

  test.do(() => popup().dispatchEvent(new KeyboardEvent("keydown", {key: "Escape", bubbles: true})));
  test.wait(() => hiddenEventCount === 1);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.do(() => timestamp = Number(timestampOutput().textContent));
  test.event("click", submit, () => Number(timestampOutput().textContent) > timestamp);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.start();
});

it("Open 'modal=true'-Popup, close it, press 'Submit'", function (done) {
  const body = querySelectorFn("body");
  const timestampOutput = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const openButton = elementByIdFn("page:mainForm:showModalTrue");
  const submit = elementByIdFn("page:mainForm:submit");
  const wrapper = elementByIdFn("page:mainForm:popupWrapper");
  const popup = elementByIdFn("page:mainForm:modalTrue");
  const collapse = elementByIdFn("page:mainForm:modalTrue::collapse");
  const closeButton = elementByIdFn("page:mainForm:modalTrue:hideModalTrue");

  let timestamp;
  let shownEventCount = 0;
  wrapper().addEventListener("shown.bs.modal", () => shownEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.event("click", openButton, () => shownEventCount === 1);
  test.do(() => expect(popup().classList).toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("false"));

  test.event("click", closeButton, () => !body().classList.contains("modal-open"));
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.do(() => timestamp = Number(timestampOutput().textContent));
  test.event("click", submit, () => Number(timestampOutput().textContent) > timestamp);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.start();
});

it("Open 'modal=true'-Popup, try to close it via ESC", function (done) {
  const body = querySelectorFn("body");
  const openButton = elementByIdFn("page:mainForm:showModalTrue");
  const wrapper = elementByIdFn("page:mainForm:popupWrapper");
  const popup = elementByIdFn("page:mainForm:modalTrue");
  const collapse = elementByIdFn("page:mainForm:modalTrue::collapse");
  const closeButton = elementByIdFn("page:mainForm:modalTrue:hideModalTrue");

  let shownEventCount = 0;
  wrapper().addEventListener("shown.bs.modal", () => shownEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.event("click", openButton, () => shownEventCount === 1);
  test.do(() => expect(popup().classList).toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("false"));

  test.do(() => popup().dispatchEvent(new KeyboardEvent("keydown", {key: "Escape", bubbles: true})));
  test.waitMs(1000);
  test.do(() => expect(popup().classList).toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("false"));

  //finally close via close button
  test.event("click", closeButton, () => !body().classList.contains("modal-open"));
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));

  test.start();
});

it("Open Popup 3, close it, press 'Submit'", function (done) {
  const timestampOutput = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const openButton = elementByIdFn("page:mainForm:showPopup3");
  const submit = elementByIdFn("page:mainForm:submit");
  const wrapper = elementByIdFn("page:mainForm:popupWrapper");
  const popup = elementByIdFn("page:mainForm:popup3");
  const collapse = elementByIdFn("page:mainForm:popup3::collapse");
  const backdropClick = elementByIdFn("popup3BackdropClick");
  const popup3CollapsedOutput = querySelectorFn("#page\\:mainForm\\:popup3Collapsed .form-control-plaintext");

  let timestamp;
  let shownEventCount = 0;
  let hiddenEventCount = 0;
  wrapper().addEventListener("shown.bs.modal", () => shownEventCount++);
  wrapper().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));
  test.do(() => expect(popup3CollapsedOutput().textContent).toBe("true"));

  test.event("click", openButton, () => shownEventCount === 1);
  test.do(() => expect(popup().classList).toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("false"));
  test.do(() => expect(popup3CollapsedOutput().textContent).toBe("false"));

  test.event("click", backdropClick, () => hiddenEventCount === 1);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));
  test.do(() => expect(popup3CollapsedOutput().textContent).toBe("false"));

  test.do(() => timestamp = Number(timestampOutput().textContent));
  test.event("click", submit, () => Number(timestampOutput().textContent) > timestamp);
  test.do(() => expect(popup().classList).not.toContain("show"));
  test.do(() => expect(collapse().getAttribute("value")).toBe("true"));
  test.do(() => expect(popup3CollapsedOutput().textContent).toBe("true"));

  test.start();
});
