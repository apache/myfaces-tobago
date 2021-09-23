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

it("set input value", function (done) {
  let test = new JasmineTestTool(done);

  function testValueEqualsLabel(inputSel, labelSel) {
    let fieldFn = querySelectorFn((inputSel));
    let labelFn = querySelectorFn(labelSel);
    test.do(() => expect(fieldFn().value).toBe(labelFn().textContent));
  }

  testValueEqualsLabel("#page\\:mainForm\\:direct\\:\\:field", "[for='page:mainForm:direct::field']");
  testValueEqualsLabel("#page\\:mainForm\\:v1\\:\\:field", "[for='page:mainForm:v1::field']");
  testValueEqualsLabel("#page\\:mainForm\\:v2\\:\\:field", "[for='page:mainForm:v2::field']");
  testValueEqualsLabel("#page\\:mainForm\\:v3\\:\\:field", "[for='page:mainForm:v3::field']");
  testValueEqualsLabel("#page\\:mainForm\\:v4\\:\\:field", "[for='page:mainForm:v4::field']");

  let fieldVuFn = querySelectorFn("#page\\:mainForm\\:vu\\:\\:field");
  test.do(() => expect(fieldVuFn().value).toBe(""));
  test.start();
});

it("set input id", function (done) {
  let test = new JasmineTestTool(done);

  function testIdEqualsLabel(inputSel, labelSel) {
    let fieldFn = querySelectorFn(inputSel);
    let labelFn = querySelectorFn(labelSel);
    test.do(() => expect(fieldFn().id).toBe(labelFn().textContent));
  }

  testIdEqualsLabel("#page\\:mainForm\\:my_number_1\\:\\:field", "[for='page:mainForm:my_number_1::field']");
  testIdEqualsLabel("#page\\:mainForm\\:my_number_3\\:\\:field", "[for='page:mainForm:my_number_3::field']");

  test.start();
});
