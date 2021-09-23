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

it("single: select Music, select Mathematics", function (done) {
  let radiosFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=radio]");
  let checkboxesFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=checkbox]");
  let selectableNoneFn = elementByIdFn("page:mainForm:selectable::0");
  let selectableSingleFn = elementByIdFn("page:mainForm:selectable::1");
  let musicFn = elementByIdFn("page:mainForm:categoriesTree:3:select");
  let mathematicsFn = elementByIdFn("page:mainForm:categoriesTree:9:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => radiosFn().length === 0 && checkboxesFn().length === 0,
      () => selectableNoneFn().checked = true,
      "change", selectableNoneFn);
  test.do(() => expect(radiosFn().length).toEqual(0));
  test.do(() => expect(checkboxesFn().length).toEqual(0));

  test.do(() => selectableSingleFn().checked = true);
  test.event("change", selectableSingleFn, () => radiosFn().length > 0);
  test.do(() => expect(radiosFn().length).toBeGreaterThan(0));

  test.do(() => musicFn().checked = true);
  test.event("change", musicFn, () => outputFn().textContent === "Music");
  test.do(() => expect(outputFn().textContent).toBe("Music"));

  test.do(() => mathematicsFn().checked = true);
  test.event("change", mathematicsFn, () => outputFn().textContent === "Mathematics");
  test.do(() => expect(outputFn().textContent).toBe("Mathematics"));
  test.start();
});

it("singleLeafOnly: select Classic, select Geography", function (done) {
  let radiosFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=radio]");
  let checkboxesFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=checkbox]");
  let selectableNoneFn = elementByIdFn("page:mainForm:selectable::0");
  let selectableSingleLeafOnlyFn = elementByIdFn("page:mainForm:selectable::2");
  let classicFn = elementByIdFn("page:mainForm:categoriesTree:4:select");
  let geographyFn = elementByIdFn("page:mainForm:categoriesTree:10:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => radiosFn().length === 0 && checkboxesFn().length === 0,
      () => selectableNoneFn().checked = true,
      "change", selectableNoneFn);
  test.do(() => expect(radiosFn().length).toEqual(0));
  test.do(() => expect(checkboxesFn().length).toEqual(0));

  test.do(() => selectableSingleLeafOnlyFn().checked = true);
  test.event("change", selectableSingleLeafOnlyFn, () => radiosFn().length > 0);
  test.do(() => expect(radiosFn().length).toBeGreaterThan(0));

  test.do(() => classicFn().checked = true);
  test.event("change", classicFn, () => outputFn().textContent === "Classic");
  test.do(() => expect(outputFn().textContent).toBe("Classic"));

  test.do(() => geographyFn().checked = true);
  test.event("change", geographyFn, () => outputFn().textContent === "Geography");
  test.do(() => expect(outputFn().textContent).toBe("Geography"));
  test.start();
});

it("multi: select Music, select Geography, deselect Music", function (done) {
  let radiosFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=radio]");
  let checkboxesFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=checkbox]");
  let selectableNoneFn = elementByIdFn("page:mainForm:selectable::0");
  let selectableMultiFn = elementByIdFn("page:mainForm:selectable::3");
  let musicFn = elementByIdFn("page:mainForm:categoriesTree:3:select");
  let geographyFn = elementByIdFn("page:mainForm:categoriesTree:10:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => radiosFn().length === 0 && checkboxesFn().length === 0,
      () => selectableNoneFn().checked = true,
      "change", selectableNoneFn);
  test.do(() => expect(radiosFn().length).toEqual(0));
  test.do(() => expect(checkboxesFn().length).toEqual(0));

  test.do(() => selectableMultiFn().checked = true);
  test.event("change", selectableMultiFn, () => checkboxesFn().length > 0);
  test.do(() => expect(checkboxesFn().length).toBeGreaterThan(0));

  test.do(() => musicFn().checked = true);
  test.event("change", musicFn, () => outputFn().textContent === "Music");
  test.do(() => expect(outputFn().textContent).toBe("Music"));

  test.do(() => geographyFn().checked = true);
  test.event("change", geographyFn, () => outputFn().textContent === "Music, Geography");
  test.do(() => expect(outputFn().textContent).toBe("Music, Geography"));

  test.do(() => musicFn().checked = false);
  test.event("change", musicFn, () => outputFn().textContent === "Geography");
  test.do(() => expect(outputFn().textContent).toBe("Geography"));
  test.start();
});

it("multiLeafOnly: select Classic, select Geography, deselect Classic", function (done) {
  let radiosFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=radio]");
  let checkboxesFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=checkbox]");
  let selectableNoneFn = elementByIdFn("page:mainForm:selectable::0");
  let selectableMultiLeafOnlyFn = elementByIdFn("page:mainForm:selectable::4");
  let classicFn = elementByIdFn("page:mainForm:categoriesTree:4:select");
  let geographyFn = elementByIdFn("page:mainForm:categoriesTree:10:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => radiosFn().length === 0 && checkboxesFn().length === 0,
      () => selectableNoneFn().checked = true,
      "change", selectableNoneFn);
  test.do(() => expect(radiosFn().length).toEqual(0));
  test.do(() => expect(checkboxesFn().length).toEqual(0));

  test.do(() => selectableMultiLeafOnlyFn().checked = true);
  test.event("change", selectableMultiLeafOnlyFn, () => checkboxesFn().length > 0);
  test.do(() => expect(checkboxesFn().length).toBeGreaterThan(0));

  test.do(() => classicFn().checked = true);
  test.event("change", classicFn, () => outputFn().textContent === "Classic");
  test.do(() => expect(outputFn().textContent).toBe("Classic"));

  test.do(() => geographyFn().checked = true);
  test.event("change", geographyFn, () => outputFn().textContent === "Classic, Geography");
  test.do(() => expect(outputFn().textContent).toBe("Classic, Geography"));

  test.do(() => classicFn().checked = false);
  test.event("change", classicFn, () => outputFn().textContent === "Geography");
  test.do(() => expect(outputFn().textContent).toBe("Geography"));
  test.start();
});

it("multiCascade: select Music, select Mathematics, deselect Classic", function (done) {
  let radiosFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=radio]");
  let checkboxesFn = querySelectorAllFn("#page\\:mainForm\\:categoriesTree input[type=checkbox]");
  let selectableNoneFn = elementByIdFn("page:mainForm:selectable::0");
  let selectableMultiCascadeFn = elementByIdFn("page:mainForm:selectable::5");
  let musicFn = elementByIdFn("page:mainForm:categoriesTree:3:select");
  let classicFn = elementByIdFn("page:mainForm:categoriesTree:4:select");
  let mathematicsFn = elementByIdFn("page:mainForm:categoriesTree:9:select");
  let outputFn = querySelectorFn("#page\\:mainForm\\:selectedNodesOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => radiosFn().length === 0 && checkboxesFn().length === 0,
      () => selectableNoneFn().checked = true,
      "change", selectableNoneFn);
  test.do(() => expect(radiosFn().length).toEqual(0));
  test.do(() => expect(checkboxesFn().length).toEqual(0));

  test.do(() => selectableMultiCascadeFn().checked = true);
  test.event("change", selectableMultiCascadeFn, () => checkboxesFn().length > 0);
  test.do(() => expect(checkboxesFn().length).toBeGreaterThan(0));

  test.do(() => musicFn().checked = true);
  test.event("change", musicFn, () => outputFn().textContent === "Music, Classic, Pop, World");
  test.do(() => expect(outputFn().textContent).toBe("Music, Classic, Pop, World"));

  test.do(() => mathematicsFn().checked = true);
  test.event("change", mathematicsFn, () => outputFn().textContent === "Music, Classic, Pop, World, Mathematics");
  test.do(() => expect(outputFn().textContent).toBe("Music, Classic, Pop, World, Mathematics"));

  test.do(() => classicFn().checked = false);
  test.event("change", classicFn, () => outputFn().textContent === "Music, Pop, World, Mathematics");
  test.do(() => expect(outputFn().textContent).toBe("Music, Pop, World, Mathematics"));
  test.start();
});
