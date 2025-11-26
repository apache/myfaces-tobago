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

it("Select 'Bordered' move it to the right", function (done) {
  const unselectedFn = elementByIdFn("page:mainForm:shuttle::unselected");
  const unselectedOptionsFn = querySelectorAllFn(".tobago-unselected option");
  const selectedOptionsFn = querySelectorAllFn(".tobago-selected option");
  const addButtonFn = elementByIdFn("page:mainForm:shuttle::add");
  const removeAllButtonFn = elementByIdFn("page:mainForm:shuttle::removeAll");
  const outputFn = querySelectorFn("#page\\:mainForm\\:reloadCounter .form-control-plaintext");

  let counter;

  const test = new JasmineTestTool(done);
  test.setup(() => unselectedOptionsFn().length === 5, "click", removeAllButtonFn);
  test.do(() => counter = Number(outputFn().textContent));
  test.do(() => unselectedFn().selectedIndex = 2);
  test.do(() => unselectedFn().dispatchEvent(new Event("change", {bubbles: true})));
  test.event("click", addButtonFn, () => selectedOptionsFn().length > 0);
  test.do(() => expect(selectedOptionsFn().length).toBe(1));
  test.do(() => expect(selectedOptionsFn()[0].value).toBe("bordered"));
  test.do(() => expect(Number(outputFn().textContent)).toBe(counter + 1));
  test.start();
});
