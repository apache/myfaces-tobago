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

it("ajax execute", function (done) {
  let in1Fn = elementByIdFn("page:mainForm:in1::field");
  let in2Fn = elementByIdFn("page:mainForm:in2::field");
  let in3Fn = elementByIdFn("page:mainForm:in3::field");
  let in4Fn = elementByIdFn("page:mainForm:in4::field");
  let clearButtonFn = elementByIdFn("page:mainForm:clear");
  let submitButtonFn = elementByIdFn("page:mainForm:submit");
  let reloadButtonFn = elementByIdFn("page:mainForm:reload");

  const test = new JasmineTestTool(done);
  test.setup(
      () => in1Fn().value === "" && in2Fn().value === "" && in3Fn().value === "" && in4Fn().value === "",
      null, "click", clearButtonFn);
  test.do(() => expect(in1Fn().value).toBe(""));
  test.do(() => expect(in2Fn().value).toBe(""));
  test.do(() => expect(in3Fn().value).toBe(""));
  test.do(() => expect(in4Fn().value).toBe(""));

  test.do(() => in1Fn().value = "Alice");
  test.do(() => in2Fn().value = "Bob");
  test.do(() => in3Fn().value = "Charlie");
  test.do(() => in4Fn().value = "Dave");

  test.event("click", submitButtonFn,
      () => in1Fn().value === "Alice" && in2Fn().value === "Bob"
          && in3Fn().value === "Charlie" && in4Fn().value === "");
  test.do(() => expect(in1Fn().value).toBe("Alice"));
  test.do(() => expect(in2Fn().value).toBe("Bob"));
  test.do(() => expect(in3Fn().value).toBe("Charlie"));
  test.do(() => expect(in4Fn().value).toBe(""));

  test.event("click", reloadButtonFn,
      () => in1Fn().value === "Alice" && in2Fn().value === ""
          && in3Fn().value === "Charlie" && in4Fn().value === "");
  test.do(() => expect(in1Fn().value).toBe("Alice"));
  test.do(() => expect(in2Fn().value).toBe(""));
  test.do(() => expect(in3Fn().value).toBe("Charlie"));
  test.do(() => expect(in4Fn().value).toBe(""));

  test.start();
});
