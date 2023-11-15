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
import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";

it("Ajax rendering of labelLayout=gridLeft", function (done) {
  const timestampComponent = elementByIdFn("page:mainForm:timestamp");
  const gridLeftInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:label");
  const gridLeftInputField = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:field");
  const gridRightInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:label");
  const gridRightInputField = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:field");
  const gridLeftNoLabelLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:label");
  const gridLeftNoLabelField = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxGridLeft");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp < timestampComponent().textContent);
  test.do(() => expect(gridLeftInputLabel().length).toEqual(1));
  test.do(() => expect(gridLeftInputField().length).toEqual(1));
  test.do(() => expect(gridRightInputLabel().length).toEqual(1));
  test.do(() => expect(gridRightInputField().length).toEqual(1));
  test.do(() => expect(gridLeftNoLabelLabel().length).toEqual(0));
  test.do(() => expect(gridLeftNoLabelField().length).toEqual(1));
  test.start();
});

it("Ajax rendering of labelLayout=gridRight", function (done) {
  const timestampComponent = elementByIdFn("page:mainForm:timestamp");
  const gridLeftInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:label");
  const gridLeftInputField = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:field");
  const gridRightInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:label");
  const gridRightInputField = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:field");
  const gridLeftNoLabelLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:label");
  const gridLeftNoLabelField = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxGridRight");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp < timestampComponent().textContent);
  test.do(() => expect(gridLeftInputLabel().length).toEqual(1));
  test.do(() => expect(gridLeftInputField().length).toEqual(1));
  test.do(() => expect(gridRightInputLabel().length).toEqual(1));
  test.do(() => expect(gridRightInputField().length).toEqual(1));
  test.do(() => expect(gridLeftNoLabelLabel().length).toEqual(0));
  test.do(() => expect(gridLeftNoLabelField().length).toEqual(1));
  test.start();
});

it("Ajax rendering of labelLayout=gridTop", function (done) {
  const timestampComponent = elementByIdFn("page:mainForm:timestamp");
  const gridLeftInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:label");
  const gridLeftInputField = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:field");
  const gridRightInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:label");
  const gridRightInputField = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:field");
  const gridLeftNoLabelLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:label");
  const gridLeftNoLabelField = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxGridTop");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp < timestampComponent().textContent);
  test.do(() => expect(gridLeftInputLabel().length).toEqual(1));
  test.do(() => expect(gridLeftInputField().length).toEqual(1));
  test.do(() => expect(gridRightInputLabel().length).toEqual(1));
  test.do(() => expect(gridRightInputField().length).toEqual(1));
  test.do(() => expect(gridLeftNoLabelLabel().length).toEqual(0));
  test.do(() => expect(gridLeftNoLabelField().length).toEqual(1));
  test.start();
});

it("Ajax rendering of labelLayout=gridBottom", function (done) {
  const timestampComponent = elementByIdFn("page:mainForm:timestamp");
  const gridLeftInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:label");
  const gridLeftInputField = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:field");
  const gridRightInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:label");
  const gridRightInputField = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:field");
  const gridLeftNoLabelLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:label");
  const gridLeftNoLabelField = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxGridBottom");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp < timestampComponent().textContent);
  test.do(() => expect(gridLeftInputLabel().length).toEqual(1));
  test.do(() => expect(gridLeftInputField().length).toEqual(1));
  test.do(() => expect(gridRightInputLabel().length).toEqual(1));
  test.do(() => expect(gridRightInputField().length).toEqual(1));
  test.do(() => expect(gridLeftNoLabelLabel().length).toEqual(0));
  test.do(() => expect(gridLeftNoLabelField().length).toEqual(1));
  test.start();
});

it("Ajax rendering of labelLayout=gridLeft without label attribute", function (done) {
  const timestampComponent = elementByIdFn("page:mainForm:timestamp");
  const gridLeftInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:label");
  const gridLeftInputField = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:field");
  const gridRightInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:label");
  const gridRightInputField = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:field");
  const gridLeftNoLabelLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:label");
  const gridLeftNoLabelField = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxGridLeftNoLabel");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp < timestampComponent().textContent);
  test.do(() => expect(gridLeftInputLabel().length).toEqual(1));
  test.do(() => expect(gridLeftInputField().length).toEqual(1));
  test.do(() => expect(gridRightInputLabel().length).toEqual(1));
  test.do(() => expect(gridRightInputField().length).toEqual(1));
  test.do(() => expect(gridLeftNoLabelLabel().length).toEqual(0));
  test.do(() => expect(gridLeftNoLabelField().length).toEqual(1));
  test.start();
});

it("Ajax rendering of the whole grid layout component", function (done) {
  const timestampComponent = elementByIdFn("page:mainForm:timestamp");
  const gridLeftInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:label");
  const gridLeftInputField = querySelectorAllFn("#page\\:mainForm\\:gridLeftInput\\:\\:field");
  const gridRightInputLabel = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:label");
  const gridRightInputField = querySelectorAllFn("#page\\:mainForm\\:gridRightInput\\:\\:field");
  const gridLeftNoLabelLabel = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:label");
  const gridLeftNoLabelField = querySelectorAllFn("#page\\:mainForm\\:gridLeftNoLabel\\:\\:field");
  const ajaxButton = elementByIdFn("page:mainForm:ajaxGridLayout");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = timestampComponent().textContent);
  test.event("click", ajaxButton, () => timestamp < timestampComponent().textContent);
  test.do(() => expect(gridLeftInputLabel().length).toEqual(1));
  test.do(() => expect(gridLeftInputField().length).toEqual(1));
  test.do(() => expect(gridRightInputLabel().length).toEqual(1));
  test.do(() => expect(gridRightInputField().length).toEqual(1));
  test.do(() => expect(gridLeftNoLabelLabel().length).toEqual(0));
  test.do(() => expect(gridLeftNoLabelField().length).toEqual(1));
  test.start();
});
