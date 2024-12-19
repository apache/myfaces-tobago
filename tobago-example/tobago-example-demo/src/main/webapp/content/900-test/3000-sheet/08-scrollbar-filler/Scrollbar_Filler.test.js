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

import {querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Markup: none", function (done) {
  const sheetHeaderCell = querySelectorFn("#page\\:mainForm\\:sheetMarkupNone header th");
  const scrollbarFiller = querySelectorFn("#page\\:mainForm\\:sheetMarkupNone .tobago-scrollbar-filler");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).backgroundColor).toEqual(getComputedStyle(scrollbarFiller()).backgroundColor));

  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderTopWidth).toEqual(getComputedStyle(scrollbarFiller()).borderTopWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftWidth).toEqual(getComputedStyle(scrollbarFiller()).borderLeftWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightWidth).toEqual(getComputedStyle(scrollbarFiller()).borderRightWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderBottomWidth).toEqual(getComputedStyle(scrollbarFiller()).borderBottomWidth));

  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderTopStyle).toEqual(getComputedStyle(scrollbarFiller()).borderTopStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftStyle).toEqual(getComputedStyle(scrollbarFiller()).borderLeftStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightStyle).toEqual(getComputedStyle(scrollbarFiller()).borderRightStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderBottomStyle).toEqual(getComputedStyle(scrollbarFiller()).borderBottomStyle));

  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderTopColor).toEqual(getComputedStyle(scrollbarFiller()).borderTopColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftColor).toEqual(getComputedStyle(scrollbarFiller()).borderLeftColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightColor).toEqual(getComputedStyle(scrollbarFiller()).borderRightColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderBottomColor).toEqual(getComputedStyle(scrollbarFiller()).borderBottomColor));
  test.start();
});

it("Markup: bordered", function (done) {
  const sheetHeaderRow = querySelectorFn("#page\\:mainForm\\:sheetMarkupBordered header tr");
  const sheetHeaderCell = querySelectorFn("#page\\:mainForm\\:sheetMarkupBordered header th");
  const scrollbarFiller = querySelectorFn("#page\\:mainForm\\:sheetMarkupBordered .tobago-scrollbar-filler");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).backgroundColor).toEqual(getComputedStyle(scrollbarFiller()).backgroundColor));

  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderTopWidth).toEqual(getComputedStyle(scrollbarFiller()).borderTopWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderBottomWidth).toEqual(getComputedStyle(scrollbarFiller()).borderBottomWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftWidth).toEqual(getComputedStyle(scrollbarFiller()).borderLeftWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightWidth).toEqual(getComputedStyle(scrollbarFiller()).borderRightWidth));

  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderTopStyle).toEqual(getComputedStyle(scrollbarFiller()).borderTopStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderBottomStyle).toEqual(getComputedStyle(scrollbarFiller()).borderBottomStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftStyle).toEqual(getComputedStyle(scrollbarFiller()).borderLeftStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightStyle).toEqual(getComputedStyle(scrollbarFiller()).borderRightStyle));

  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderTopColor).toEqual(getComputedStyle(scrollbarFiller()).borderTopColor));
  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderBottomColor).toEqual(getComputedStyle(scrollbarFiller()).borderBottomColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftColor).toEqual(getComputedStyle(scrollbarFiller()).borderLeftColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightColor).toEqual(getComputedStyle(scrollbarFiller()).borderRightColor));
  test.start();
});

it("Markup: dark", function (done) {
  const sheetHeaderCell = querySelectorFn("#page\\:mainForm\\:sheetMarkupDark header th");
  const scrollbarFiller = querySelectorFn("#page\\:mainForm\\:sheetMarkupDark .tobago-scrollbar-filler");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).backgroundColor).toEqual(getComputedStyle(scrollbarFiller()).backgroundColor));

  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderTopWidth).toEqual(getComputedStyle(scrollbarFiller()).borderTopWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftWidth).toEqual(getComputedStyle(scrollbarFiller()).borderLeftWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightWidth).toEqual(getComputedStyle(scrollbarFiller()).borderRightWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderBottomWidth).toEqual(getComputedStyle(scrollbarFiller()).borderBottomWidth));

  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderTopStyle).toEqual(getComputedStyle(scrollbarFiller()).borderTopStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftStyle).toEqual(getComputedStyle(scrollbarFiller()).borderLeftStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightStyle).toEqual(getComputedStyle(scrollbarFiller()).borderRightStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderBottomStyle).toEqual(getComputedStyle(scrollbarFiller()).borderBottomStyle));

  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderTopColor).toEqual(getComputedStyle(scrollbarFiller()).borderTopColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftColor).toEqual(getComputedStyle(scrollbarFiller()).borderLeftColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightColor).toEqual(getComputedStyle(scrollbarFiller()).borderRightColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderBottomColor).toEqual(getComputedStyle(scrollbarFiller()).borderBottomColor));
  test.start();
});

it("Markup: dark bordered", function (done) {
  const sheetHeaderRow = querySelectorFn("#page\\:mainForm\\:sheetMarkupDarkBordered header tr");
  const sheetHeaderCell = querySelectorFn("#page\\:mainForm\\:sheetMarkupDarkBordered header th");
  const scrollbarFiller = querySelectorFn("#page\\:mainForm\\:sheetMarkupDarkBordered .tobago-scrollbar-filler");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).backgroundColor).toEqual(getComputedStyle(scrollbarFiller()).backgroundColor));

  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderTopWidth).toEqual(getComputedStyle(scrollbarFiller()).borderTopWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderBottomWidth).toEqual(getComputedStyle(scrollbarFiller()).borderBottomWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftWidth).toEqual(getComputedStyle(scrollbarFiller()).borderLeftWidth));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightWidth).toEqual(getComputedStyle(scrollbarFiller()).borderRightWidth));

  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderTopStyle).toEqual(getComputedStyle(scrollbarFiller()).borderTopStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderBottomStyle).toEqual(getComputedStyle(scrollbarFiller()).borderBottomStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftStyle).toEqual(getComputedStyle(scrollbarFiller()).borderLeftStyle));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightStyle).toEqual(getComputedStyle(scrollbarFiller()).borderRightStyle));

  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderTopColor).toEqual(getComputedStyle(scrollbarFiller()).borderTopColor));
  test.do(() => expect(getComputedStyle(sheetHeaderRow()).borderBottomColor).toEqual(getComputedStyle(scrollbarFiller()).borderBottomColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderLeftColor).toEqual(getComputedStyle(scrollbarFiller()).borderLeftColor));
  test.do(() => expect(getComputedStyle(sheetHeaderCell()).borderRightColor).toEqual(getComputedStyle(scrollbarFiller()).borderRightColor));
  test.start();
});
