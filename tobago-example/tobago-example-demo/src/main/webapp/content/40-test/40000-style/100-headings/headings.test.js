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
  var $alink = jQueryFrame("#page\\:mainForm\\:link1");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink1");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h2", function (assert) {
  var $alink = jQueryFrame("#page\\:mainForm\\:link2");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink2");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h3", function (assert) {
  var $alink = jQueryFrame("#page\\:mainForm\\:link3");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink3");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h4", function (assert) {
  var $alink = jQueryFrame("#page\\:mainForm\\:link4");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink4");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h5", function (assert) {
  var $alink = jQueryFrame("#page\\:mainForm\\:link5");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink5");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test h6", function (assert) {
  var $alink = jQueryFrame("#page\\:mainForm\\:link6");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink6");
  testFont(assert, $alink, $buttonlink);
});

QUnit.test("Test no heading", function (assert) {
  var $alink = jQueryFrame("#page\\:mainForm\\:link0");
  var $buttonlink = jQueryFrame("#page\\:mainForm\\:actionLink0");
  testFont(assert, $alink, $buttonlink);
});

function testFont(assert, $alink, $buttonlink) {
  assert.expect(5);

  assert.equal($alink.css("color"), $buttonlink.css("color"));
  assert.equal($alink.css("font-family"), $buttonlink.css("font-family"));
  assert.equal($alink.css("font-size"), $buttonlink.css("font-size"));
  assert.equal($alink.css("font-weight"), $buttonlink.css("font-weight"));
  assert.equal($alink.css("text-decoration"), $buttonlink.css("text-decoration"));
}
