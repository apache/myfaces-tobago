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

it("there must be no 'tobago-auto-spacing'", function () {
  const sheetAutoSpacings = querySelectorAllFn("tobago-sheet .tobago-auto-spacing")
  expect(sheetAutoSpacings().length).toBe(0);
});

it("main sheet: select all; sun sub sheet: select Mercury; venus sub sheet: select earth", function (done) {
  const reloadPage = querySelectorFn("page:mainForm:reloadPage");
  const selectAllCheckbox = querySelectorFn("input[name='page:mainForm:mainSheet::columnSelector']");
  const sunSheetMercuryRow = querySelectorFn("#page\\:mainForm\\:mainSheet\\:0\\:subSheet tr[row-index='1']");
  const venusSheetEarthRow = querySelectorFn("#page\\:mainForm\\:mainSheet\\:2\\:subSheet tr[row-index='3']");

  const test = new JasmineTestTool(done);
  test.setup(() => getMainSheetSelection().size === 0
          && getSunSubSheetSelection().size === 0
          && getVenusSubSheetSelection().size === 0,
      null, "click", reloadPage);

  test.do(() => expect(getMainSheetSelection().size).toBe(0));
  test.do(() => expect(getSunSubSheetSelection().size).toBe(0));
  test.do(() => expect(getVenusSubSheetSelection().size).toBe(0));

  test.event("click", selectAllCheckbox, () => getMainSheetSelection().size === 5);
  test.do(() => expect(getMainSheetSelection().has(0)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(1)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(2)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(3)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(4)).toBe(true));
  test.do(() => expect(getSunSubSheetSelection().size).toBe(0));
  test.do(() => expect(getVenusSubSheetSelection().size).toBe(0));

  test.event("click", sunSheetMercuryRow, () => getSunSubSheetSelection().has(1));
  test.do(() => expect(getMainSheetSelection().has(0)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(1)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(2)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(3)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(4)).toBe(true));
  test.do(() => expect(getSunSubSheetSelection().has(1)).toBe(true));
  test.do(() => expect(getVenusSubSheetSelection().size).toBe(0));

  test.event("click", venusSheetEarthRow, () => getVenusSubSheetSelection().has(3));
  test.do(() => expect(getMainSheetSelection().has(0)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(1)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(2)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(3)).toBe(true));
  test.do(() => expect(getMainSheetSelection().has(4)).toBe(true));
  test.do(() => expect(getSunSubSheetSelection().has(1)).toBe(true));
  test.do(() => expect(getVenusSubSheetSelection().has(3)).toBe(true));

  test.start();
});

function getMainSheetSelection() {
  const hiddenSelectedField = elementByIdFn("page:mainForm:mainSheet::selected");
  return new Set(JSON.parse(hiddenSelectedField().value));
}

function getSunSubSheetSelection() {
  const hiddenSelectedField = elementByIdFn("page:mainForm:mainSheet:0:subSheet::selected");
  return new Set(JSON.parse(hiddenSelectedField().value));
}

function getVenusSubSheetSelection() {
  const hiddenSelectedField = elementByIdFn("page:mainForm:mainSheet:2:subSheet::selected");
  return new Set(JSON.parse(hiddenSelectedField().value));
}
