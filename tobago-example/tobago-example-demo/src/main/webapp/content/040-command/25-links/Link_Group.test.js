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

it("vertical link group", function (done) {
  const topFn = elementByIdFn("page:mainForm:verticalGroupTop");
  const middleFn = elementByIdFn("page:mainForm:verticalGroupMiddle");
  const bottomFn = elementByIdFn("page:mainForm:verticalGroupBottom");

  const test = new JasmineTestTool(done);
  test.do(() => expect(topFn().offsetTop).toBeLessThan(middleFn().offsetTop));
  test.do(() => expect(middleFn().offsetTop).toBeLessThan(bottomFn().offsetTop));
  test.start();
});
