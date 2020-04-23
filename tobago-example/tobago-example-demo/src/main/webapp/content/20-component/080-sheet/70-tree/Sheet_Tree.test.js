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

import {testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Collapse tree", function (done) {
  let row0nameFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:nameOut");
  let row0centralBodyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:centralBodyOut");
  let row0distanceFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:distanceOut");
  let row0periodFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:periodOut");
  let row0discovererFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:discovererOut");
  let row0yearFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:yearOut");
  let row1nameFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:nameOut");
  let row1centralBodyFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:centralBodyOut");
  let row1distanceFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:distanceOut");
  let row1periodFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:periodOut");
  let row1discovererFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:discovererOut");
  let row1yearFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:1\\:yearOut");
  let rootTreeButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:nameCol .tobago-treeNode-toggle");

  let test = new JasmineTestTool(done);
  test.do(() => expect(row0nameFn().textContent).toBe("Sun"));
  test.do(() => expect(row0centralBodyFn().textContent).toBe("-"));
  test.do(() => expect(row0distanceFn().textContent).toBe("0"));
  test.do(() => expect(row0periodFn().textContent).toBe("0.0"));
  test.do(() => expect(row0discovererFn().textContent).toBe("-"));
  test.do(() => expect(row0yearFn().textContent).toBe(""));
  test.do(() => expect(row1nameFn().textContent).toBe("Mercury"));
  test.do(() => expect(row1centralBodyFn().textContent).toBe("Sun"));
  test.do(() => expect(row1distanceFn().textContent).toBe("57910"));
  test.do(() => expect(row1periodFn().textContent).toBe("87.97"));
  test.do(() => expect(row1discovererFn().textContent).toBe("-"));
  test.do(() => expect(row1yearFn().textContent).toBe(""));
  let sheetRow = row1yearFn().parentElement.parentElement;
  test.do(() => expect(sheetRow.classList.contains("tobago-sheet-row")).toBe(true));
  test.do(() => expect(getComputedStyle(sheetRow).display).not.toBe("none"));
  test.do(() => rootTreeButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => row0nameFn() && row0nameFn().textContent === "Sun");
  test.do(() => expect(row0nameFn().textContent).toBe("Sun"));
  test.do(() => expect(row0centralBodyFn().textContent).toBe("-"));
  test.do(() => expect(row0distanceFn().textContent).toBe("0"));
  test.do(() => expect(row0periodFn().textContent).toBe("0.0"));
  test.do(() => expect(row0discovererFn().textContent).toBe("-"));
  test.do(() => expect(row0yearFn().textContent).toBe(""));
  sheetRow = row1yearFn().parentElement.parentElement;
  test.do(() => expect(sheetRow.classList.contains("tobago-sheet-row")).toBe(true));
  test.do(() => expect(getComputedStyle(sheetRow).display).toBe("none"));
  test.start();
});
