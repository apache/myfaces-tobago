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

function escapeClientId(clientId) {
  return '#' + clientId.replace(/([:\.])/g, '\\$1');
}

it("inputfield with label", function (done) {
  let test = new JasmineTestTool(done);

  function testValueEquals(id) {
    let fieldFn = querySelectorFn(escapeClientId(id));
    let labelFn = querySelectorFn("[for='" + id + "']");
    test.do(() => expect(fieldFn().value).toBe(labelFn().textContent));
  }

  testValueEquals("page:mainForm:direct::field");
  testValueEquals("page:mainForm:v1::field");
  testValueEquals("page:mainForm:v2::field");
  testValueEquals("page:mainForm:v3::field");
  testValueEquals("page:mainForm:v4::field");

  const testVuId = "page:mainForm:vu::field";
  let fieldVuFn = querySelectorFn(escapeClientId(testVuId));
  test.do(() => expect(fieldVuFn().value).toBe(""));
  test.start();
});

it("inputfield with label", function (done) {
  let test = new JasmineTestTool(done);

  function testValueEquals(id) {
    let fieldFn = querySelectorFn(escapeClientId(id));
    let labelFn = querySelectorFn("[for='" + id + "']");
    test.do(() => expect(fieldFn().id).toBe(labelFn().textContent));
  }

  testValueEquals("page:mainForm:my_number_1::field");
  testValueEquals("page:mainForm:my_number_3::field");
  test.start();
});
