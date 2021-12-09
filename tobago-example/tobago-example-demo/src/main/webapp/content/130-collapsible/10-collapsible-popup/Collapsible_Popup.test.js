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

it("Simple Popup", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const openFn = elementByIdFn("page:mainForm:simple:open1");
  const submitOnPageFn = elementByIdFn("page:mainForm:simple:submitOnPage1");
  const popupFn = elementByIdFn("page:mainForm:simple:simplePopup");
  const inFn = elementByIdFn("page:mainForm:simple:simplePopup:in1::field");
  const outFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePopup\\:out1 .form-control-plaintext");
  const submitOnPopupFn = elementByIdFn("page:mainForm:simple:simplePopup:submitOnPopup1");
  const closeFn = elementByIdFn("page:mainForm:simple:simplePopup:close1");

  const clientInFn = elementByIdFn("page:mainForm:client:clientPopup:in3::field");
  const clientSubmitOnPopupFn = elementByIdFn("page:mainForm:client:clientPopup:submitOnPopup3");

  const date = new Date().toString();

  const test = new JasmineTestTool(done);
  test.setup(() => !popupFn().classList.contains("show"), null, "click", closeFn);
  test.event("click", openFn, () => popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(inFn()).not.toBeNull());

  test.do(() => inFn().value = date);
  test.event("click", submitOnPopupFn, () => outFn().textContent === date);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(inFn().value).toBe(date));
  test.do(() => expect(outFn().textContent).toBe(date));

  test.do(() => inFn().value = "");
  test.event("click", submitOnPopupFn, () => messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(inFn().value).toBe(""));
  test.do(() => expect(outFn().textContent).toBe(date));

  test.event("click", closeFn, () => !popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(inFn()).toBeNull());

  //add an error message for the next step
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => clientInFn().value = "");
  test.event("click", clientSubmitOnPopupFn, () => messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));

  test.event("click", submitOnPageFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Full Server Request", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const openFn = elementByIdFn("page:mainForm:server:open2");
  const submitOnPageFn = elementByIdFn("page:mainForm:server:submitOnPage2");
  const popupFn = elementByIdFn("page:mainForm:server:serverPopup");
  const inFn = elementByIdFn("page:mainForm:server:serverPopup:in2::field");
  const outFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPopup\\:out2 .form-control-plaintext");
  const submitOnPopupFn = elementByIdFn("page:mainForm:server:serverPopup:submitOnPopup2");
  const closeFn = elementByIdFn("page:mainForm:server:serverPopup:close2");

  const clientInFn = elementByIdFn("page:mainForm:client:clientPopup:in3::field");
  const clientSubmitOnPopupFn = elementByIdFn("page:mainForm:client:clientPopup:submitOnPopup3");

  const date = new Date().toString();

  const test = new JasmineTestTool(done);
  test.setup(() => !popupFn().classList.contains("show"), null, "click", closeFn);
  test.event("click", openFn, () => popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(inFn()).not.toBeNull());

  test.do(() => inFn().value = date);
  test.event("click", submitOnPopupFn, () => outFn().textContent === date);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(inFn().value).toBe(date));
  test.do(() => expect(outFn().textContent).toBe(date));

  test.do(() => inFn().value = "");
  test.event("click", submitOnPopupFn, () => messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(inFn()).not.toBeNull());
  test.do(() => expect(inFn().value).toBe(""));
  test.do(() => expect(outFn().textContent).toBe(date));

  test.event("click", closeFn, () => !popupFn().classList.contains("show"));
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(inFn()).toBeNull());

  //add an error message for the next step
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => clientInFn().value = "");
  test.event("click", clientSubmitOnPopupFn, () => messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));

  test.event("click", submitOnPageFn, () => messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Client Side", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const openFn = elementByIdFn("page:mainForm:client:open3");
  const submitOnPageFn = elementByIdFn("page:mainForm:client:submitOnPage3");
  const popupFn = elementByIdFn("page:mainForm:client:clientPopup");
  const collapseFn = elementByIdFn("page:mainForm:client:clientPopup::collapse");
  const inFn = elementByIdFn("page:mainForm:client:clientPopup:in3::field");
  const outFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPopup\\:out3 .form-control-plaintext");
  const submitOnPopupFn = elementByIdFn("page:mainForm:client:clientPopup:submitOnPopup3");
  const closeFn = elementByIdFn("page:mainForm:client:clientPopup:close3");

  const simpleSubmitOnPageFn = elementByIdFn("page:mainForm:simple:submitOnPage1");

  const date = new Date().toString();

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  popupFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.do(() => hiddenEventCount = 1);
  test.setup(() => collapseFn().value === "true" && hiddenEventCount === 1,
      () => hiddenEventCount = 0,
      "click", closeFn);

  test.do(() => shownEventCount = 0);
  test.event("click", openFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("false"));

  test.do(() => hiddenEventCount = 0);
  test.event("click", closeFn, () => hiddenEventCount > 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("true"));

  test.do(() => shownEventCount = 0);
  test.event("click", openFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("false"));

  test.do(() => inFn().value = date);
  test.event("click", submitOnPopupFn, () => outFn().textContent === date);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("true"));
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(inFn().value).toBe(date));
  test.do(() => expect(outFn().textContent).toBe(date));

  test.do(() => popupFn().addEventListener("shown.bs.modal", () => shownEventCount++));
  test.do(() => popupFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++));

  test.do(() => shownEventCount = 0);
  test.event("click", openFn, () => shownEventCount > 0);
  test.do(() => expect(popupFn().classList).toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("false"));

  test.do(() => inFn().value = "");
  test.event("click", submitOnPopupFn, () => messagesFn().length === 1);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("true"));
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(inFn().value).toBe(""));
  test.do(() => expect(outFn().textContent).toBe(date));

  //remove error message for the next step
  test.event("click", simpleSubmitOnPageFn, () => messagesFn().length === 0);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("true"));
  test.do(() => expect(messagesFn().length).toBe(0));

  test.event("click", submitOnPageFn, () => messagesFn().length === 1);
  test.do(() => expect(popupFn().classList).not.toContain("show"));
  test.do(() => expect(collapseFn().value).toBe("true"));
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});
