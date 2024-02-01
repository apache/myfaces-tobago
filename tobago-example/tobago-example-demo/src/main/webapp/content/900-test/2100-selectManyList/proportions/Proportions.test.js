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

it("Compare to height of tc:in", function (done) {
  const inSmall = elementByIdFn("page:mainForm:inSmall");
  const selectOneChoiceSmall = elementByIdFn("page:mainForm:selectOneChoiceSmall");
  const selectOneListSmall = elementByIdFn("page:mainForm:selectOneListSmall");
  const selectManyListSmall = elementByIdFn("page:mainForm:selectManyListSmall");
  const inNormal = elementByIdFn("page:mainForm:inNormal");
  const selectOneChoiceNormal = elementByIdFn("page:mainForm:selectOneChoiceNormal");
  const selectOneListNormal = elementByIdFn("page:mainForm:selectOneListNormal");
  const selectManyListNormal = elementByIdFn("page:mainForm:selectManyListNormal");
  const inLarge = elementByIdFn("page:mainForm:inLarge");
  const selectOneChoiceLarge = elementByIdFn("page:mainForm:selectOneChoiceLarge");
  const selectOneListLarge = elementByIdFn("page:mainForm:selectOneListLarge");
  const selectManyListLarge = elementByIdFn("page:mainForm:selectManyListLarge");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(selectOneChoiceSmall()).height).toBe(getComputedStyle(inSmall()).height));
  test.do(() => expect(getComputedStyle(selectOneListSmall()).height).toBe(getComputedStyle(inSmall()).height));
  test.do(() => expect(getComputedStyle(selectManyListSmall()).height).toBe(getComputedStyle(inSmall()).height));
  test.do(() => expect(getComputedStyle(selectOneChoiceNormal()).height).toBe(getComputedStyle(inNormal()).height));
  test.do(() => expect(getComputedStyle(selectOneListNormal()).height).toBe(getComputedStyle(inNormal()).height));
  test.do(() => expect(getComputedStyle(selectManyListNormal()).height).toBe(getComputedStyle(inNormal()).height));
  test.do(() => expect(getComputedStyle(selectOneChoiceLarge()).height).toBe(getComputedStyle(inLarge()).height));
  test.do(() => expect(getComputedStyle(selectOneListLarge()).height).toBe(getComputedStyle(inLarge()).height));
  test.do(() => expect(getComputedStyle(selectManyListLarge()).height).toBe(getComputedStyle(inLarge()).height));
  test.start();
});

it("Compare to height of tc:selectOneChoice", function (done) {
  const inSmall = elementByIdFn("page:mainForm:inSmall");
  const selectOneChoiceSmall = elementByIdFn("page:mainForm:selectOneChoiceSmall");
  const selectOneListSmall = elementByIdFn("page:mainForm:selectOneListSmall");
  const selectManyListSmall = elementByIdFn("page:mainForm:selectManyListSmall");
  const inNormal = elementByIdFn("page:mainForm:inNormal");
  const selectOneChoiceNormal = elementByIdFn("page:mainForm:selectOneChoiceNormal");
  const selectOneListNormal = elementByIdFn("page:mainForm:selectOneListNormal");
  const selectManyListNormal = elementByIdFn("page:mainForm:selectManyListNormal");
  const inLarge = elementByIdFn("page:mainForm:inLarge");
  const selectOneChoiceLarge = elementByIdFn("page:mainForm:selectOneChoiceLarge");
  const selectOneListLarge = elementByIdFn("page:mainForm:selectOneListLarge");
  const selectManyListLarge = elementByIdFn("page:mainForm:selectManyListLarge");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(inSmall()).height).toBe(getComputedStyle(selectOneChoiceSmall()).height));
  test.do(() => expect(getComputedStyle(selectOneListSmall()).height).toBe(getComputedStyle(selectOneChoiceSmall()).height));
  test.do(() => expect(getComputedStyle(selectManyListSmall()).height).toBe(getComputedStyle(selectOneChoiceSmall()).height));
  test.do(() => expect(getComputedStyle(inNormal()).height).toBe(getComputedStyle(selectOneChoiceNormal()).height));
  test.do(() => expect(getComputedStyle(selectOneListNormal()).height).toBe(getComputedStyle(selectOneChoiceNormal()).height));
  test.do(() => expect(getComputedStyle(selectManyListNormal()).height).toBe(getComputedStyle(selectOneChoiceNormal()).height));
  test.do(() => expect(getComputedStyle(inLarge()).height).toBe(getComputedStyle(selectOneChoiceLarge()).height));
  test.do(() => expect(getComputedStyle(selectOneListLarge()).height).toBe(getComputedStyle(selectOneChoiceLarge()).height));
  test.do(() => expect(getComputedStyle(selectManyListLarge()).height).toBe(getComputedStyle(selectOneChoiceLarge()).height));
  test.start();
});
