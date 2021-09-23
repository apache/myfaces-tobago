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
import {elementByIdFn} from "/script/tobago-test.js";

it("border-radius", function (done) {
  let dropdown1Fn = elementByIdFn("page:mainForm:dd1::command");
  let dropdown2Fn = elementByIdFn("page:mainForm:dd2::command");
  let dropdown3Fn = elementByIdFn("page:mainForm:dd3::command");
  let dropdown4Fn = elementByIdFn("page:mainForm:dd4::command");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(dropdown1Fn()).borderTopLeftRadius).not.toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown1Fn()).borderTopRightRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown1Fn()).borderBottomLeftRadius).not.toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown1Fn()).borderBottomRightRadius).toBe("0px"));

  test.do(() => expect(getComputedStyle(dropdown2Fn()).borderTopLeftRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown2Fn()).borderTopRightRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown2Fn()).borderBottomLeftRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown2Fn()).borderBottomRightRadius).toBe("0px"));

  test.do(() => expect(getComputedStyle(dropdown3Fn()).borderTopLeftRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown3Fn()).borderTopRightRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown3Fn()).borderBottomLeftRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown3Fn()).borderBottomRightRadius).toBe("0px"));

  test.do(() => expect(getComputedStyle(dropdown4Fn()).borderTopLeftRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown4Fn()).borderTopRightRadius).not.toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown4Fn()).borderBottomLeftRadius).toBe("0px"));
  test.do(() => expect(getComputedStyle(dropdown4Fn()).borderBottomRightRadius).not.toBe("0px"));
  test.start();
});
