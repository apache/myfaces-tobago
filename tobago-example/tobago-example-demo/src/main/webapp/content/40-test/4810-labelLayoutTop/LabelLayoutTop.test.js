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

QUnit.test("Check width for tc:date", function (assert) {
  assert.expect(2);
  testWidth(assert, "date");
});

QUnit.test("Check width for tc:file", function (assert) {
  assert.expect(2);
  testWidth(assert, "file");
});

QUnit.test("Check width for tc:in", function (assert) {
  assert.expect(2);
  testWidth(assert, "in");
});

QUnit.test("Check width for input group", function (assert) {
  assert.expect(2);
  testWidth(assert, "inGroup");
});

QUnit.test("Check width for tc:out", function (assert) {
  assert.expect(2);
  testWidth(assert, "out");
});

QUnit.test("Check width for tc:selectBooleanCheckbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectBooleanCheckbox");
});

QUnit.test("Check width for tc:selectManyCheckbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectManyCheckbox");
});

QUnit.test("Check width for tc:selectManyListbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectManyListbox");
});

QUnit.test("Check width for tc:selectManyShuttle", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectManyShuttle");
});

QUnit.test("Check width for tc:selectOneChoice", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectOneChoice");
});

QUnit.test("Check width for tc:selectOneListbox", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectOneListbox");
});

QUnit.test("Check width for tc:selectOneRadio", function (assert) {
  assert.expect(2);
  testWidth(assert, "selectOneRadio");
});

QUnit.test("Check width for tc:textarea", function (assert) {
  assert.expect(2);
  testWidth(assert, "textarea");
});

function testWidth(assert, idPart) {
  var $compLabel = jQueryFrame("#page\\:mainForm\\:" + idPart + " label");
  var $compTopLabel = jQueryFrame("#page\\:mainForm\\:" + idPart + "Top label");

  assert.equal($compLabel.width(), "155");
  assert.equal($compTopLabel.width(), $compTopLabel.width());
}
