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

it("Simple Panel", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = querySelectorFn("#page\\:mainForm\\:simple\\:showSimple");
  let hideFn = querySelectorFn("#page\\:mainForm\\:simple\\:hideSimple");
  let submitFn = querySelectorFn("#page\\:mainForm\\:simple\\:submitSimple");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:simple\\:inSimple\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => showFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => panelCollapsedFn() && panelCollapsedFn().value === "false");
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "some text");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => hideFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(false));
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(false));
  test.start();
});

it("Full Server Request", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = querySelectorFn("#page\\:mainForm\\:server\\:showServer");
  let hideFn = querySelectorFn("#page\\:mainForm\\:server\\:hideServer");
  let submitFn = querySelectorFn("#page\\:mainForm\\:server\\:submitServer");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:server\\:inServer\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => showFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => panelCollapsedFn() && panelCollapsedFn().value === "false");
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "some text");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => hideFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(false));
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(false));
  test.start();
});

it("Client Side", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showClient");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideClient");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitClient");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inClient\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => showFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "some text");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => hideFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.start();
});

it("Ajax", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjax");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjax");
  let submitFn = querySelectorFn("#page\\:mainForm\\:ajax\\:submitAjax");
  let panelCollapsedFn = querySelectorFn("#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjax\\:\\:field");

  let test = new JasmineTestTool(done);
  test.do(() => showFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => panelCollapsedFn() && panelCollapsedFn().value === "false");
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "some text");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "");
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(panelCollapsedFn().value).toBe("false"));
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => hideFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => panelCollapsedFn() && panelCollapsedFn().value === "true");
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(false));
  test.do(() => submitFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(panelCollapsedFn().value).toBe("true"));
  test.do(() => expect(inFn() !== null).toBe(false));
  test.start();
});
