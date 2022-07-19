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

it("RolesAllowed as 'guest'", function (done) {
  const userFn = elementByIdFn("page:mainForm:username::field");
  const passwordFn = elementByIdFn("page:mainForm:password::field");
  const loginFn = elementByIdFn("page:mainForm:login");
  const logoutFn = elementByIdFn("page:mainForm:logout");
  const outputLabelFn = querySelectorFn("#page\\:mainForm\\:output label");
  const outputValueFn = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  const buttonFn = elementByIdFn("page:mainForm:button");
  const ccOutputLabelFn = querySelectorFn("#page\\:mainForm\\:ccRolesTest\\:out label");
  const ccOutputValueFn = querySelectorFn("#page\\:mainForm\\:ccRolesTest\\:out .form-control-plaintext");
  const ccButtonFn = elementByIdFn("page:mainForm:ccRolesTest:submit");
  const ccButton2Fn = elementByIdFn("page:mainForm:ccRolesTest:submit2");

  const url = document.getElementById("page:testframe").contentWindow.location.href;
  let time;

  const test = new JasmineTestTool(done);
  test.setup(() => loginFn(), null, "click", logoutFn);

  test.do(() => expect(loginFn()).not.toBeNull());
  test.do(() => expect(outputLabelFn().textContent).toBe("Output label"));
  test.do(() => expect(buttonFn().disabled).toBeTrue());
  test.do(() => expect(ccOutputLabelFn().textContent).toBe("Label"));
  test.do(() => expect(ccButtonFn().disabled).toBeTrue());
  test.do(() => expect(ccButton2Fn().disabled).toBeTrue());

  test.do(() => userFn().value = "guest");
  test.do(() => passwordFn().value = "guest");
  test.event("click", loginFn, () => !loginFn());
  test.do(() => document.getElementById("page:testframe").contentWindow.location.href = url);
  test.wait(() => logoutFn());

  test.do(() => expect(logoutFn()).not.toBeNull());
  test.do(() => expect(outputLabelFn().textContent).toBe("Output label"));
  test.do(() => expect(buttonFn().disabled).toBeFalse());
  test.do(() => expect(ccOutputLabelFn().textContent).toBe("Label"));
  test.do(() => expect(ccButtonFn().disabled).toBeFalse());
  test.do(() => expect(ccButton2Fn().disabled).toBeFalse());

  test.do(() => time = outputValueFn().textContent);
  test.event("click", buttonFn, () => outputValueFn().textContent !== time);
  test.do(() => expect(outputValueFn().textContent).not.toBe(time));
  test.do(() => expect(ccOutputValueFn().textContent).not.toBe(time));

  test.do(() => time = ccOutputValueFn().textContent);
  test.event("click", ccButtonFn, () => ccOutputValueFn().textContent !== time);
  test.do(() => expect(outputValueFn().textContent).not.toBe(time));
  test.do(() => expect(ccOutputValueFn().textContent).not.toBe(time));

  test.event("click", logoutFn, () => loginFn());
  test.do(() => expect(loginFn()).not.toBeNull());

  test.start();
});
