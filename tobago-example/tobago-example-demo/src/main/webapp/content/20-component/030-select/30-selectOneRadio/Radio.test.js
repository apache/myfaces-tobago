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

it("submit: Addition (2 + 4)", function (done) {
  let number1Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum2 input");
  let submitAddFn = querySelectorFn("#page\\:mainForm\\:submitAdd");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.do(() => number1Fn().item(0).checked = false);
  test.do(() => number1Fn().item(1).checked = true); // Select 2
  test.do(() => number1Fn().item(2).checked = false);
  test.do(() => number2Fn().item(0).checked = false);
  test.do(() => number2Fn().item(1).checked = false);
  test.do(() => number2Fn().item(2).checked = true); // Select 4
  test.event("click", submitAddFn, () => outputFn() && outputFn().textContent === "6");
  test.do(() => expect(outputFn().textContent).toBe("6"));
  test.start();
});

it("submit: Subtraction (4 - 1)", function (done) {
  let number1Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum2 input");
  let submitSubFn = querySelectorFn("#page\\:mainForm\\:submitSub");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.do(() => number1Fn().item(0).checked = false);
  test.do(() => number1Fn().item(1).checked = false);
  test.do(() => number1Fn().item(2).checked = true); // Select 4
  test.do(() => number2Fn().item(0).checked = true); // Select 1
  test.do(() => number2Fn().item(1).checked = false);
  test.do(() => number2Fn().item(2).checked = false);
  test.event("click", submitSubFn, () => outputFn() && outputFn().textContent === "3");
  test.do(() => expect(outputFn().textContent).toBe("3"));
  test.start();
});

it("ajax: select Mars", function (done) {
  const earthFn = elementByIdFn("page:mainForm:selectPlanet::0");
  const marsFn = elementByIdFn("page:mainForm:selectPlanet::1");
  const jupiterFn = elementByIdFn("page:mainForm:selectPlanet::2");
  const moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonradio .form-check-label");

  const test = new JasmineTestTool(done);
  test.do(() => earthFn().checked = false);
  test.do(() => marsFn().checked = true);
  test.do(() => jupiterFn().checked = false);
  test.event("change", marsFn, () => moonsFn() && moonsFn()[0].textContent === "Phobos");
  test.do(() => expect(moonsFn()[0].textContent).toBe("Phobos"));
  test.do(() => expect(moonsFn()[1].textContent).toBe("Deimos"));
  test.start();
});

it("ajax: select Jupiter", function (done) {
  const earthFn = elementByIdFn("page:mainForm:selectPlanet::0");
  const marsFn = elementByIdFn("page:mainForm:selectPlanet::1");
  const jupiterFn = elementByIdFn("page:mainForm:selectPlanet::2");
  const moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonradio .form-check-label");

  const test = new JasmineTestTool(done);
  test.do(() => earthFn().checked = false);
  test.do(() => marsFn().checked = false);
  test.do(() => jupiterFn().checked = true);
  test.event("change", jupiterFn, () => moonsFn() && moonsFn()[0].textContent === "Europa");
  test.do(() => expect(moonsFn()[0].textContent).toBe("Europa"));
  test.do(() => expect(moonsFn()[1].textContent).toBe("Ganymed"));
  test.do(() => expect(moonsFn()[2].textContent).toBe("Io"));
  test.do(() => expect(moonsFn()[3].textContent).toBe("Kallisto"));
  test.start()
});
