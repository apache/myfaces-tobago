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

it("Select 2,2,0 and submit", function (done) {
  const hiddenInput = elementByIdFn("page:mainForm:listbox::selected");
  const submit = elementByIdFn("page:mainForm:submit");
  const output = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  const node1 = elementByIdFn("page:mainForm:listbox:1:node");
  const node3 = elementByIdFn("page:mainForm:listbox:3:node"); // 2
  const node6 = elementByIdFn("page:mainForm:listbox:6:node"); // 2,2
  const node7 = elementByIdFn("page:mainForm:listbox:7:node"); // 2,2,0

  const test = new JasmineTestTool(done);
  test.setup(() => isLevelSelectVisible(2, 1),
      () => node1().selected = true,
      "change", node1);
  test.setup(() => output().textContent !== "[[2, 2, 0]]",
      null, "click", submit);

  test.do(() => node3().selected = true);
  test.event("change", node3,
      () => hiddenInput().value === "[2]" && isLevelSelectVisible(2, 2));

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeFalse());

  test.do(() => node6().selected = true);
  test.event("change", node6,
      () => hiddenInput().value === "[2,2]" && isLevelSelectVisible(3, 2));

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeFalse());

  test.do(() => node7().selected = true);
  test.event("change", node7, () => hiddenInput().value === "[2,2,0]");

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeFalse());

  test.event("click", submit, () => output().textContent === "[[2, 2, 0]]");
  test.do(() => expect(output().textContent).toBe("[[2, 2, 0]]"));

  test.start();
});


it("Select 3 and submit", function (done) {
  const hiddenInput = elementByIdFn("page:mainForm:listbox::selected");
  const submit = elementByIdFn("page:mainForm:submit");
  const output = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  const node1 = elementByIdFn("page:mainForm:listbox:1:node");
  const node9 = elementByIdFn("page:mainForm:listbox:9:node"); // 3

  const test = new JasmineTestTool(done);
  test.setup(() => isLevelSelectVisible(2, 1),
      () => node1().selected = true,
      "change", node1);
  test.setup(() => output().textContent !== "[[3]]",
      null, "click", submit);

  test.do(() => node9().selected = true);
  test.event("change", node9,
      () => hiddenInput().value === "[3]" && isLevelSelectVisible(2, 1));

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeFalse());

  test.event("click", submit, () => output().textContent === "[[3]]");
  test.do(() => expect(output().textContent).toBe("[[3]]"));

  test.start();
});

it("Select 4,2,1,1 and submit", function (done) {
  const hiddenInput = elementByIdFn("page:mainForm:listbox::selected");
  const submit = elementByIdFn("page:mainForm:submit");
  const output = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  const node1 = elementByIdFn("page:mainForm:listbox:1:node");
  const node10 = elementByIdFn("page:mainForm:listbox:10:node"); // 4
  const node15 = elementByIdFn("page:mainForm:listbox:15:node"); // 4,2
  const node17 = elementByIdFn("page:mainForm:listbox:17:node"); // 4,2,1
  const node19 = elementByIdFn("page:mainForm:listbox:19:node"); // 4,2,1,1

  const test = new JasmineTestTool(done);
  test.setup(() => isLevelSelectVisible(2, 1),
      () => node1().selected = true,
      "change", node1);
  test.setup(() => output().textContent !== "[[4, 2, 1, 1]]",
      null, "click", submit);

  test.do(() => node10().selected = true);
  test.event("change", node10,
      () => hiddenInput().value === "[4]" && isLevelSelectVisible(2, 3));

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeFalse());

  test.do(() => node15().selected = true);
  test.event("change", node15,
      () => hiddenInput().value === "[4,2]" && isLevelSelectVisible(3, 4));

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeFalse());

  test.do(() => node17().selected = true);
  test.event("change", node17,
      () => hiddenInput().value === "[4,2,1]" && isLevelSelectVisible(4, 2));

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeTrue());

  test.do(() => node19().selected = true);
  test.event("change", node19, () => hiddenInput().value === "[4,2,1,1]");

  test.do(() => expect(isLevelSelectVisible(1, 1)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(2, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(2, 3)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(3, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 2)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 3)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(3, 4)).toBeTrue());
  test.do(() => expect(isLevelSelectVisible(4, 1)).toBeFalse());
  test.do(() => expect(isLevelSelectVisible(4, 2)).toBeTrue());

  test.event("click", submit, () => output().textContent === "[[4, 2, 1, 1]]");
  test.do(() => expect(output().textContent).toBe("[[4, 2, 1, 1]]"));

  test.start();
});

function isLevelSelectVisible(level, select) {
  const selectElement = querySelectorFn("#page\\:mainForm\\:listbox .tobago-level:nth-of-type("
      + level + ") .tobago-selected:nth-of-type(" + select + ")");
  return !selectElement().classList.contains("d-none");
}
