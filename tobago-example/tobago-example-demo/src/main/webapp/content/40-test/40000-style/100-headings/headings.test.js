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

QUnit.test("Test h1", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link1");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink1");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h2", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link2");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink2");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h3", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link3");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink3");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h4", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link4");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink4");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h5", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link5");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink5");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h6", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link6");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink6");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test no heading", function (assert) {
  let $alink = jQueryFrameFn("#page\\:mainForm\\:link0");
  let $buttonlink = jQueryFrameFn("#page\\:mainForm\\:actionLink0");
  testFont(assert, $alink, $buttonlink);
});

function testFont(assert, $alink, $buttonlink) {
  assert.expect(5);

  assert.equal($alink().css("color"), $buttonlink().css("color"));
  assert.equal($alink().css("font-family"), $buttonlink().css("font-family"));
  assert.equal($alink().css("font-size"), $buttonlink().css("font-size"));
  assert.equal($alink().css("font-weight"), $buttonlink().css("font-weight"));
  assert.equal($alink().css("text-decoration"), $buttonlink().css("text-decoration"));
}

QUnit.test("Ajax reload for section 2", function (assert) {
  let $reloadButton = jQueryFrameFn("#page\\:mainForm\\:reloadSection2");
  let $section2Header = jQueryFrameFn("#page\\:mainForm\\:levelTwoSection h3");
  let $timestamp = jQueryFrameFn("#page\\:mainForm\\:timestamp span");
  let firstTimestamp = $timestamp().text();

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(1, function () {
    assert.equal($section2Header().length, 1);
  });
  TTT.action(function () {
    $reloadButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($section2Header().length, 1);
    assert.ok(firstTimestamp < $timestamp().text(), "value of new timestamp must be higher");
  });
  TTT.startTest();
});
