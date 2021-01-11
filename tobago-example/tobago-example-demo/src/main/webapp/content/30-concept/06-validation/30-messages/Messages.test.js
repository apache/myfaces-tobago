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

it("Press '7 Messages' Button and close the first, the last and the fourth", function (done) {
  const tab = querySelectorFn("#page\\:mainForm\\:woAttr > .nav-link");
  const alerts = querySelectorAllFn("#page\\:mainForm\\:woAttr\\:woAttrMessage .alert");
  const closeButtons = querySelectorAllFn("#page\\:mainForm\\:woAttr\\:woAttrMessage .alert .btn-close");
  const alertLabels = querySelectorAllFn("#page\\:mainForm\\:woAttr\\:woAttrMessage .alert label");
  const messagesButton = elementByIdFn("page:mainForm:add7messages");

  let test = new JasmineTestTool(done);
  test.setup(
      () => tab().classList.contains("active"),
      null, "click", tab);
  test.setup(
      () => alerts().length === 7,
      null, "click", messagesButton);
  test.do(() => expect(alertLabels()[0].textContent).toBe("First Message - Info"));
  test.do(() => expect(alertLabels()[1].textContent).toBe("Second Message - Fatal"));
  test.do(() => expect(alertLabels()[2].textContent).toBe("Third Message - Warn"));
  test.do(() => expect(alertLabels()[3].textContent).toBe("Fourth Message - Fatal"));
  test.do(() => expect(alertLabels()[4].textContent).toBe("Fifth Message - Error"));
  test.do(() => expect(alertLabels()[5].textContent).toBe("Sixth Message - Info"));
  test.do(() => expect(alertLabels()[6].textContent).toBe("Seventh Message - Warn"));
  test.event("click", () => closeButtons()[0], () => alerts().length === 6);
  test.do(() => expect(alertLabels()[0].textContent).toBe("Second Message - Fatal"));
  test.do(() => expect(alertLabels()[1].textContent).toBe("Third Message - Warn"));
  test.do(() => expect(alertLabels()[2].textContent).toBe("Fourth Message - Fatal"));
  test.do(() => expect(alertLabels()[3].textContent).toBe("Fifth Message - Error"));
  test.do(() => expect(alertLabels()[4].textContent).toBe("Sixth Message - Info"));
  test.do(() => expect(alertLabels()[5].textContent).toBe("Seventh Message - Warn"));
  test.event("click", () => closeButtons()[2], () => alerts().length === 5);
  test.do(() => expect(alertLabels()[0].textContent).toBe("Second Message - Fatal"));
  test.do(() => expect(alertLabels()[1].textContent).toBe("Third Message - Warn"));
  test.do(() => expect(alertLabels()[2].textContent).toBe("Fifth Message - Error"));
  test.do(() => expect(alertLabels()[3].textContent).toBe("Sixth Message - Info"));
  test.do(() => expect(alertLabels()[4].textContent).toBe("Seventh Message - Warn"));
  test.event("click", () => closeButtons()[4], () => alerts().length === 4);
  test.do(() => expect(alertLabels()[0].textContent).toBe("Second Message - Fatal"));
  test.do(() => expect(alertLabels()[1].textContent).toBe("Third Message - Warn"));
  test.do(() => expect(alertLabels()[2].textContent).toBe("Fifth Message - Error"));
  test.do(() => expect(alertLabels()[3].textContent).toBe("Sixth Message - Info"));
  test.start();
});
