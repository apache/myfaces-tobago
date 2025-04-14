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
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("deselection if required=false", function (done) {
  const hiddenSelect = elementByIdFn("page:mainForm:selectOneList::field");
  const spanSelected = querySelectorFn("#page\\:mainForm\\:selectOneList\\:\\:selectField span");
  const saturnRow = querySelectorFn(".tobago-options[name='page:mainForm:selectOneList'] tr[data-tobago-value='Saturn']");
  const output = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  const submit = elementByIdFn("page:mainForm:submit");
  const reset = elementByIdFn("page:mainForm:reset");
  const requiredCheckbox = elementByIdFn("page:mainForm:requiredCheckbox::field");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenSelect().required === false,
      () => requiredCheckbox().checked = false, "change", requiredCheckbox);
  test.setup(() => spanSelected().textContent === "" && output().textContent === "", null, "click", reset);
  test.do(() => expect(hiddenSelect().required).toBeFalse());
  test.do(() => expect(spanSelected().textContent).toEqual(""));
  test.do(() => expect(output().textContent).toEqual(""));

  test.do(() => clickRow(saturnRow));
  test.waitMs(1000);
  test.do(() => expect(spanSelected().textContent).toEqual("Saturn"));
  test.do(() => expect(output().textContent).toEqual(""));

  test.do(() => clickRow(saturnRow));
  test.waitMs(1000);
  test.do(() => expect(spanSelected().textContent).toEqual(""));
  test.do(() => expect(output().textContent).toEqual(""));

  test.do(() => timestamp = currentTimestamp());
  test.event("click", submit, () => currentTimestamp() > timestamp);
  test.do(() => expect(spanSelected().textContent).toEqual(""));
  test.do(() => expect(output().textContent).toEqual(""));
  test.start();
});

it("deselection if required=true", function (done) {
  const hiddenSelect = elementByIdFn("page:mainForm:selectOneList::field");
  const spanSelected = querySelectorFn("#page\\:mainForm\\:selectOneList\\:\\:selectField span");
  const jupiterRow = querySelectorFn(".tobago-options[name='page:mainForm:selectOneList'] tr[data-tobago-value='Jupiter']");
  const output = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  const submit = elementByIdFn("page:mainForm:submit");
  const reset = elementByIdFn("page:mainForm:reset");
  const requiredCheckbox = elementByIdFn("page:mainForm:requiredCheckbox::field");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenSelect().required === true,
      () => requiredCheckbox().checked = true, "change", requiredCheckbox);
  test.setup(() => spanSelected().textContent === "" && output().textContent === "", null, "click", reset);
  test.do(() => expect(hiddenSelect().required).toBeTrue());
  test.do(() => expect(spanSelected().textContent).toEqual(""));
  test.do(() => expect(output().textContent).toEqual(""));

  test.do(() => clickRow(jupiterRow));
  test.waitMs(1000);
  test.do(() => expect(spanSelected().textContent).toEqual("Jupiter"));
  test.do(() => expect(output().textContent).toEqual(""));

  test.do(() => clickRow(jupiterRow));
  test.waitMs(1000);
  test.do(() => expect(spanSelected().textContent).toEqual("Jupiter"));
  test.do(() => expect(output().textContent).toEqual(""));

  test.do(() => timestamp = currentTimestamp());
  test.event("click", submit, () => currentTimestamp() > timestamp);
  test.do(() => expect(spanSelected().textContent).toEqual("Jupiter"));
  test.do(() => expect(output().textContent).toEqual("Jupiter"));
  test.start();
});

function clickRow(row) {
  row().dispatchEvent(new Event("click", {bubbles: true}))
}

function currentTimestamp() {
  const timestampOut = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  return Number(timestampOut().textContent);
}
