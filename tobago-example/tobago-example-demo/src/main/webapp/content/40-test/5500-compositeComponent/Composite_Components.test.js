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

QUnit.test("no layout manager", function (assert) {
  var inFn = jQueryFrameFn("#page\\:mainForm\\:noLayoutIn\\:in\\:\\:field");
  var outFn = jQueryFrameFn("#page\\:mainForm\\:noLayoutOut\\:out");
  var notRenderedFn = jQueryFrameFn("#page\\:mainForm\\:noLayoutNotRendered\\:in\\:\\:field");
  assert.expect(3);

  assert.equal(inFn().length, 1);
  assert.equal(outFn().length, 1);
  assert.equal(notRenderedFn().length, 0);
});

QUnit.test("flowlayout", function (assert) {
  var inFn = jQueryFrameFn("#page\\:mainForm\\:flowLayoutIn\\:in\\:\\:field");
  var outFn = jQueryFrameFn("#page\\:mainForm\\:flowLayoutOut\\:out");
  var notRenderedFn = jQueryFrameFn("#page\\:mainForm\\:flowLayoutNotRendered\\:in\\:\\:field");
  assert.expect(3);

  assert.equal(inFn().length, 1);
  assert.equal(outFn().length, 1);
  assert.equal(notRenderedFn().length, 0);
});

QUnit.test("flexlayout", function (assert) {
  var inFn = jQueryFrameFn("#page\\:mainForm\\:flexLayoutIn\\:in\\:\\:field");
  var outFn = jQueryFrameFn("#page\\:mainForm\\:flexLayoutOut\\:out");
  var notRenderedFn = jQueryFrameFn("#page\\:mainForm\\:flexLayoutNotRendered\\:in\\:\\:field");
  assert.expect(3);

  assert.equal(inFn().length, 1);
  assert.equal(outFn().length, 1);
  assert.equal(notRenderedFn().length, 0);
});

QUnit.test("segmentlayout", function (assert) {
  var inFn = jQueryFrameFn("#page\\:mainForm\\:segLayoutIn\\:in\\:\\:field");
  var outFn = jQueryFrameFn("#page\\:mainForm\\:segLayoutOut\\:out");
  var notRenderedFn = jQueryFrameFn("#page\\:mainForm\\:segLayoutNotRendered\\:in\\:\\:field");
  assert.expect(3);

  assert.equal(inFn().length, 1);
  assert.equal(outFn().length, 1);
  assert.equal(notRenderedFn().length, 0);
});

QUnit.test("gridlayout", function (assert) {
  var inFn = jQueryFrameFn("#page\\:mainForm\\:gridLayoutIn\\:in\\:\\:field");
  var outFn = jQueryFrameFn("#page\\:mainForm\\:gridLayoutOut\\:out");
  var notRenderedFn = jQueryFrameFn("#page\\:mainForm\\:gridLayoutNotRendered\\:in\\:\\:field");
  assert.expect(3);

  assert.equal(inFn().length, 1);
  assert.equal(outFn().length, 1);
  assert.equal(notRenderedFn().length, 0);
});

QUnit.test("splitlayout", function (assert) {
  var inFn = jQueryFrameFn("#page\\:mainForm\\:splitLayoutIn\\:in\\:\\:field");
  var outFn = jQueryFrameFn("#page\\:mainForm\\:splitLayoutOut\\:out");
  var notRenderedFn = jQueryFrameFn("#page\\:mainForm\\:splitLayoutNotRendered\\:in\\:\\:field");
  assert.expect(3);

  assert.equal(inFn().length, 1);
  assert.equal(outFn().length, 1);
  assert.equal(notRenderedFn().length, 0);
});
