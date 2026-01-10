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

it("Execute 'AJAX' entry in dropdown menu", function (done) {
  let dropdownButtonFn = elementByIdFn("page:mainForm:dropdownButton::command");
  let dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name=page\\:mainForm\\:dropdownButton]");
  let dropdownAjaxEntryFn = elementByIdFn("page:mainForm:ajaxEntry");
  let inputFn = elementByIdFn("page:mainForm:inputAjax::field");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputAjax .form-control-plaintext");

  const test = new JasmineTestTool(done);
  // no test.setup() because controller is @RequestScoped
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));
  test.do(() => expect(outputFn().textContent).toBe(""));

  test.event("click", dropdownButtonFn, () => dropdownMenuFn().classList.contains("show"));
  test.do(() => expect(dropdownMenuFn().classList).toContain("show"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList).toContain("tobago-page-menuStore"));

  test.do(() => inputFn().value = "Tobago");
  test.event("click", dropdownAjaxEntryFn, () => outputFn().textContent === "Tobago");
  test.do(() => expect(outputFn().textContent).toBe("Tobago"));
  test.do(() => expect(dropdownMenuFn().classList)
      .not.toContain("show", "dropdown menu should be closed after menu entry is clicked"));

  const pageOverlays = querySelectorAllFn("tobago-overlay");
  test.do(() => expect(pageOverlays().length).toBe(0, "there must be no tobago-overlay"));
  test.start();
});
