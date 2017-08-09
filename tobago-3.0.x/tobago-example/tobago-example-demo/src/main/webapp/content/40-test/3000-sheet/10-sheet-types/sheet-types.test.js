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

QUnit.test("Column 0: tc:columnSelector", function(assert) {
  assert.expect(4);

  var $column = getColumn(0);
  testWidthHeight(assert, $column, 50, 57, 13, 13);
});

QUnit.test("Column 1: tc:out", function(assert) {
  assert.expect(4);

  var $column = getColumn(1);
  testWidthHeight(assert, $column, 70, 57, 70, 40);
});

QUnit.test("Column 2: tc:out", function(assert) {
  assert.expect(4);

  var $column = getColumn(2);
  testWidthHeight(assert, $column, 70, 57, 70, 40);
});

QUnit.test("Column 3: tc:in", function(assert) {
  assert.expect(4);

  var $column = getColumn(3);
  testWidthHeight(assert, $column, 90, 57, 90, 38);
});

QUnit.test("Column 4: tc:date", function(assert) {
  assert.expect(4);

  var $column = getColumn(4);
  testWidthHeight(assert, $column, 160, 57, 160, 38);
});

QUnit.test("Column 5: tc:button", function(assert) {
  assert.expect(4);

  var $column = getColumn(5);
  testWidthHeight(assert, $column, 100, 57, 63, 38);
});

QUnit.test("Column 6: tc:link", function(assert) {
  assert.expect(4);

  var $column = getColumn(6);
  testWidthHeight(assert, $column, 70, 57, 29, 20);
});

QUnit.test("Column 7: tc:selectOneChoice", function(assert) {
  assert.expect(4);

  var $column = getColumn(7);
  testWidthHeight(assert, $column, 180, 57, 180, 40);
});

QUnit.test("Column 8: tc:image", function(assert) {
  assert.expect(4);

  var $column = getColumn(8);
  testWidthHeight(assert, $column, 90, 57, 72, 30);
});

QUnit.test("Column 9: tc:flexLayout", function(assert) {
  assert.expect(4);

  var $column = getColumn(9);
  testWidthHeight(assert, $column, 130, 57, 130, 56);
});

function testWidthHeight(assert, $column, width, height, childWidth, childHeight) {
  assert.equal($column.get(0).offsetWidth, width);
  assert.equal($column.get(0).offsetHeight, height);

  //maximal difference 2px
  var offsetWidth = $column.children().get(0).offsetWidth;
  var offsetHeight = $column.children().get(0).offsetHeight;
  var delta = 2;
  assert.ok(Math.abs(offsetWidth - childWidth) <= delta,
      "expected: " + childWidth + " result: " + offsetWidth + " delta: " + delta);
  assert.ok(Math.abs(offsetHeight - childHeight) <= delta,
      "expected: " + childHeight + " result: " + offsetHeight + " delta: " + delta);
}

function getColumn(number) {
  return jQueryFrame("tr").eq(1).find("td").eq(number);
}
