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

it("Open popup and press 'Close'.", function (done) {
  let popupFn = elementByIdFn("page:mainForm:popup");
  let collapseFn = elementByIdFn("page:mainForm:popup::collapse");
  let openButtonFn = elementByIdFn("page:mainForm:showPopupButton");
  let closeButtonFn = elementByIdFn("page:mainForm:popup:hidePopupButton");

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
