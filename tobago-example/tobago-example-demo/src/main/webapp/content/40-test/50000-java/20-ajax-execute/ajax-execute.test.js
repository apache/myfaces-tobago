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

QUnit.test("ajax excecute", function (assert) {
  let $in1 = jQueryFrameFn("#page\\:mainForm\\:in1\\:\\:field");
  let $in2 = jQueryFrameFn("#page\\:mainForm\\:in2\\:\\:field");
  let $in3 = jQueryFrameFn("#page\\:mainForm\\:in3\\:\\:field");
  let $in4 = jQueryFrameFn("#page\\:mainForm\\:in4\\:\\:field");
  let $clearButton = jQueryFrameFn("#page\\:mainForm\\:clear");
  let $submitButton = jQueryFrameFn("#page\\:mainForm\\:submit");
  let $reloadButton = jQueryFrameFn("#page\\:mainForm\\:reload");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in1().val("a");
    $in2().val("b");
    $in3().val("c");
    $in4().val("d");
    $clearButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($in1().val(), "");
    assert.equal($in2().val(), "");
    assert.equal($in3().val(), "");
    assert.equal($in4().val(), "");
  });
  TTT.action(function () {
    $in1().val("a");
    $in2().val("b");
    $in3().val("c");
    $in4().val("d");
    $submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($in1().val(), "a");
    assert.equal($in2().val(), "b");
    assert.equal($in3().val(), "c");
    assert.equal($in4().val(), "");
  });
  TTT.action(function () {
    $reloadButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($in1().val(), "a");
    assert.equal($in2().val(), "");
    assert.equal($in3().val(), "c");
    assert.equal($in4().val(), "");
  });
  TTT.startTest();
});
