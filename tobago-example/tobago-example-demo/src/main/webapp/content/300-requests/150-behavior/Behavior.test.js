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

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Ajax Input", function (done) {
  const ajaxInputFn = elementByIdFn("page:mainForm:inputAjax::field");
  const ajaxOutputFn = querySelectorFn("#page\\:mainForm\\:outputAjax .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => ajaxOutputFn().textContent !== "Alice",
      () => ajaxInputFn().value = "Bob",
      "change", ajaxInputFn);
  test.do(() => ajaxInputFn().value = "Alice");
  test.event("change", ajaxInputFn, () => ajaxOutputFn() && ajaxOutputFn().textContent === "Alice");
  test.do(() => expect(ajaxOutputFn().textContent).toBe("Alice"));
  test.start();
});

it("Event Input", function (done) {
  const eventInputFn = elementByIdFn("page:mainForm:inputEvent::field");
  const eventOutputFn = querySelectorFn("#page\\:mainForm\\:outputEvent .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => eventOutputFn().textContent !== "Charlie",
      () => eventInputFn().value = "Dave",
      "change", eventInputFn);
  test.do(() => eventInputFn().value = "Charlie");
  test.event("change", eventInputFn, () => eventOutputFn() && eventOutputFn().textContent === "Charlie");
  test.do(() => expect(eventOutputFn().textContent).toBe("Charlie"));
  test.start();
});

it("change the event name", function (done) {
  const dblButtonAjaxFn = elementByIdFn("page:mainForm:dblButtonAjax");
  const dblButtonEventFn = elementByIdFn("page:mainForm:dblButtonEvent");
  const dblCounterFn = querySelectorFn("#page\\:mainForm\\:dblCounter .form-control-plaintext");
  let counter = Number(dblCounterFn().textContent);

  let test = new JasmineTestTool(done);
  test.event("dblclick", dblButtonAjaxFn,
      () => dblCounterFn() && Number(dblCounterFn().textContent) === counter + 1);
  test.do(() => expect(Number(dblCounterFn().textContent)).toBe(counter + 1));
  test.event("dblclick", dblButtonEventFn,
      () => dblCounterFn() && Number(dblCounterFn().textContent) === counter + 2);
  test.do(() => expect(Number(dblCounterFn().textContent)).toBe(counter + 2));
  test.event("dblclick", dblButtonEventFn,
      () => dblCounterFn() && Number(dblCounterFn().textContent) === counter + 3);
  test.do(() => expect(Number(dblCounterFn().textContent)).toBe(counter + 3));
  test.event("dblclick", dblButtonAjaxFn,
      () => dblCounterFn() && Number(dblCounterFn().textContent) === counter + 4);
  test.do(() => expect(Number(dblCounterFn().textContent)).toBe(counter + 4));
  test.start();
});

it("f:ajax and tc:event", function (done) {
  let submitFn = elementByIdFn("page:mainForm:btnAjaxEvent");
  let outFn = querySelectorFn("#page\\:mainForm\\:out .form-control-plaintext");

  let test = new JasmineTestTool(done);
  test.setup(() => outFn().textContent === "Ajax",
      null, "click", submitFn);
  test.event("dblclick", submitFn, () => outFn() && outFn().textContent === "Event");
  test.do(() => expect(outFn().textContent).toBe("Event"));
  test.event("click", submitFn, () => outFn() && outFn().textContent === "Ajax");
  test.do(() => expect(outFn().textContent).toBe("Ajax"));
  test.start();
});

it("Custom event", function (done) {
  let button = elementByIdFn("page:mainForm:customEventButton");
  let output = elementByIdFn("page:mainForm:customEventOutput");

  const test = new JasmineTestTool(done);
  test.do(() => output().textContent === "");
  test.event("click", button, () => output().textContent.startsWith("my-event fired at "));
  test.start();
});
