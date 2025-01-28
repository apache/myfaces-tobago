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

it("Simple Collapsible Box: show -> hide transition", function (done) {
  let showFn = querySelectorFn("#page\\:mainForm\\:controller\\:show");
  let hideFn = querySelectorFn("#page\\:mainForm\\:controller\\:hide");
  let contentFn = querySelectorFn("#page\\:mainForm\\:controller\\:content");

  let test = new JasmineTestTool(done);
  test.setup(
      () => contentFn(),
      null, "click", showFn);
  test.do(() => expect(contentFn() !== null).toBe(true));
  test.event("click", hideFn, () => !contentFn());
  test.do(() => expect(contentFn() !== null).toBe(false));
  test.start();
});

it("Simple Collapsible Box: hide -> show transition", function (done) {
  let showFn = querySelectorFn("#page\\:mainForm\\:controller\\:show");
  let hideFn = querySelectorFn("#page\\:mainForm\\:controller\\:hide");
  let contentFn = querySelectorFn("#page\\:mainForm\\:controller\\:content");

  let test = new JasmineTestTool(done);
  test.setup(
      () => !contentFn(),
      null, "click", hideFn);
  test.do(() => expect(contentFn() !== null).toBe(false));
  test.event("click", showFn, () => contentFn());
  test.do(() => expect(contentFn() !== null).toBe(true));
  test.start();
});

it("Full Server Request: open both boxes", function (done) {
  let show1Fn = querySelectorFn("#page\\:mainForm\\:server\\:show1");
  let show2Fn = querySelectorFn("#page\\:mainForm\\:server\\:show2");
  let hide1Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let hide2Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide2");
  let content1Fn = querySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = querySelectorFn("#page\\:mainForm\\:server\\:content2");

  let test = new JasmineTestTool(done);
  test.setup(
      () => !content1Fn(),
      null, "click", hide1Fn);
  test.setup(
      () => !content2Fn(),
      null, "click", hide2Fn);
  test.do(() => expect(content1Fn() !== null).toBe(false));
  test.do(() => expect(content2Fn() !== null).toBe(false));
  test.event("click", show1Fn, () => content1Fn());
  test.do(() => expect(content1Fn() !== null).toBe(true));
  test.do(() => expect(content2Fn() !== null).toBe(false));
  test.event("click", show2Fn, () => content2Fn());
  test.do(() => expect(content1Fn() !== null).toBe(true));
  test.do(() => expect(content2Fn() !== null).toBe(true));
  test.start();
});

it("Full Server Request: open box 1, close box 2", function (done) {
  let show1Fn = querySelectorFn("#page\\:mainForm\\:server\\:show1");
  let show2Fn = querySelectorFn("#page\\:mainForm\\:server\\:show2");
  let hide1Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let hide2Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide2");
  let content1Fn = querySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = querySelectorFn("#page\\:mainForm\\:server\\:content2");

  let test = new JasmineTestTool(done);
  test.setup(
      () => !content1Fn(),
      null, "click", hide1Fn);
  test.setup(
      () => content2Fn(),
      null, "click", show2Fn);
  test.do(() => expect(content1Fn() !== null).toBe(false));
  test.do(() => expect(content2Fn() !== null).toBe(true));
  test.event("click", show1Fn, () => content1Fn());
  test.do(() => expect(content1Fn() !== null).toBe(true));
  test.do(() => expect(content2Fn() !== null).toBe(true));
  test.event("click", hide2Fn, () => !content2Fn());
  test.do(() => expect(content1Fn() !== null).toBe(true));
  test.do(() => expect(content2Fn() !== null).toBe(false));
  test.start();
});

it("Full Server Request: close box 1, open box 2", function (done) {
  let hide1Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let hide2Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide2");
  let show1Fn = querySelectorFn("#page\\:mainForm\\:server\\:show1");
  let show2Fn = querySelectorFn("#page\\:mainForm\\:server\\:show2");
  let content1Fn = querySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = querySelectorFn("#page\\:mainForm\\:server\\:content2");

  let test = new JasmineTestTool(done);
  test.setup(
      () => content1Fn(),
      null, "click", show1Fn);
  test.setup(
      () => !content2Fn(),
      null, "click", hide2Fn);
  test.do(() => expect(content1Fn() !== null).toBe(true));
  test.do(() => expect(content2Fn() !== null).toBe(false));
  test.event("click", hide1Fn, () => !content1Fn());
  test.do(() => expect(content1Fn() !== null).toBe(false))
  test.do(() => expect(content2Fn() !== null).toBe(false));
  test.event("click", show2Fn, () => content2Fn());
  test.do(() => expect(content1Fn() !== null).toBe(false));
  test.do(() => expect(content2Fn() !== null).toBe(true));
  test.start();
});

it("Full Server Request: close both boxes", function (done) {
  let hide1Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let hide2Fn = querySelectorFn("#page\\:mainForm\\:server\\:hide2");
  let show1Fn = querySelectorFn("#page\\:mainForm\\:server\\:show1");
  let show2Fn = querySelectorFn("#page\\:mainForm\\:server\\:show2");
  let content1Fn = querySelectorFn("#page\\:mainForm\\:server\\:content1");
  let content2Fn = querySelectorFn("#page\\:mainForm\\:server\\:content2");

  let test = new JasmineTestTool(done);
  test.setup(
      () => content1Fn(),
      null, "click", show1Fn);
  test.setup(
      () => content2Fn(),
      null, "click", show2Fn);
  test.do(() => expect(content1Fn() !== null).toBe(true));
  test.do(() => expect(content2Fn() !== null).toBe(true));
  test.event("click", hide1Fn, () => !content1Fn());
  test.do(() => expect(content1Fn() !== null).toBe(false));
  test.do(() => expect(content2Fn() !== null).toBe(true));
  test.event("click", hide2Fn, () => !content2Fn());
  test.do(() => expect(content1Fn() !== null).toBe(false));
  test.do(() => expect(content2Fn() !== null).toBe(false));
  test.start();
});

it("Client Side: show -> hide transition", function (done) {
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  let boxFn = querySelectorFn("#page\\:mainForm\\:client\\:noRequestBox");

  let test = new JasmineTestTool(done);
  test.setup(() => !boxFn().classList.contains("tobago-collapsed"),
      null, "click", showFn);
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).toBe(false));
  test.event("click", hideFn, () => boxFn().classList.contains("tobago-collapsed"));
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).toBe(true));
  test.start();
});

it("Client Side: hide -> show transition", function (done) {
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  let boxFn = querySelectorFn("#page\\:mainForm\\:client\\:noRequestBox");

  let test = new JasmineTestTool(done);
  test.setup(() => boxFn().classList.contains("tobago-collapsed"),
      null, "click", hideFn);
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).toBe(true));
  test.event("click", showFn, () => !boxFn().classList.contains("tobago-collapsed"));
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).toBe(false));
  test.start();
});

it("Client Side: hide content and submit empty string", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:client\\:showNoRequestBox");
  let hideFn = querySelectorFn("#page\\:mainForm\\:client\\:hideNoRequestBox");
  let boxFn = querySelectorFn("#page\\:mainForm\\:client\\:noRequestBox");
  let inFn = querySelectorFn("#page\\:mainForm\\:client\\:inNoRequestBox\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:client\\:submitNoRequestBox");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "some content",
      "click", submitFn);
  test.setup(() => !boxFn().classList.contains("tobago-collapsed"),
      null, "click", showFn);
  test.event("click", hideFn, () => boxFn().classList.contains("tobago-collapsed"));
  test.do(() => expect(boxFn().classList.contains("tobago-collapsed")).toBe(true));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Ajax: show -> hide transition", function (done) {
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => inFn(),
      null, "click", showFn);
  test.do(() => expect(inFn() !== null).toBe(true));
  test.event("click", hideFn, () => !inFn());
  test.do(() => expect(inFn() !== null).toBe(false));
  test.start();
});

it("Ajax: hide -> show transition", function (done) {
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");

  let test = new JasmineTestTool(done);
  test.setup(
      () => !inFn(),
      null, "click", hideFn);
  test.do(() => expect(inFn() !== null).toBe(false));
  test.event("click", showFn, () => inFn());
  test.do(() => expect(inFn() !== null).toBe(true));
  test.start();
});

it("Ajax: submit empty string with shown and hidden content", function (done) {
  let hide1FullReqFn = querySelectorFn("#page\\:mainForm\\:server\\:hide1");
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let showFn = querySelectorFn("#page\\:mainForm\\:ajax\\:showAjaxBox");
  let hideFn = querySelectorFn("#page\\:mainForm\\:ajax\\:hideAjaxBox");
  let inFn = querySelectorFn("#page\\:mainForm\\:ajax\\:inAjaxBox\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:ajax\\:submitAjaxBox");

  let test = new JasmineTestTool(done);
  test.setup(
      () => inFn(),
      null, "click", showFn);
  test.setup(
      () => messagesFn() && messagesFn().length === 0,
      null, "click", hide1FullReqFn);
  test.do(() => expect(inFn() !== null).toBe(true));
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.event("click", hideFn, () => !inFn());
  test.do(() => expect(inFn() !== null).toBe(false));
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});
