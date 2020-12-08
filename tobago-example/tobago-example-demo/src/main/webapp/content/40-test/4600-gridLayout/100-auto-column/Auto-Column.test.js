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

it("not implemented yet", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});

/*
import {querySelectorFn} from "/script/tobago-test.js";

QUnit.test("test width of grid layout and 'auto' button", function (assert) {
  assert.expect(2);

  let gridLayoutFn = querySelectorFn("#page\\:mainForm\\:grid");
  let buttonAutoFn = querySelectorFn("#page\\:mainForm\\:buttonAuto");

  assert.equal(gridLayoutFn().offsetWidth, 358);
  assert.equal(buttonAutoFn().offsetWidth, 58);
});
*/
