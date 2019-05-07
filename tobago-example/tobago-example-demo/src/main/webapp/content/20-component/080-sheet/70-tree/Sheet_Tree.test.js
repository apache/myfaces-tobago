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

QUnit.test("Collapse tree", function (assert) {
  var row0nameFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:nameOut");
  var row0centralBodyFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:centralBodyOut");
  var row0distanceFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:distanceOut");
  var row0periodFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:periodOut");
  var row0discovererFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:discovererOut");
  var row0yearFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:yearOut");
  var row1nameFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:nameOut");
  var row1centralBodyFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:centralBodyOut");
  var row1distanceFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:distanceOut");
  var row1periodFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:periodOut");
  var row1discovererFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:discovererOut");
  var row1yearFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:yearOut");
  var rootTreeButtonFn = jQueryFrameFn("#page\\:mainForm\\:sheet\\:0\\:nameCol .tobago-treeNode-toggle");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(13, function () {
    assert.equal(row0nameFn().text(), "Sun");
    assert.equal(row0centralBodyFn().text(), "-");
    assert.equal(row0distanceFn().text(), "0");
    assert.equal(row0periodFn().text(), "0.0");
    assert.equal(row0discovererFn().text(), "-");
    assert.equal(row0yearFn().text(), "");
    assert.equal(row1nameFn().text(), "Mercury");
    assert.equal(row1centralBodyFn().text(), "Sun");
    assert.equal(row1distanceFn().text(), "57910");
    assert.equal(row1periodFn().text(), "87.97");
    assert.equal(row1discovererFn().text(), "-");
    assert.equal(row1yearFn().text(), "");

    assert.notEqual(row1yearFn().parents(".tobago-sheet-row").css("display"), "none");
  });
  TTT.action(function () {
    rootTreeButtonFn().click();
  });
  TTT.waitMs(1000);
  TTT.asserts(7, function () {
    assert.equal(row0nameFn().text(), "Sun");
    assert.equal(row0centralBodyFn().text(), "-");
    assert.equal(row0distanceFn().text(), "0");
    assert.equal(row0periodFn().text(), "0.0");
    assert.equal(row0discovererFn().text(), "-");
    assert.equal(row0yearFn().text(), "");

    assert.equal(row1yearFn().parents(".tobago-sheet-row").css("display"), "none");
  });
  TTT.startTest();
});
