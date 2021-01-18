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

it("ajax: chat send button", function (done) {
  let chatlogFn = querySelectorFn("#page\\:mainForm\\:tachatlog\\:\\:field");
  let inputFn = querySelectorFn("#page\\:mainForm\\:inewmessage\\:\\:field");
  let sendButtonFn = querySelectorFn("#page\\:mainForm\\:sendButton");

  const test = new JasmineTestTool(done);
  test.setup(() => chatlogFn().textContent === "",
      () => inputFn().value = "delete chat",
      "click", sendButtonFn);
  test.do(() => expect(chatlogFn().textContent).toBe(""));
  test.do(() => inputFn().value = "Hi Peter, how are you?");
  test.event("click", sendButtonFn, () => chatlogFn().textContent === "User Two: Hi Peter, how are you?");
  test.do(() => expect(chatlogFn().textContent).toBe("User Two: Hi Peter, how are you?"));
  test.start();
});

it("ajax: dropdown button", function (done) {
  let buttonFn = querySelectorFn("#page\\:mainForm\\:lsendtoc\\:\\:command");
  let buttonLabelFn = querySelectorFn("#page\\:mainForm\\:lsendtoc\\:\\:command span");
  let sendToPeterFn = querySelectorFn("#page\\:mainForm\\:sendToPeter");
  let sendToBobFn = querySelectorFn("#page\\:mainForm\\:sendToBob");
  let sendToAllFn = querySelectorFn("#page\\:mainForm\\:sendToAll");

  const test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet; fix dropdown first"));
  /*TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
    sendToPeterFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().textContent, "SendTo: Peter");
  });
  TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
    sendToBobFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().textContent, "SendTo: Bob");
  });
  TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
    sendToAllFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(buttonLabelFn().textContent, "SendTo: All");
  });*/
  test.start();
});

it("ajax: currency change event", function (done) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:value\\:\\:field");
  let selectFn = querySelectorFn("#page\\:mainForm\\:currency\\:\\:field");
  let optionsFn = querySelectorAllFn("#page\\:mainForm\\:currency option");
  let outputFn = querySelectorFn("#page\\:mainForm\\:valueInEuro tobago-out");

  const test = new JasmineTestTool(done);
  test.setup(() => parseInt(inputFn().value.replaceAll(".", "")) === 1000
      && outputFn().textContent === "1.000,00",
      () => {
        inputFn().value = "1000";
        optionsFn().item(0).selected = false; // Yen
        optionsFn().item(1).selected = false; // Trinidad-Tobago Dollar
        optionsFn().item(2).selected = false; // US Dollar
        optionsFn().item(3).selected = true; // Euro
      },
      "change", selectFn);

  test.do(() => inputFn().value = "1000");
  test.do(() => optionsFn().item(0).selected = true); // Yen
  test.do(() => optionsFn().item(1).selected = false); // Trinidad-Tobago Dollar
  test.do(() => optionsFn().item(2).selected = false); // US Dollar
  test.do(() => optionsFn().item(3).selected = false); // Euro
  test.event("change", selectFn, () => outputFn().textContent === "8,85");
  test.do(() => expect(outputFn().textContent).toBe("8,85"));

  test.do(() => inputFn().value = "2000");
  test.do(() => optionsFn().item(0).selected = false); // Yen
  test.do(() => optionsFn().item(1).selected = true); // Trinidad-Tobago Dollar
  test.do(() => optionsFn().item(2).selected = false); // US Dollar
  test.do(() => optionsFn().item(3).selected = false); // Euro
  test.event("change", selectFn, () => outputFn().textContent === "267,50");
  test.do(() => expect(outputFn().textContent).toBe("267,50"));

  test.do(() => inputFn().value = "3000");
  test.do(() => optionsFn().item(0).selected = false); // Yen
  test.do(() => optionsFn().item(1).selected = false); // Trinidad-Tobago Dollar
  test.do(() => optionsFn().item(2).selected = true); // US Dollar
  test.do(() => optionsFn().item(3).selected = false); // Euro
  test.event("change", selectFn, () => outputFn().textContent === "2.688,29");
  test.do(() => expect(outputFn().textContent).toBe("2.688,29"));
  test.start();
});
