/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("TOBAGO-2349: Lazy sheet: tobago-behavior not initialized", function (done) {
  const selectAjax = elementByIdFn("page:mainForm:changeAjaxOrFsr::0");
  const selectMulti = elementByIdFn("page:mainForm:changeSelectable::2");
  const reset = elementByIdFn("page:mainForm:reset");
  const timestampElement = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const checkAjax = elementByIdFn("page:mainForm:checkAjax::field");
  const checkSelectable = elementByIdFn("page:mainForm:checkSelectable::field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxButton");
  const europaRow = elementByIdFn("page:mainForm:solar:18:rowAjax");
  const europaUpdateTimestamp = elementByIdFn("page:mainForm:solar:18:updateTimestamp");
  const name = elementByIdFn("page:mainForm:name::field");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => checkAjax().checked, () => selectAjax().checked = true, "change", selectAjax);
  test.setup(() => checkSelectable().value === "multi", () => selectMulti().checked = true, "change", selectMulti);
  test.setup(() => name().value === "Sun", null, "click", reset);

  test.do(() => focusRowIndex(18));
  test.do(() => expect(isLoaded(18, 67)).toBeTrue());
  test.do(() => timestamp = Number(timestampElement().textContent));
  test.event("click", ajaxButton, () => Number(timestampElement().textContent) > timestamp);
  test.wait(() => isLoaded(18, 67));
  test.event("click", europaRow, () => name().value === "Europa");
  test.do(() => expect(name().value).toBe("Europa"));
  test.do(() => timestamp = Number(timestampElement().textContent));
  test.event("click", europaUpdateTimestamp, () => Number(timestampElement().textContent) > timestamp);
  test.do(() => expect(Number(timestampElement().textContent) > timestamp).toBeTrue());

  test.start();
});

function isLoaded(startRow, endRow) {
  if (endRow === undefined) {
    endRow = startRow;
  }
  for (let i = startRow; i <= endRow; i++) {
    if (row(i).getAttribute("dummy") != null) {
      return false;
    }
  }
  return true;
}

function row(rowIndex) {
  return querySelectorFn("tr[row-index='" + rowIndex + "']")();
}

function focusRowIndex(rowIndex) {
  querySelectorFn("#page\\:mainForm\\:solar .tobago-body")().scrollTop = row(rowIndex).offsetTop;
}
