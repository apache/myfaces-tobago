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

it("must be fixed first", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("must be fixed first"));
  test.start();
});

/*it("Ajax Input", function (done) {
  let ajaxInputFn = querySelectorFn("#page\\:mainForm\\:j_id_2g\\:\\:field");
  let ajaxOutputFn = querySelectorFn("#page\\:mainForm\\:outAjax tobago-out");

  let test = new JasmineTestTool(done);
  test.do(() => ajaxInputFn().value = "Alice");
  test.event("change", ajaxInputFn, () => ajaxOutputFn() && ajaxOutputFn().textContent === "Alice");
  test.do(() => expect(ajaxOutputFn().textContent).toBe(ajaxInputFn().value));
  test.start();
});

it("Event Input", function (done) {
  let eventInputFn = querySelectorFn("#page\\:mainForm\\:j_id_2k\\:\\:field");
  let eventOutputFn = querySelectorFn("#page\\:mainForm\\:j_id_2m tobago-out");

  let test = new JasmineTestTool(done);
  test.do(() => eventInputFn().value = "Alice");
  test.event("change", eventInputFn, () => eventOutputFn() && eventOutputFn().textContent === "Alice");
  test.do(() => expect(eventOutputFn().textContent).toBe(eventInputFn().value));
  test.start();
});

it("change the event name", function (done) {
  let ajaxFn = querySelectorFn("#page\\:mainForm\\:j_id_2p");
  let eventFn = querySelectorFn("#page\\:mainForm\\:j_id_2q");
  let outCounterFn = querySelectorFn("#page\\:mainForm\\:outCounter tobago-out");
  let counter = Number(outCounterFn().textContent);

  let test = new JasmineTestTool(done);
  test.event("dblclick", ajaxFn, () => outCounterFn() && Number(outCounterFn().textContent) === counter + 1);
  test.do(() => expect(Number(outCounterFn().textContent)).toBe(counter + 1));
  test.event("dblclick", eventFn, () => outCounterFn() && Number(outCounterFn().textContent) === counter + 2);
  test.do(() => expect(Number(outCounterFn().textContent)).toBe(counter + 2));
  test.event("click", ajaxFn, () => outCounterFn() && Number(outCounterFn().textContent) === counter + 2);
  test.do(() => expect(Number(outCounterFn().textContent)).toBe(counter + 2));
  test.event("dblclick", eventFn, () => outCounterFn() && Number(outCounterFn().textContent) === counter + 3);
  test.do(() => expect(Number(outCounterFn().textContent)).toBe(counter + 3));
  test.event("click", eventFn, () => outCounterFn() && Number(outCounterFn().textContent) === counter + 3);
  test.do(() => expect(Number(outCounterFn().textContent)).toBe(counter + 3));
  test.event("dblclick", ajaxFn, () => outCounterFn() && Number(outCounterFn().textContent) === counter + 4);
  test.do(() => expect(Number(outCounterFn().textContent)).toBe(counter + 4));
  test.start();
});

it("f:ajax and tc:event", function (done) {
  let submitFn = querySelectorFn("#page\\:mainForm\\:btnAjaxEvent");
  let outFn = querySelectorFn("#page\\:mainForm\\:out tobago-out");

  let test = new JasmineTestTool(done);
  test.setup(() => outFn().textContent === "Ajax",
      null, "click", submitFn);
  test.event("dblclick", submitFn, () => outFn() && outFn().textContent === "Event");
  test.do(() => expect(outFn().textContent).toBe("Event"));
  test.event("click", submitFn, () => outFn() && outFn().textContent === "Ajax");
  test.do(() => expect(outFn().textContent).toBe("Ajax"));
  test.start();
});*/
