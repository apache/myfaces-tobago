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

it("Open the 'World' node to see 'Carib' and 'Africa'", function (done) {
  const reset = elementByIdFn("page:mainForm:reset")
  const treeNodes = querySelectorAllFn("#page\\:mainForm\\:sheet tobago-tree-node");
  const toggleWorld = querySelectorFn("#page\\:mainForm\\:sheet\\:6\\:nameCol .tobago-toggle");
  const spanWorld = querySelectorFn("#page\\:mainForm\\:sheet\\:6\\:nameOut span.form-control-plaintext");
  const spanCarib = querySelectorFn("#page\\:mainForm\\:sheet\\:7\\:nameOut span.form-control-plaintext");
  const spanAfrica = querySelectorFn("#page\\:mainForm\\:sheet\\:8\\:nameOut span.form-control-plaintext");

  const test = new JasmineTestTool(done);
  if (treeNodes().length !== 12) {
    test.fail("Must be a number of 12 tree nodes!");
  }
  if (reset() === null) {
    test.fail("reset button not found!");
  }
  if (toggleWorld() === null) {
    test.fail("toggleWorld not found!");
  }
  if (spanWorld() === null) {
    test.fail("spanWorld not found!");
  }
  test.setup(() => treeNodes().length === 12
          && spanWorld().textContent === "World"
          && spanCarib().textContent !== "Carib"
          && spanAfrica().textContent !== "Africa",
      null, "click", reset);
  test.do(() => expect(treeNodes().length).toEqual(12));
  test.do(() => expect(spanWorld().textContent).toEqual("World"));
  test.do(() => expect(spanCarib().textContent).not.toEqual("Carib"));
  test.do(() => expect(spanAfrica().textContent).not.toEqual("Africa"));
  test.event("click", toggleWorld, () => spanCarib().textContent === "Carib" && spanAfrica().textContent === "Africa");
  test.do(() => expect(treeNodes().length).toEqual(14));
  test.do(() => expect(spanWorld().textContent).toEqual("World"));
  test.do(() => expect(spanCarib().textContent).toEqual("Carib"));
  test.do(() => expect(spanAfrica().textContent).toEqual("Africa"));
  test.start();
});
