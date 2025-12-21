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

it("no layout manager", function (done) {
  const input = elementByIdFn("page:mainForm:form:input::field");
  const output = querySelectorFn("#page\\:mainForm\\:form\\:output .form-control-plaintext")
  const submit = elementByIdFn("page:mainForm:form:submit");
  const reset = elementByIdFn("page:mainForm:form:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => input().value === '', null, "click", reset);
  test.do(() => expect(input().value).toBe(""));
  test.do(() => expect(output().textContent).toBe(""));

  test.do(() => input().value = "Tobago");
  test.event("click", submit, () => output().textContent === "Tobago");
  test.do(() => expect(input().value).toBe("Tobago"));
  test.do(() => expect(output().textContent).toBe("Tobago"));

  test.event("click", reset, () => output().textContent === "");
  test.do(() => expect(input().value).toBe(""));
  test.do(() => expect(output().textContent).toBe(""));

  test.start();
});

it("no layout manager", function (done) {
  const html = querySelectorAllFn("html")
  const body = querySelectorFn("body")
  const ajaxButton = elementByIdFn("page:mainForm:ajaxPageReload");
  const timestampComponent = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => expect(html().length).toBe(1));
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp !== timestampComponent().textContent);
  test.do(() => expect(html().length).toBe(1));
  test.do(() => expect(getComputedStyle(body()).overflow).not.toBe("hidden"));
  test.start();
});
