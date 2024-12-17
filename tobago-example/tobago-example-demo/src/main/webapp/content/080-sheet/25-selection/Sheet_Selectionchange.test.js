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

it("select 'Sun', select 'Venus', select all, deselect 'Mercury'", function (done) {
  const selectedRows = querySelectorFn("#page\\:mainForm\\:selectedRows .form-control-plaintext");
  const resetSelected = elementByIdFn("page:mainForm:resetSelected");
  const hiddenSelectedField = elementByIdFn("page:mainForm:sheet::selected");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:sheet::columnSelector']");
  const sun = querySelectorFn("tr[row-index='0']");
  const mercury = querySelectorFn("tr[row-index='1']");
  const venus = querySelectorFn("tr[row-index='2']");
  const earth = querySelectorFn("tr[row-index='3']");
  const firstPageButton = querySelectorFn("#page\\:mainForm\\:sheet [data-tobago-action*='first']");

  const test = new JasmineTestTool(done);
  if (firstPageButton() === null) {
    test.fail("firstPageButton button not found!");
  }
  test.setup(() => hiddenSelectedField().value === "[]", null, "click", resetSelected);
  test.setup(() => firstPageButton().disabled, null, "click", firstPageButton);

  test.do(() => expect(selectAllCheckbox().getAttribute("type")).toBe("checkbox"));
  test.do(() => expect(selectAllCheckbox().checked).toBe(false));

  test.event("click", sun, () => selectedRows().textContent === "0");
  test.do(() => expect(selectedRows().textContent).toBe("0"));
  test.do(() => expect(selectAllCheckbox().checked).toBe(false));

  test.event("click", venus, () => selectedRows().textContent === "0, 2");
  test.do(() => expect(selectedRows().textContent).toBe("0, 2"));
  test.do(() => expect(selectAllCheckbox().checked).toBe(false));

  test.event("click", selectAllCheckbox, () => selectedRows().textContent === "0, 2, 1, 3");
  test.do(() => expect(selectedRows().textContent).toBe("0, 2, 1, 3"));
  test.do(() => expect(selectAllCheckbox().checked).toBe(true));

  test.event("click", mercury, () => selectedRows().textContent === "0, 2, 3");
  test.do(() => expect(selectedRows().textContent).toBe("0, 2, 3"));
  test.do(() => expect(selectAllCheckbox().checked).toBe(false));
  test.start();
});
