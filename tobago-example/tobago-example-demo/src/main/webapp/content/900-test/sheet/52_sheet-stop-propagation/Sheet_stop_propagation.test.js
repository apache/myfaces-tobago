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

import {elementByIdFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Test stop-propagation attribute", function (done) {
  const plain = elementByIdFn("page:mainForm:spPlain");
  const ajax = elementByIdFn("page:mainForm:spAjax");
  const eventFalse = elementByIdFn("page:mainForm:spEventFalse");
  const eventTrue = elementByIdFn("page:mainForm:spEventTrue");
  const eventFalseAjax = elementByIdFn("page:mainForm:spEventFalseAjax");
  const eventTrueAjax = elementByIdFn("page:mainForm:spEventTrueAjax");

  const sheetPlain = elementByIdFn("page:mainForm:sheet:sheetSpPlain");
  const sheetAjax = elementByIdFn("page:mainForm:sheet:sheetSpAjax");
  const sheetEventFalse = elementByIdFn("page:mainForm:sheet:sheetSpEventFalse");
  const sheetEventTrue = elementByIdFn("page:mainForm:sheet:sheetSpEventTrue");
  const sheetEventFalseAjax = elementByIdFn("page:mainForm:sheet:sheetSpEventFalseAjax");
  const sheetEventTrueAjax = elementByIdFn("page:mainForm:sheet:sheetSpEventTrueAjax");

  const test = new JasmineTestTool(done);
  test.do(() => expect(isStopPropagationRendered(plain())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(ajax())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(eventFalse())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(eventTrue())).toBeTrue());
  test.do(() => expect(isStopPropagationRendered(eventFalseAjax())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(eventTrueAjax())).toBeTrue());

  test.do(() => expect(isStopPropagationRendered(sheetPlain())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(sheetAjax())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(sheetEventFalse())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(sheetEventTrue())).toBeTrue());
  test.do(() => expect(isStopPropagationRendered(sheetEventFalseAjax())).toBeFalse());
  test.do(() => expect(isStopPropagationRendered(sheetEventTrueAjax())).toBeTrue());
  test.start();
});

function isStopPropagationRendered(button) {
  const tobagoBehavior = button.querySelector("tobago-behavior");
  return tobagoBehavior.hasAttribute("stop-propagation");
}
