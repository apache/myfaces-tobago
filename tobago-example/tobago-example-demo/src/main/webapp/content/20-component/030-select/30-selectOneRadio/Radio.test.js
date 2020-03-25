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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("submit: Addition (2 + 4)", function (done) {
  let number1Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum2 input");
  let submitAddFn = querySelectorFn("#page\\:mainForm\\:submitAdd");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput span");

  const test = new JasmineTestTool(done);
  test.do(() => number1Fn().item(0).checked = false);
  test.do(() => number1Fn().item(1).checked = true); // Select 2
  test.do(() => number1Fn().item(2).checked = false);
  test.do(() => number2Fn().item(0).checked = false);
  test.do(() => number2Fn().item(1).checked = false);
  test.do(() => number2Fn().item(2).checked = true); // Select 4
  test.do(() => submitAddFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => outputFn() && outputFn().textContent === "6");
  test.do(() => expect(outputFn().textContent).toBe("6"));
  test.start();
});

it("submit: Subtraction (4 - 1)", function (done) {
  let number1Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = querySelectorAllFn("#page\\:mainForm\\:selectNum2 input");
  let submitSubFn = querySelectorFn("#page\\:mainForm\\:submitSub");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput span");

  const test = new JasmineTestTool(done);
  test.do(() => number1Fn().item(0).checked = false);
  test.do(() => number1Fn().item(1).checked = false);
  test.do(() => number1Fn().item(2).checked = true); // Select 4
  test.do(() => number2Fn().item(0).checked = true); // Select 1
  test.do(() => number2Fn().item(1).checked = false);
  test.do(() => number2Fn().item(2).checked = false);
  test.do(() => submitSubFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => outputFn() && outputFn().textContent === "3");
  test.do(() => expect(outputFn().textContent).toBe("3"));
  test.start();
});

it("ajax: select Mars", function (done) {
  let planetFn = querySelectorAllFn("#page\\:mainForm\\:selectPlanet input");
  let moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonradio label.form-check-label");

  const test = new JasmineTestTool(done);
  test.do(() => planetFn().item(0).checked = false);
  test.do(() => planetFn().item(2).checked = false);
  test.do(() => planetFn().item(1).checked = true); // Mars.
  test.do(() => planetFn().item(1).dispatchEvent(new Event("change", {bubbles: true})));
  test.wait(() => moonsFn()
      && moonsFn().item(0).textContent === "Phobos" && moonsFn().item(1).textContent === "Deimos");
  test.do(() => expect(moonsFn().item(0).textContent).toBe("Phobos"));
  test.do(() => expect(moonsFn().item(1).textContent).toBe("Deimos"));
  test.start();
});

it("ajax: select Jupiter", function (done) {
  let planetFn = querySelectorAllFn("#page\\:mainForm\\:selectPlanet input");
  let moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonradio label.form-check-label");

  const test = new JasmineTestTool(done);
  test.do(() => planetFn().item(0).checked = false);
  test.do(() => planetFn().item(1).checked = false);
  test.do(() => planetFn().item(2).checked = true); // Jupiter.
  test.do(() => planetFn().item(2).dispatchEvent(new Event("change", {bubbles: true})));
  test.wait(() => moonsFn() &&
      moonsFn().item(0).textContent === "Europa"
      && moonsFn().item(1).textContent === "Ganymed"
      && moonsFn().item(2).textContent === "Io"
      && moonsFn().item(3).textContent === "Kallisto"
  );
  test.do(() => expect(moonsFn().item(0).textContent).toBe("Europa"));
  test.do(() => expect(moonsFn().item(1).textContent).toBe("Ganymed"));
  test.do(() => expect(moonsFn().item(2).textContent).toBe("Io"));
  test.do(() => expect(moonsFn().item(3).textContent).toBe("Kallisto"));
  test.start()
});
