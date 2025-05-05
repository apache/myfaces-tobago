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
  test.setup(() => isLoaded(0) && isLoaded(29) && !isLoaded(30), null, "click", reset);
  test.do(() => expect(isLoaded(0)).toBeTrue());
  test.do(() => expect(isLoaded(29)).toBeTrue());
  test.do(() => expect(isLoaded(30)).toBeFalse());
  test.start();
});

it("focus row index 500 and scroll up", function (done) {
  const reset = elementByIdFn("page:mainForm:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => !isLoaded(500), null, "click", reset);

  test.do(() => focusRowIndex(500)); //new area; rows 500 to 513 visible; range 485 to 528
  test.wait(() => isLoaded(500));
  test.do(() => expect(isLoaded(484)).toBeFalse());
  test.do(() => expect(isLoaded(485, 528)).toBeTrue());
  test.do(() => expect(isLoaded(529)).toBeFalse());

  test.do(() => focusRowIndex(494)); //discovered area: 494 to 504 visible; range 484 to 514
  test.wait(() => isLoaded(484));
  test.do(() => expect(isLoaded(454)).toBeFalse());
  test.do(() => expect(isLoaded(455, 528)).toBeTrue());
  test.do(() => expect(isLoaded(529)).toBeFalse());
  test.start();
});

it("scroll down, select row 1 and 40, then press 'Period'", function (done) {
  const row1 = querySelectorFn("tbody tr[row-index='1']");
  const row40 = querySelectorFn("tbody tr[row-index='40']");
  const selectedInput = elementByIdFn("page:mainForm:sheet::selected");
  const resetButton = elementByIdFn("page:mainForm:reset");
  const periodButton = elementByIdFn("page:mainForm:sheet:0:period");
  const selectedRows = querySelectorFn("#page\\:mainForm\\:selectedRows .form-control-plaintext");
  const actionCountOut = querySelectorFn("#page\\:mainForm\\:actionCount .form-control-plaintext");
  const actionListenerCountOut = querySelectorFn("#page\\:mainForm\\:actionListenerCount .form-control-plaintext");
  let actionCount = Number(actionCountOut().textContent);
  let actionListenerCount = Number(actionListenerCountOut().textContent);

  const test = new JasmineTestTool(done);
  test.setup(() => isLoaded(0) && !isLoaded(30) && selectedInput().value === "[]", null, "click", resetButton);
  test.do(() => focusRowIndex(12)); //discovered area: rows 12 to 21 visible; range 2 to 31
  test.wait(() => isLoaded(30));
  test.do(() => expect(isLoaded(0, 59)).toBeTrue());
  test.do(() => expect(isLoaded(60)).toBeFalse());
  test.event("click", row1, () => selectedInput().value === "[1]");
  test.event("click", row40, () => selectedInput().value === "[1,40]");
  test.event("click", periodButton, () => Number(actionCountOut().textContent) > actionCount);
  test.do(() => expect(selectedRows().textContent).toBe("[1, 40]"));
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
  test.do(() => focusRowIndex(800)); //new area: rows 800 to 813 visible; range 785 to 828
  test.wait(() => isLoaded(800));
  test.do(() => expect(isLoaded(784)).toBeFalse());
  test.do(() => expect(isLoaded(785, 828)).toBeTrue());
  test.do(() => expect(isLoaded(829)).toBeFalse());
  test.do(() => expect(getFirstVisibleRow()).toBe(800));

  test.do(() => timestamp = Number(timestampOut().textContent));
  test.event("click", ajax, () => Number(timestampOut().textContent) > timestamp);
  test.do(() => expect(isLoaded(785, 829)).toBeTrue()); //preloaded
  test.do(() => expect(isLoaded(830)).toBeFalse());
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
