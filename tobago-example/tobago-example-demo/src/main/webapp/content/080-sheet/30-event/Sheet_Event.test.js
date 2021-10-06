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

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("On click with ajax", function (done) {
  const onClickAjaxFn = elementByIdFn("page:mainForm:changeExample::0");
  const onClickAjaxPopupFn = elementByIdFn("page:mainForm:changeExample::3");
  const detailsBoxFn = elementByIdFn("page:mainForm:detail");
  const detailsBoxNameFieldFn = elementByIdFn("page:mainForm:name::field");
  const sunFn = elementByIdFn("page:mainForm:s1:0:sample0");
  const venusFn = elementByIdFn("page:mainForm:s1:2:sample0");
  const jupiterFn = elementByIdFn("page:mainForm:s1:5:sample0");
  const saturnFn = elementByIdFn("page:mainForm:s1:6:sample0");

  const test = new JasmineTestTool(done);
  test.setup(() => detailsBoxFn() === null,
      () => onClickAjaxPopupFn().checked = true, "change", onClickAjaxPopupFn);
  test.setup(() => detailsBoxFn() !== null,
      () => onClickAjaxFn().checked = true, "change", onClickAjaxFn);
  test.setup(() => detailsBoxNameFieldFn() !== null && detailsBoxNameFieldFn().value === "Sun",
      null, "click", sunFn);
  test.event("click", venusFn, () => detailsBoxNameFieldFn().value === "Venus");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Venus"));
  test.event("click", jupiterFn, () => detailsBoxNameFieldFn().value === "Jupiter");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Jupiter"));
  test.event("click", saturnFn, () => detailsBoxNameFieldFn().value === "Saturn");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Saturn"));
  test.start();
});

it("On click with full request", function (done) {
  const onClickFullRequestFn = elementByIdFn("page:mainForm:changeExample::1");
  const onClickAjaxPopupFn = elementByIdFn("page:mainForm:changeExample::3");
  const detailsBoxFn = elementByIdFn("page:mainForm:detail");
  const detailsBoxNameFieldFn = elementByIdFn("page:mainForm:name::field");
  const sunFn = elementByIdFn("page:mainForm:s1:0:sample1");
  const venusFn = elementByIdFn("page:mainForm:s1:2:sample1");
  const jupiterFn = elementByIdFn("page:mainForm:s1:5:sample1");
  const saturnFn = elementByIdFn("page:mainForm:s1:6:sample1");

  const test = new JasmineTestTool(done);
  test.setup(() => detailsBoxFn() === null,
      () => onClickAjaxPopupFn().checked = true, "change", onClickAjaxPopupFn);
  test.setup(() => detailsBoxFn() !== null,
      () => onClickFullRequestFn().checked = true, "change", onClickFullRequestFn);
  test.setup(() => detailsBoxNameFieldFn() !== null && detailsBoxNameFieldFn().value === "Sun",
      null, "click", sunFn);
  test.event("click", venusFn, () => detailsBoxNameFieldFn().value === "Venus");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Venus"));
  test.event("click", jupiterFn, () => detailsBoxNameFieldFn().value === "Jupiter");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Jupiter"));
  test.event("click", saturnFn, () => detailsBoxNameFieldFn().value === "Saturn");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Saturn"));
  test.start();
});

it("On double click with full request", function (done) {
  const onDblclickFullRequestFn = elementByIdFn("page:mainForm:changeExample::2");
  const onClickAjaxPopupFn = elementByIdFn("page:mainForm:changeExample::3");
  const detailsBoxFn = elementByIdFn("page:mainForm:detail");
  const detailsBoxNameFieldFn = elementByIdFn("page:mainForm:name::field");
  const sunFn = elementByIdFn("page:mainForm:s1:0:sample2");
  const venusFn = elementByIdFn("page:mainForm:s1:2:sample2");
  const jupiterFn = elementByIdFn("page:mainForm:s1:5:sample2");
  const saturnFn = elementByIdFn("page:mainForm:s1:6:sample2");

  const test = new JasmineTestTool(done);
  test.setup(() => detailsBoxFn() === null,
      () => onClickAjaxPopupFn().checked = true, "change", onClickAjaxPopupFn);
  test.setup(() => detailsBoxFn() !== null,
      () => onDblclickFullRequestFn().checked = true, "change", onDblclickFullRequestFn);
  test.setup(() => detailsBoxNameFieldFn() !== null && detailsBoxNameFieldFn().value === "Sun",
      null, "dblclick", sunFn);
  test.event("dblclick", venusFn, () => detailsBoxNameFieldFn().value === "Venus");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Venus"));
  test.event("dblclick", jupiterFn, () => detailsBoxNameFieldFn().value === "Jupiter");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Jupiter"));
  test.event("dblclick", saturnFn, () => detailsBoxNameFieldFn().value === "Saturn");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Saturn"));
  test.start();
});

it("Open popup on click with ajax", function (done) {
  const onClickAjaxFn = elementByIdFn("page:mainForm:changeExample::0");
  const onClickAjaxPopupFn = elementByIdFn("page:mainForm:changeExample::3");
  const detailsBoxFn = elementByIdFn("page:mainForm:detail");
  const venusFn = elementByIdFn("page:mainForm:s1:2:sample3");
  const jupiterFn = elementByIdFn("page:mainForm:s1:5:sample3");
  const saturnFn = elementByIdFn("page:mainForm:s1:6:sample3");
  const bodyFn = querySelectorFn("body");
  const backdropFn = querySelectorFn(".modal-backdrop.fade.show")
  const reloadableFn = elementByIdFn("page:mainForm:reloadable");
  const popupFn = elementByIdFn("page:mainForm:popup");
  const popupNameFieldFn = elementByIdFn("page:mainForm:popup:popupName::field");
  const cancelFn = elementByIdFn("page:mainForm:popup:cancel");

  let shownEventCount = 0;
  let hiddenEventCount = 0;
  reloadableFn().addEventListener("shown.bs.modal", () => shownEventCount++);
  reloadableFn().addEventListener("hidden.bs.modal", () => hiddenEventCount++);

  const test = new JasmineTestTool(done);
  test.setup(() => detailsBoxFn() !== null,
      () => onClickAjaxFn().checked = true, "change", onClickAjaxFn);
  test.setup(() => detailsBoxFn() === null,
      () => onClickAjaxPopupFn().checked = true, "change", onClickAjaxPopupFn);

  test.do(() => shownEventCount = 0);
  test.event("click", venusFn, () => shownEventCount > 0);
  test.do(() => expect(popupNameFieldFn().value).toBe("Venus"));
  test.do(() => expect(bodyFn().classList.contains("modal-open")).toBeTrue());
  test.do(() => expect(backdropFn()).not.toBeNull());
  test.do(() => expect(popupFn().classList.contains("show")).toBeTrue());

  test.do(() => hiddenEventCount = 0);
  test.event("click", cancelFn, () => hiddenEventCount > 0);
  test.do(() => expect(bodyFn().classList.contains("modal-open")).toBeFalse());
  test.do(() => expect(backdropFn()).toBeNull());
  test.do(() => expect(popupFn().classList.contains("show")).toBeFalse());

  test.do(() => shownEventCount = 0);
  test.event("click", jupiterFn, () => shownEventCount > 0);
  test.do(() => expect(popupNameFieldFn().value).toBe("Jupiter"));
  test.do(() => expect(bodyFn().classList.contains("modal-open")).toBeTrue());
  test.do(() => expect(backdropFn()).not.toBeNull());
  test.do(() => expect(popupFn().classList.contains("show")).toBeTrue());

  test.do(() => hiddenEventCount = 0);
  test.event("click", cancelFn, () => hiddenEventCount > 0);
  test.do(() => expect(bodyFn().classList.contains("modal-open")).toBeFalse());
  test.do(() => expect(backdropFn()).toBeNull());
  test.do(() => expect(popupFn().classList.contains("show")).toBeFalse());

  test.do(() => shownEventCount = 0);
  test.event("click", saturnFn, () => shownEventCount > 0);
  test.do(() => expect(popupNameFieldFn().value).toBe("Saturn"));
  test.do(() => expect(bodyFn().classList.contains("modal-open")).toBeTrue());
  test.do(() => expect(backdropFn()).not.toBeNull());
  test.do(() => expect(popupFn().classList.contains("show")).toBeTrue());

  test.do(() => hiddenEventCount = 0);
  test.event("click", cancelFn, () => hiddenEventCount > 0);
  test.do(() => expect(bodyFn().classList.contains("modal-open")).toBeFalse());
  test.do(() => expect(backdropFn()).toBeNull());
  test.do(() => expect(popupFn().classList.contains("show")).toBeFalse());
  test.start();
});

it("On double click with ajax", function (done) {
  const onClickAjaxPopupFn = elementByIdFn("page:mainForm:changeExample::3");
  const onDblclickAjaxFn = elementByIdFn("page:mainForm:changeExample::4");
  const detailsBoxFn = elementByIdFn("page:mainForm:detail");
  const detailsBoxNameFieldFn = elementByIdFn("page:mainForm:name::field");
  const sunFn = elementByIdFn("page:mainForm:s1:0:sample4");
  const venusFn = elementByIdFn("page:mainForm:s1:2:sample4");
  const jupiterFn = elementByIdFn("page:mainForm:s1:5:sample4");
  const saturnFn = elementByIdFn("page:mainForm:s1:6:sample4");

  const test = new JasmineTestTool(done);
  test.setup(() => detailsBoxFn() === null,
      () => onClickAjaxPopupFn().checked = true, "change", onClickAjaxPopupFn);
  test.setup(() => detailsBoxFn() !== null,
      () => onDblclickAjaxFn().checked = true, "change", onDblclickAjaxFn);
  test.setup(() => detailsBoxNameFieldFn() !== null && detailsBoxNameFieldFn().value === "Sun",
      null, "dblclick", sunFn);
  test.event("dblclick", venusFn, () => detailsBoxNameFieldFn().value === "Venus");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Venus"));
  test.event("dblclick", jupiterFn, () => detailsBoxNameFieldFn().value === "Jupiter");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Jupiter"));
  test.event("dblclick", saturnFn, () => detailsBoxNameFieldFn().value === "Saturn");
  test.do(() => expect(detailsBoxNameFieldFn().value).toBe("Saturn"));
  test.start();
});
