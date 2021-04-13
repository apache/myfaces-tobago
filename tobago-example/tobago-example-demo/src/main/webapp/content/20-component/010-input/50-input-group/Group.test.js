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
  let dropdownMenu = querySelectorFn(".dropdown-menu[name='page:mainForm:lsendtoc']");
  let sendToPeterFn = querySelectorFn("#page\\:mainForm\\:sendToPeter");
  let sendToBobFn = querySelectorFn("#page\\:mainForm\\:sendToBob");
  let sendToAllFn = querySelectorFn("#page\\:mainForm\\:sendToAll");

  const test = new JasmineTestTool(done);
  test.setup(() => !dropdownMenu().parentElement.classList.contains("tobago-page-menuStore"),
      null, "click", buttonFn);
  test.setup(() => buttonLabelFn().textContent !== "SendTo: Peter",
      null, "click", sendToAllFn);
  test.do(() => expect(dropdownMenu().parentElement.classList).not.toContain("tobago-page-menuStore"));
  test.do(() => expect(buttonLabelFn().textContent).not.toBe("SendTo: Peter"));

  test.event("click", buttonFn, () => dropdownMenu().parentElement.classList.contains("tobago-page-menuStore"));
  test.do(() => expect(dropdownMenu().parentElement.classList).toContain("tobago-page-menuStore"));
  test.event("click", sendToPeterFn, () => buttonLabelFn().textContent === "SendTo: Peter");
  test.do(() => expect(dropdownMenu().parentElement.classList).not.toContain("tobago-page-menuStore"));
  test.do(() => expect(buttonLabelFn().textContent).toBe("SendTo: Peter"));

  test.event("click", buttonFn, () => dropdownMenu().parentElement.classList.contains("tobago-page-menuStore"));
  test.do(() => expect(dropdownMenu().parentElement.classList).toContain("tobago-page-menuStore"));
  test.event("click", sendToBobFn, () => buttonLabelFn().textContent === "SendTo: Bob");
  test.do(() => expect(dropdownMenu().parentElement.classList).not.toContain("tobago-page-menuStore"));
  test.do(() => expect(buttonLabelFn().textContent).toBe("SendTo: Bob"));

  test.event("click", buttonFn, () => dropdownMenu().parentElement.classList.contains("tobago-page-menuStore"));
  test.do(() => expect(dropdownMenu().parentElement.classList).toContain("tobago-page-menuStore"));
  test.event("click", sendToAllFn, () => buttonLabelFn().textContent === "SendTo: All");
  test.do(() => expect(dropdownMenu().parentElement.classList).not.toContain("tobago-page-menuStore"));
  test.do(() => expect(buttonLabelFn().textContent).toBe("SendTo: All"));
  test.start();
});

it("ajax: currency change event", function (done) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:value\\:\\:field");
  let selectFn = querySelectorFn("#page\\:mainForm\\:currency\\:\\:field");
  let optionsFn = querySelectorAllFn("#page\\:mainForm\\:currency option");
  let outputFn = querySelectorFn("#page\\:mainForm\\:valueInEuro .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.setup(() => convertInt(outputFn().textContent) === 100000,
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
  test.event("change", selectFn, () => convertInt(outputFn().textContent) === 885);
  test.do(() => expect(convertInt(outputFn().textContent)).toBe(885));

  test.do(() => inputFn().value = "2000");
  test.do(() => optionsFn().item(0).selected = false); // Yen
  test.do(() => optionsFn().item(1).selected = true); // Trinidad-Tobago Dollar
  test.do(() => optionsFn().item(2).selected = false); // US Dollar
  test.do(() => optionsFn().item(3).selected = false); // Euro
  test.event("change", selectFn, () => convertInt(outputFn().textContent) === 26750);
  test.do(() => expect(convertInt(outputFn().textContent)).toBe(26750));

  test.do(() => inputFn().value = "3000");
  test.do(() => optionsFn().item(0).selected = false); // Yen
  test.do(() => optionsFn().item(1).selected = false); // Trinidad-Tobago Dollar
  test.do(() => optionsFn().item(2).selected = true); // US Dollar
  test.do(() => optionsFn().item(3).selected = false); // Euro
  test.event("change", selectFn, () => convertInt(outputFn().textContent) === 268829);
  test.do(() => expect(convertInt(outputFn().textContent)).toBe(268829));
  test.start();
});

/**
 * need this function, because chrome displays "1.000,00" and firefox displays "1,000.00"
 */
function convertInt(string) {
  return parseInt(string.replaceAll(",", "").replaceAll(".", ""));
}
