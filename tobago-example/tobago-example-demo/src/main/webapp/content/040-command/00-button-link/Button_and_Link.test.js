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

it("Spaces between buttons", function (done) {
  const b1Spacing = elementByIdFn("page:mainForm:b1")().getBoundingClientRect();
  const b2Spacing = elementByIdFn("page:mainForm:b2")().getBoundingClientRect();
  const b4Spacing = elementByIdFn("page:mainForm:b4")().getBoundingClientRect();
  const b5Spacing = elementByIdFn("page:mainForm:b5")().getBoundingClientRect();

  const test = new JasmineTestTool(done);
  test.do(() => expect(b1Spacing.right < b2Spacing.left).toBeTrue());
  test.do(() => expect(b2Spacing.right < b4Spacing.left).toBeTrue());
  test.do(() => expect(b4Spacing.right < b5Spacing.left).toBeTrue());
  test.start();
});
