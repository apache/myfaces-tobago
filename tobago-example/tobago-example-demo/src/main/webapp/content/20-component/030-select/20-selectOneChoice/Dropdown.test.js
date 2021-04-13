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
import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("submit: Alice", function (done) {
  let aliceFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Alice']");
  let bobFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Bob']");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputPerson .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => () => outputFn().textContent !== "Alice Anderson",
      () => {
        aliceFn().selected = false;
        bobFn().selected = true;
      },
      "click", submitFn);
  test.do(() => aliceFn().selected = true);
  test.do(() => bobFn().selected = false);
  test.event("click", submitFn, () => outputFn().textContent === "Alice Anderson");
  test.do(() => expect(outputFn().textContent).toBe("Alice Anderson"));
  test.start();
});

it("submit: Bob", function (done) {
  let aliceFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Alice']");
  let bobFn = querySelectorFn("#page\\:mainForm\\:selectPerson\\:\\:field option[value^='Bob']");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputPerson .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => () => outputFn().textContent !== "Bob Brunch",
      () => {
        aliceFn().selected = true;
        bobFn().selected = false;
      },
      "click", submitFn);
  test.do(() => aliceFn().selected = false);
  test.do(() => bobFn().selected = true);
  test.event("click", submitFn, () => outputFn().textContent === "Bob Brunch");
  test.do(() => expect(outputFn().textContent).toBe("Bob Brunch"));
  test.start();
});

it("ajax: select Mars", function (done) {
  let planetFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field");
  let earthOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='0']");
  let marsOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='1']");
  let jupiterOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='2']");
  let moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  const test = new JasmineTestTool(done);
  test.setup(() => moonsFn().item(0).text !== "Phobos",
      () => {
        earthOptionFn().selected = true;
        marsOptionFn().selected = false;
        jupiterOptionFn().selected = false;
      },
      "change", planetFn);
  test.do(() => earthOptionFn().selected = false);
  test.do(() => marsOptionFn().selected = true);
  test.do(() => jupiterOptionFn().selected = false);
  test.event("change", planetFn, () => moonsFn().item(0).text === "Phobos");
  test.do(() => expect(moonsFn().item(0).text).toBe("Phobos"));
  test.do(() => expect(moonsFn().item(1).text).toBe("Deimos"));
  test.start();
});

it("ajax: select Jupiter", function (done) {
  let planetFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field");
  let earthOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='0']");
  let marsOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='1']");
  let jupiterOptionFn = querySelectorFn("#page\\:mainForm\\:selectPlanet\\:\\:field option[value='2']");
  let moonsFn = querySelectorAllFn("#page\\:mainForm\\:moonbox\\:\\:field option");

  const test = new JasmineTestTool(done);
  test.setup(() => moonsFn().item(0).text !== "Europa",
      () => {
        earthOptionFn().selected = true;
        marsOptionFn().selected = false;
        jupiterOptionFn().selected = false;
      },
      "change", planetFn);
  test.do(() => earthOptionFn().selected = false);
  test.do(() => marsOptionFn().selected = false);
  test.do(() => jupiterOptionFn().selected = true);
  test.event("change", planetFn, () => moonsFn().item(0).text === "Europa");
  test.do(() => expect(moonsFn().item(0).text).toBe("Europa"));
  test.do(() => expect(moonsFn().item(1).text).toBe("Ganymed"));
  test.do(() => expect(moonsFn().item(2).text).toBe("Io"));
  test.do(() => expect(moonsFn().item(3).text).toBe("Kallisto"));
  test.start();
});
