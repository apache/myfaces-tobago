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

import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("initial load", function (done) {
  const reset = elementByIdFn("page:mainForm:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => isLoaded(0) && isLoaded(49) && !isLoaded(50), null, "click", reset);
  test.do(() => expect(isLoaded(0)).toBeTrue());
  test.do(() => expect(isLoaded(49)).toBeTrue());
  test.do(() => expect(isLoaded(50)).toBeFalse());
  test.start();
});

it("focus row index 500 and scroll up", function (done) {
  const reset = elementByIdFn("page:mainForm:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => !isLoaded(425) && !isLoaded(500) && !isLoaded(538), null, "click", reset);
  test.do(() => focusRowIndex(500)); //new area; visible 500 to 513; range 475 to 538
  test.wait(() => isLoaded(500));
  test.do(() => expect(isLoaded(474)).toBeFalse());
  test.do(() => expect(isLoaded(475, 538)).toBeTrue());
  test.do(() => expect(isLoaded(539)).toBeFalse());

  test.do(() => focusRowIndex(493)); //discovered area; visible 493 to 503; range: 474 to 523
  test.wait(() => isLoaded(474));
  test.do(() => expect(isLoaded(424)).toBeFalse());
  test.do(() => expect(isLoaded(425, 538)).toBeTrue());
  test.do(() => expect(isLoaded(539)).toBeFalse());
  test.start();
});

it("focus row index 20, select row 1 and 60, then press 'Period'", function (done) {
  const row1 = querySelectorFn("tbody tr[row-index='1']");
  const row40 = querySelectorFn("tbody tr[row-index='60']");
  const selectedInput = elementByIdFn("page:mainForm:sheet::selected");
  const resetButton = elementByIdFn("page:mainForm:reset");
  const periodButton = elementByIdFn("page:mainForm:sheet:0:period");
  const selectedRows = querySelectorFn("#page\\:mainForm\\:selectedRows .form-control-plaintext");
  const actionCountOut = querySelectorFn("#page\\:mainForm\\:actionCount .form-control-plaintext");
  const actionListenerCountOut = querySelectorFn("#page\\:mainForm\\:actionListenerCount .form-control-plaintext");
  let actionCount = Number(actionCountOut().textContent);
  let actionListenerCount = Number(actionListenerCountOut().textContent);

  const test = new JasmineTestTool(done);
  test.setup(() => isLoaded(0) && !isLoaded(50) && selectedInput().value === "[]", null, "click", resetButton);
  test.do(() => focusRowIndex(23)); //visible: 23 to 33; range: 4 to 53;
  test.wait(() => isLoaded(50));
  test.do(() => expect(isLoaded(0, 99)).toBeTrue());
  test.do(() => expect(isLoaded(100)).toBeFalse());
  test.event("click", row1, () => selectedInput().value === "[1]");
  test.event("click", row40, () => selectedInput().value === "[1,60]");
  test.event("click", periodButton, () => Number(actionCountOut().textContent) > actionCount);
  test.do(() => expect(selectedRows().textContent).toBe("[1, 60]"));
  test.do(() => expect(Number(actionCountOut().textContent)).toBeGreaterThan(actionCount));
  test.do(() => expect(Number(actionListenerCountOut().textContent)).toBeGreaterThan(actionListenerCount));
  test.start();
});

it("focus row index 800, press 'Ajax'", function (done) {
  const ajax = elementByIdFn("page:mainForm:ajax");
  const reset = elementByIdFn("page:mainForm:reset");
  const timestampOut = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => !isLoaded(800), null, "click", reset);
  test.do(() => focusRowIndex(800)); //new area; visible 800 to 813; range 775 to 838
  test.wait(() => isLoaded(800));
  test.do(() => expect(isLoaded(774)).toBeFalse());
  test.do(() => expect(isLoaded(775, 838)).toBeTrue());
  test.do(() => expect(isLoaded(839)).toBeFalse());
  test.do(() => expect(getFirstVisibleRow()).toBe(800));

  test.do(() => timestamp = Number(timestampOut().textContent));
  test.event("click", ajax, () => Number(timestampOut().textContent) > timestamp);
  test.do(() => expect(isLoaded(774)).toBeFalse());
  test.do(() => expect(isLoaded(775, 849)).toBeTrue());
  test.do(() => expect(isLoaded(850)).toBeFalse());
  test.do(() => expect(getFirstVisibleRow()).toBe(800));
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
  querySelectorFn("#page\\:mainForm\\:sheet .tobago-body")().scrollTop = row(rowIndex).offsetTop;
}

function getFirstVisibleRow() {
  const tobagoBodyScrollTop = querySelectorFn("tobago-sheet .tobago-body")().scrollTop;
  const rows = querySelectorAllFn("tobago-sheet tr[row-index]")();

  let firstVisibleRow = 0;
  for (const row of rows) {
    if (row.offsetTop <= tobagoBodyScrollTop) {
      firstVisibleRow = Number(row.getAttribute("row-index"));
    } else {
      return firstVisibleRow;
    }
  }

  return -1;
}
