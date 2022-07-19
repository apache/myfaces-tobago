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

QUnit.test("RolesAllowed as 'guest'", function (assert) {
  var userFn = jQueryFrameFn("#page\\:mainForm\\:username\\:\\:field");
  var passwordFn = jQueryFrameFn("#page\\:mainForm\\:password\\:\\:field");
  var loginFn = jQueryFrameFn("#page\\:mainForm\\:login");
  var logoutFn = jQueryFrameFn("#page\\:mainForm\\:logout");
  var outputLabelFn = jQueryFrameFn("#page\\:mainForm\\:output label");
  var outputValueFn = jQueryFrameFn("#page\\:mainForm\\:output .tobago-out");
  var buttonFn = jQueryFrameFn("#page\\:mainForm\\:button");
  var ccOutputLabelFn = jQueryFrameFn("#page\\:mainForm\\:ccRolesTest\\:out label");
  var ccOutputValueFn = jQueryFrameFn("#page\\:mainForm\\:ccRolesTest\\:out .tobago-out");
  var ccButtonFn = jQueryFrameFn("#page\\:mainForm\\:ccRolesTest\\:submit");
  var ccButton2Fn = jQueryFrameFn("#page\\:mainForm\\:ccRolesTest\\:submit2");

  var url = document.getElementById("page:testframe").contentWindow.location.href;
  var time;

  assert.expect(5);
  var TTT = new TobagoTestTool(assert);
  TTT.asserts(6, function () {
    assert.equal(loginFn().length, 1, "user must be logged out");
    assert.equal(outputLabelFn().text(), "Output label");
    assert.equal(buttonFn().attr("disabled"), "disabled");
    assert.equal(ccOutputLabelFn().text(), "Label");
    assert.equal(ccButtonFn().attr("disabled"), "disabled");
    assert.equal(ccButton2Fn().attr("disabled"), "disabled");
  });
  TTT.action(function () {
    userFn().val("guest");
    passwordFn().val("guest");
    loginFn().click();
  });
  TTT.waitForResponse();
  TTT.action(function () {
    document.getElementById("page:testframe").contentWindow.location.href = url;
  });
  TTT.waitForResponse();
  TTT.waitMs(2000);
  TTT.asserts(6, function () {
    assert.equal(logoutFn().length, 1, "user must be logged in");
    assert.equal(outputLabelFn().text(), "Output label");
    assert.equal(buttonFn().attr("disabled"), undefined);
    assert.equal(ccOutputLabelFn().text(), "Label");
    assert.equal(ccButtonFn().attr("disabled"), undefined);
    assert.equal(ccButton2Fn().attr("disabled"), undefined);
  });
  TTT.action(function () {
    time = outputValueFn().text();
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.notEqual(outputValueFn().text(), time);
    assert.notEqual(ccOutputValueFn().text(), time);
  });
  TTT.action(function () {
    time = ccOutputValueFn().text();
    ccButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.notEqual(outputValueFn().text(), time);
    assert.notEqual(ccOutputValueFn().text(), time);
  });
  TTT.action(function () {
    logoutFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(loginFn().length, 1, "user must be logged out");
  });
  TTT.startTest();
});
